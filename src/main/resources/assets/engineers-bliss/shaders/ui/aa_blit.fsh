#version 150


uniform sampler2D Sampler0;

in vec2 localPos;
in vec2 rectSize;
in vec2 uv;
in float alpha;

out vec4 fragColor;




void main() {

    //! Clamp UVs between 0 and 1 so they don't wrap around
    vec2 texSize = vec2(textureSize(Sampler0, 0));
    vec2 uvMin = vec2(0.5) / texSize;
    vec2 uvMax = vec2(1.0) - vec2(0.5) / texSize;
    vec4 texColor = texture(Sampler0, clamp(uv, uvMin, uvMax));

    // Calculate coverage and final alpha. Discard if transparent.
    vec2 rectLow  = max(localPos - 0.5, vec2(0.0));
    vec2 rectHigh = min(localPos + 0.5, rectSize);
    vec2 coverage = clamp(rectHigh - rectLow, 0.0, 1.0);
    float finalAlpha = alpha * texColor.a * coverage.x * coverage.y;
    if(finalAlpha <= 0.0) {
        discard;
    }

    // Output color
    fragColor = vec4(texColor.rgb, finalAlpha);
}