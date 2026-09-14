#version 150


uniform sampler2D Sampler0;

in vec2 localPos;
in vec2 rectSize;
in vec2 uv;
in float alpha;

out vec4 fragColor;




void main() {
    vec2 rectLow  = max(localPos - 0.5, vec2(0.0));
    vec2 rectHigh = min(localPos + 0.5, rectSize);
    vec2 coverage = clamp(rectHigh - rectLow, 0.0, 1.0);

    vec4 texColor = texture(Sampler0, uv);

    float finalAlpha = alpha * texColor.a * coverage.x * coverage.y;
    if(finalAlpha <= 0.0) {
        discard;
    }

    fragColor = vec4(texColor.rgb, finalAlpha);
}