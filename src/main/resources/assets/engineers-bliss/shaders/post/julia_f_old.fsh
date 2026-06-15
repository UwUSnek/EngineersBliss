//#version 150
//
//uniform sampler2D InSampler;
//
//layout(std140) uniform SamplerInfo {
//    vec2 OutSize;
//    vec2 InSize;
//};
//
////layout(std140) uniform TimeConfig {
//    //float Time;
////};
//
//in vec2 texCoord;
//out vec4 fragColor;
//
//void main() {
//    float time = 1;
//    float size = 0.75;
//    vec2 uv = texCoord * 2.0 - 1.0;
//    uv.x *= OutSize.x / OutSize.y;
//    float cx = -0.7 + sin(time * 0.3) * 0.1;
//    float cy = 0.27015 + cos(time * 0.2) * 0.05;
//    float zx = uv.x * size;
//    float zy = uv.y * size;
//    int iter = 0;
//    for (int i = 0; i < 64; i++) {
//        float tmp = zx*zx - zy*zy + cx;
//        zy = 2.0*zx*zy + cy;
//        zx = tmp;
//        if (zx*zx + zy*zy > 4.0) { iter = i; break; }
//        iter = 64;
//    }
//    float t = float(iter) / 64.0;
//    vec3 col = vec3(
//        9.0*(1.0-t)*t*t*t,
//        15.0*(1.0-t)*(1.0-t)*t*t,
//        8.5*(1.0-t)*(1.0-t)*(1.0-t)*t
//    );
//    //fragColor = vec4(col, 1.0);
//    //fragColor = vec4(vec3(t), 1.0);
//vec2 uv = gl_FragCoord.xy / vec2(textureSize(InSampler, 0));
//fragColor = vec4(1, uv, 1);
//}




#version 150

in vec2 texCoord;
out vec4 fragColor;

void main() {
    fragColor = vec4(texCoord, 0.0, 1.0);
}