#version 150
#define MINECRAFT

#ifdef MINECRAFT
    #moj_import <minecraft:globals.glsl>
    in vec4 vertexColor;
    in vec2 uv0;
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
    float horizon  = 0.22;
    float angle = atan(uv.y, uv.x);


    // differential rotation based on the distance from the center
    float spin = 1.0 / (distance);
    float swirledAngle = angle + spin * 0.8 + t * 0.1;
    vec2 swirlPos = vec2(cos(swirledAngle), sin(swirledAngle)) * distance;

    // scrolling disk texture
    float turbulence = fbm(swirlPos * 8.0 + vec2(t * 0.35, -t * 0.25));

    // event horizon, black core
    float horizonMask = smoothstep(0.19, horizon, distance);

    // photon ring
    float photonRing = exp(-pow((distance - horizon) * 20.0, 2.0));

    // accretion disk brightness
    float diskFalloff = (1.0 - smoothstep(horizon, 0.7, sqrt(distance)));

    vec3 diskColor = mix(vec3(1.0, 0.7, 0.3), vec3(1.0, 0.15, 0.15), turbulence);
    diskColor = mix(diskColor, vec3(1.0, 0.95, 0.85), turbulence * turbulence * 0.4);

    vec3 color = diskColor * diskFalloff;
    color += vec3(1.0, 0.95, 0.9) * photonRing;
    color *= horizonMask;


    fragColor = vec4(color, max(photonRing, max(diskFalloff, 1.0 - horizonMask)));
    //fragColor = vec4(abs(swirlPos), 0.0, 0.0);
    //fragColor = vec4(max(diskFalloff, 1.0 - horizonMask), 1.0, 1.0, 1.0);
}