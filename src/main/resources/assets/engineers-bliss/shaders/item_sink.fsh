#version 150
#define MINECRAFT

#ifdef MINECRAFT
    #moj_import <minecraft:globals.glsl>

    in vec4 vertexColor;
    in vec2 uv0;

    uniform sampler2D SceneSampler;
    uniform sampler2D SceneDepthSampler; //TODO remove if not used

    out vec4 fragColor;
#endif




#define TAU 6.28318530718




float hash21(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}

float valueNoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    float a = hash21(i);
    float b = hash21(i + vec2(1.0, 0.0));
    float c = hash21(i + vec2(0.0, 1.0));
    float d = hash21(i + vec2(1.0, 1.0));
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

float fbm(vec2 p) {
    float sum = 0.0;
    float amp = 0.5;
    for (int i = 0; i < 4; i++) {
        sum += amp * valueNoise(p);
        p *= 2.0;
        amp *= 0.5;
    }
    return sum;
}




vec4 apply_lensing_background(vec4 objectColor, vec2 uv, float distance, float horizon){

    // Calculate the distance from the event horizon
    float maxHorizonDistance = 0.5 - horizon;
    float horizonDistance = clamp(abs(distance - horizon), 0.0, maxHorizonDistance);

    // Calculate lensing effect strength. The closer to the horizon, the stronger the effect.
    float lensStrength = 1.0 - (horizonDistance / maxHorizonDistance);
    lensStrength = pow(lensStrength, 3.0);

    // Calculate lensed UVs
    vec2 direction = normalize(uv);
    // vec2 screenUV = uv + 0.5;
    vec2 screenUV = gl_FragCoord.xy / vec2(textureSize(SceneSampler, 0));
    float bendAmount = lensStrength * 0.2;
    vec2 distortedUV = screenUV - direction * bendAmount;

    // Calculate color of the lensed background and overlay the object's color on top of it
    vec4 sceneColor = texture(SceneSampler, clamp(distortedUV, 0.0, 1.0));
    return vec4(mix(sceneColor.rgb, objectColor.rgb, objectColor.a), 1.0);
}




#ifdef MINECRAFT
void main() {
#else
void mainImage(out vec4 fragColor, in vec2 fragCoord) {
    vec2 texCoord = fragCoord / iResolution.xy;
#endif


    #ifdef MINECRAFT
        float t = GameTime * 1200.0;
        vec2 uv = uv0 - vec2(0.5);
        // vec2 centered = texCoord;
    #else
        float t = iTime;
        vec2 uv = fragCoord / iResolution.xy;
        uv = uv - vec2(0.5);
        // vec2 centered = uv;
        uv.x *= iResolution.x / iResolution.y;
    #endif




    float distance = length(uv);
    float horizon  = 0.2;
    float horizonFalloff  = 0.9;
    float outerRadius  = horizon * 2.0;
    float angle = atan(uv.y, uv.x);


    // Differential rotation based on the distance from the center
    float spin = horizon / distance;
    float swirledAngle = angle + spin * 1.5 + t * 0.03;
    vec2 swirlPos = vec2(cos(swirledAngle), sin(swirledAngle)) * distance;

    // Disk noise texture
    float driftedAngle = angle + t * 0.1;
    vec2 driftPos = vec2(cos(driftedAngle), sin(driftedAngle)) * distance;
    float turbulence = fbm(swirlPos * 16.0 + driftPos * 2.0);

    // Event horizon, black core
    float horizonMask = smoothstep(horizon * horizonFalloff, horizon, distance);

    // Photon ring
    float photonRing = exp(-pow((distance - horizon) * (8.0 / horizon), 2.0));

    // Accretion disk
    float diskFalloff = 1.0 - smoothstep(horizon, outerRadius, distance);
    diskFalloff = pow(diskFalloff, 3.0);


    vec3 diskColor = mix(vec3(0.2, 0.1, 0.05), vec3(1.0, 0.15, 0.15), turbulence);
    diskColor = mix(diskColor, vec3(1.0, 0.95, 0.85), turbulence * turbulence * 0.4);

    vec3 color = diskColor * diskFalloff;
    color += vec3(1.0, 0.95, 0.9) * photonRing;
    color *= horizonMask;




    vec4 objectColor = vec4(color, max(photonRing, max(diskFalloff, 1.0 - horizonMask)));
    fragColor = apply_lensing_background(objectColor, uv, distance, horizon);
}