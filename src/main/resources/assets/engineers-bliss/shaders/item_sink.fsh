#version 150

#moj_import <minecraft:globals.glsl>

in vec4 vertexColor;
in vec2 texCoord;

out vec4 fragColor;

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

void main() {
    float t = GameTime * 1200.0;

    vec2 centered = texCoord - vec2(0.5);
    float radius = length(centered);
    float angle = atan(centered.y, centered.x);

    // differential rotation based on the distance from the center
    float spin = 1.4 / (radius + 0.12);
    float swirledAngle = angle + spin * 0.35 + t * 0.6;
    vec2 swirlPos = vec2(cos(swirledAngle), sin(swirledAngle)) * radius;

    // scrolling disk texture
    float turbulence = fbm(swirlPos * 7.0 + vec2(t * 0.35, -t * 0.25));

    // event horizon, black core
    float horizonMask = smoothstep(0.15, 0.21, radius);

    // photon ring
    float photonRing = exp(-pow((radius - 0.22) * 20.0, 2.0));

    // cccretion disk brightness
    float diskFalloff = (1.0 - smoothstep(0.2, 0.48, radius))
                       * smoothstep(0.17, 0.27, radius);

    vec3 diskColor = mix(vec3(1.0, 0.7, 0.3), vec3(0.65, 0.1, 0.85), turbulence);
    diskColor = mix(diskColor, vec3(1.0, 0.95, 0.85), turbulence * turbulence * 0.4);

    vec3 color = diskColor * diskFalloff;
    color += vec3(1.0, 0.9, 0.8) * photonRing;
    color *= horizonMask;

    // faint violet bleed
    color += vec3(0.06, 0.0, 0.14) * (1.0 - horizonMask) * 0.5;

    // fade effect out
    float edgeFade = 1.0 - smoothstep(0.34, 0.5, radius);
    color *= edgeFade;

    fragColor = vec4(color, 1.0) * vertexColor;
}
