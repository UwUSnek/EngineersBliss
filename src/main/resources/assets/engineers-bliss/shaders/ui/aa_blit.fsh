#version 150


uniform sampler2D Sampler0;

in vec4 vertexColor;
in vec2 localPos;
in vec2 rectSize;
in vec2 texCoord;

out vec4 fragColor;




void main() {
    vec2 rectLow  = max(localPos - 0.5, vec2(0.0));
    vec2 rectHigh = min(localPos + 0.5, rectSize);
    vec2 coverage = clamp(rectHigh - rectLow, 0.0, 1.0);

    vec4 texColor = texture(Sampler0, texCoord);

    float alpha = vertexColor.a * texColor.a * coverage.x * coverage.y;
    if(alpha <= 0.0) {
        discard;
    }

    fragColor = vec4(vertexColor.rgb * texColor.rgb, alpha);
}