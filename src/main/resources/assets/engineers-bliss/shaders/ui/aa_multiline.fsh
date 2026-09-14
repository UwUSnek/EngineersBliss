#version 150

in vec4  vertexColor;
in float vCrossDist;
in float vHalfThickness;

out vec4 fragColor;

void main() {
    float dist = abs(vCrossDist);
    float aa = fwidth(dist);
    float alpha = 1.0 - smoothstep(vHalfThickness - aa, vHalfThickness + aa, dist);
    fragColor = vec4(vertexColor.rgb, vertexColor.a * alpha);
}