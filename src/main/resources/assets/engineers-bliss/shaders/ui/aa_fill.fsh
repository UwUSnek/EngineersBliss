#version 150

in vec2 localPos;
in vec2 rectSize;
in vec4 vertexColor;
flat in int ditheringStrength;

out vec4 fragColor;




// 8x8 Bayer matrix for dithering
const float bayer8x8[64] = float[64](
    00.0/64.0, 32.0/64.0, 08.0/64.0, 40.0/64.0, 02.0/64.0, 34.0/64.0, 10.0/64.0, 42.0/64.0,
    48.0/64.0, 16.0/64.0, 56.0/64.0, 24.0/64.0, 50.0/64.0, 18.0/64.0, 58.0/64.0, 26.0/64.0,
    12.0/64.0, 44.0/64.0, 04.0/64.0, 36.0/64.0, 14.0/64.0, 46.0/64.0, 06.0/64.0, 38.0/64.0,
    60.0/64.0, 28.0/64.0, 52.0/64.0, 20.0/64.0, 62.0/64.0, 30.0/64.0, 54.0/64.0, 22.0/64.0,
    03.0/64.0, 35.0/64.0, 11.0/64.0, 43.0/64.0, 01.0/64.0, 33.0/64.0, 09.0/64.0, 41.0/64.0,
    51.0/64.0, 19.0/64.0, 59.0/64.0, 27.0/64.0, 49.0/64.0, 17.0/64.0, 57.0/64.0, 25.0/64.0,
    15.0/64.0, 47.0/64.0, 07.0/64.0, 39.0/64.0, 13.0/64.0, 45.0/64.0, 05.0/64.0, 37.0/64.0,
    63.0/64.0, 31.0/64.0, 55.0/64.0, 23.0/64.0, 61.0/64.0, 29.0/64.0, 53.0/64.0, 21.0/64.0
);




void main() {
    vec2 rectLow  = max(localPos - 0.5, vec2(0.0));
    vec2 rectHigh = min(localPos + 0.5, rectSize);
    vec2 coverage = clamp(rectHigh - rectLow, 0.0, 1.0);

    // Calculate alpha, discard if fully transparent
    float alpha = vertexColor.a * coverage.x * coverage.y;
    if(alpha <= 0.0) {
        discard;
    }

    // Apply ordered dither to the alpha channel. This reduces banding in gradients.
    ivec2 texel = ivec2(mod(gl_FragCoord.xy, 8.0));
    float dither = bayer8x8[texel.y * 8 + texel.x] - 0.5;
    alpha += dither * ditheringStrength / 255.0; // 2x(1/255) dirthering strength

    // Output color
    fragColor = vec4(vertexColor.rgb, alpha);
}