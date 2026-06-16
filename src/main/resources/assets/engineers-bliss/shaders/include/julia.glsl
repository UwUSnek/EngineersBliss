
// number of lobes imported by fsh shader implementation

#moj_import <minecraft:globals.glsl>


uniform sampler2D InSampler;
in vec2 texCoord;
out vec4 fragColor;

const float timeScale = 100;
const float rotSpeed = 0.25;
const float zoom = 2;

const float STAR_SPARSITY = 0.997;

//! High powers escape very quickly. too many iterations make the animation flash, this reduces the number proportionally
//! 120 is good for 2 lobes
const int MAX_ITER = int(120.0 / min(1.0, (LOBES - 1.0) / 2.0));




void main() {

    // Calculate constants (for this shader iteration)
    vec2 pixel = 1.0 / ScreenSize;
    float aspect = ScreenSize.x / ScreenSize.y;
    vec3 col = vec3(0.0); //! Output color value



    // 2x temporal blending
    //! Temporal blending blends unstable regions of the julia set so they don't make people's eyes explode
    for(int frame = 0; frame < 4; frame++) {
        float timeShift = frame * (1.0 / 1200000.0); //! Shift forward 1/1000th of a second per ""frame""
        float time = (GameTime + timeShift) * timeScale;
        float angle = time * rotSpeed;
        float angle_s = sin(angle), angle_c = cos(angle);


        // Calculate C
        vec2 C = vec2(
            cos(time + sin(time * 0.3) * 0.5) * 0.3 - 0.7,
            sin(time + cos(time * 0.2) * 0.4) * 0.25 + 0.27
        );


        vec2 offsets[4] = vec2[4](
            vec2(-0.25, -0.25),
            vec2( 0.25, -0.25),
            vec2(-0.25,  0.25),
            vec2( 0.25,  0.25)
        );

        // 2x2 grid supersampling
        //! Supersampling smooths out sharp and chunchy edges
        // for(int s = 0; s < 4; s++) {
        for(int s = 0; s < 1; s++) {
            // vec2 uv = texCoord + offsets[s] * pixel;
            vec2 uv = texCoord;
            vec2 z = (uv - 0.5) * zoom;
            z.x *= aspect;

            // Animate rotation
            z = vec2(z.x*angle_c - z.y*angle_s, z.x*angle_s + z.y*angle_c);
            vec2 rotated_uv = vec2(uv.x*(-angle_c) - uv.y*(-angle_s), uv.x*(-angle_s) + uv.y*(-angle_c));

            int i;
            for(i = 0; i < MAX_ITER; i++) {
                if(dot(z, z) > 4.0) break;
                // Animate R
                float r = pow(dot(z,z), LOBES * 0.5);
                float theta = LOBES * atan(z.y, z.x);
                z = r * vec2(cos(theta), sin(theta)) + C;
            }
            float t = (i == MAX_ITER) ? 0.0 : float(i) / float(MAX_ITER);


            float t2;
            vec3 sample_col; //! Chosen based on zone

            // Draw stars inside the set
            if(i == MAX_ITER) {
                vec2 starUV = floor(rotated_uv * ScreenSize);
                float star = fract(sin(starUV.x * 127.1 + starUV.y * 311.7) * 43758.5453 +
                    cos(starUV.x * 269.5 + starUV.y * 183.3) * 12345.6789);
                float brightness = fract(sin(starUV.x * 269.5 + starUV.y * 183.3) * 43758.5453);
                sample_col = vec3(step(STAR_SPARSITY, star) * (0.7 * brightness));
            }

            // Draw color gradient outside
            else {

                t2 = float(i) - log2(log2(dot(z, z))) + 4.0;
                t2 /= float(MAX_ITER);
                t2 = clamp(t2, 0.0, 1.0);

                vec3 a = vec3(0.6, 0.4, 0.8);
                vec3 b = vec3(0.3, 0.1, 0.3);
                vec3 c = vec3(1.0, 1.0, 1.0);
                vec3 d = vec3(0.5, 0.5, 0.5);
                sample_col = a + b * cos(6.28318 * (c * t2 + d)); // cosine palette
                sample_col *= smoothstep(0.0, 0.15, t2);
            }
            col += sample_col;
        }
    }

    // fragColor = vec4(col / (4.0 * 2.0), 1.0);
    fragColor = vec4(col / 4.0, 1.0);
}