#version 330
#moj_import <minecraft:globals.glsl>



uniform sampler2D InSampler;
in vec2 texCoord;
out vec4 fragColor;

const float timeScale = 100;
const int MAX_ITER = 100;
const float zoom = 2;


void main() {
    float time = GameTime * timeScale;
    vec2 C = vec2(
        cos(time + sin(time * 0.3) * 0.5) * 0.3 - 0.7,
        sin(time + cos(time * 0.2) * 0.4) * 0.25 + 0.27
    );
    vec2 pixel = 1.0 / ScreenSize;
    float aspect = ScreenSize.x / ScreenSize.y;


    // 2x2 grid supersampling
    vec3 col = vec3(0.0);
    vec2 offsets[4] = vec2[4](
        vec2(-0.25, -0.25),
        vec2( 0.25, -0.25),
        vec2(-0.25,  0.25),
        vec2( 0.25,  0.25)
    );

    for(int s = 0; s < 4; s++) {
        vec2 uv = texCoord + offsets[s] * pixel;
        vec2 z = (texCoord - 0.5) * zoom;
        z.x *= aspect;

        int i;
        for(i = 0; i < MAX_ITER; i++) {
            if(dot(z, z) > 4.0) break;
            z = vec2(z.x*z.x - z.y*z.y, 2.0*z.x*z.y) + C;
        }
        float t = (i == MAX_ITER) ? 0.0 : float(i) / float(MAX_ITER);


        float t2;
        if (i == MAX_ITER) {
            t2 = 0.0;
        } else {
            t2 = float(i) - log2(log2(dot(z, z))) + 4.0;
            t2 /= float(MAX_ITER);
            t2 = clamp(t2, 0.0, 1.0);
        }

        vec3 a = vec3(0.6, 0.4, 0.8);
        vec3 b = vec3(0.3, 0.1, 0.3);
        vec3 c = vec3(1.0, 1.0, 1.0);
        vec3 d = vec3(0.5, 0.5, 0.5);
        vec3 sample_col = a + b * cos(6.28318 * (c * t2 + d)); // cosine palette
        sample_col *= smoothstep(0.0, 0.15, t2);
        col += sample_col;
    }

    fragColor = vec4(col / 4.0, 1.0);
}