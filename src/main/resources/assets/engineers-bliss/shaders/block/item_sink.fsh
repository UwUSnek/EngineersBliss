#version 150
#define MINECRAFT

#ifdef MINECRAFT
    #moj_import <minecraft:globals.glsl>
    #moj_import <engineers-bliss:utils.glsl>
    #moj_import <engineers-bliss:gravitational_lensing.glsl>

    in vec4 vertexColor;
    in vec2 uv0;

    uniform sampler2D SceneSampler;
    uniform sampler2D SceneDepthSampler;

    out vec4 fragColor;
#endif








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
    fragColor = apply_lensing_background(SceneSampler, SceneDepthSampler, objectColor, uv, distance, horizon);
}