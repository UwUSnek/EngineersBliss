#version 330


uniform sampler2D InSampler;

layout(std140) uniform BlurConfig {
    vec2 BlurDir;
    float Radius;
};

in vec2 texCoord;

out vec4 fragColor;




void main() {
    vec2 sampleStep = BlurDir / vec2(textureSize(InSampler, 0));
    float sigma = max(Radius * 0.5, 0.0001);
    float falloff = -1.0 / (2.0 * sigma * sigma);

    vec3 acc = vec3(0.0);
    float weightSum = 0.0;
    for(float i = -Radius; i <= Radius; i += 1.0) {
        float weight = exp(i * i * falloff);
        acc += texture(InSampler, texCoord + sampleStep * i).rgb * weight;
        weightSum += weight;
    }

    fragColor = vec4(acc / weightSum, 1.0);
}
