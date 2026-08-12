// #moj_import <minecraft:projection.glsl>


// #define PI  3.14159265359
// #define TAU 6.28318530718




// float hash21(vec2 p) {
//     p = fract(p * vec2(123.34, 456.21));
//     p += dot(p, p + 45.32);
//     return fract(p.x * p.y);
// }

// float valueNoise(vec2 p) {
//     vec2 i = floor(p);
//     vec2 f = fract(p);
//     float a = hash21(i);
//     float b = hash21(i + vec2(1.0, 0.0));
//     float c = hash21(i + vec2(0.0, 1.0));
//     float d = hash21(i + vec2(1.0, 1.0));
//     vec2 u = f * f * (3.0 - 2.0 * f);
//     return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
// }

// float fbm(vec2 p) {
//     float sum = 0.0;
//     float amp = 0.5;
//     for (int i = 0; i < 4; i++) {
//         sum += amp * valueNoise(p);
//         p *= 2.0;
//         amp *= 0.5;
//     }
//     return sum;
// }




// float linearizeDepth(float ndcZ) {
//     return ProjMat[3][2] / (ProjMat[2][2] + ndcZ);
// }



// vec2 rotate(vec2 p, float a) {
//     float s = sin(a);
//     float c = cos(a);
//     return mat2(c, -s, s, c) * p;
// }



// vec4 over(vec4 top, vec4 bottom) {
//     float outAlpha = top.a + bottom.a * (1.0 - top.a);
//     vec3 outColor = (top.rgb * top.a + bottom.rgb * bottom.a * (1.0 - top.a)) / max(outAlpha, 0.0001);
//     return vec4(outColor, outAlpha);
// }




#ifdef MINECRAFT
    #moj_import <minecraft:projection.glsl>
#endif


#define PI  3.14159265359
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


vec2 rotate(vec2 p, float a) {
    float s = sin(a);
    float c = cos(a);
    return mat2(c, -s, s, c) * p;
}


vec4 over(vec4 top, vec4 bottom) {
    float outAlpha = top.a + bottom.a * (1.0 - top.a);
    vec3 outColor = (top.rgb * top.a + bottom.rgb * bottom.a * (1.0 - top.a)) / max(outAlpha, 0.0001);
    return vec4(outColor, outAlpha);
}


vec3 adjustContrast(vec3 color, float contrast) {
    return clamp((color - 0.5) * contrast + 0.5, 0.0, 1.0);
}


#ifdef MINECRAFT
    float linearizeDepth(float ndcZ) {
        return ProjMat[3][2] / (ProjMat[2][2] + ndcZ);
    }
#else
    float linearizeDepth(float ndcZ) {
        return ndcZ;
    }
#endif



#ifdef MINECRAFT
    mat4 getViewMatrix() {
        return ModelViewMat;
    }

    mat4 getProjMatrix() {
        return ProjMat;
    }
#else
    mat4 cameraToWorldMatrix(vec3 eye, vec3 target, vec3 up) {
        vec3 f = normalize(target - eye);
        vec3 r = normalize(cross(f, up));
        vec3 u = cross(r, f);
        return mat4(
            vec4(r, 0.0),
            vec4(u, 0.0),
            vec4(-f, 0.0),
            vec4(eye, 1.0)
        );
    }

    mat4 fakePerspectiveMatrix(float fovY, float aspect, float near, float far) {
        float f = 1.0 / tan(fovY * 0.5);
        return mat4(
            f / aspect, 0.0, 0.0, 0.0,
            0.0, f, 0.0, 0.0,
            0.0, 0.0, (far + near) / (near - far), -1.0,
            0.0, 0.0, (2.0 * far * near) / (near - far), 0.0
        );
    }

    vec3 getOrbitCameraPos() {
        float orbitRadius = 3.0;
        float orbitHeight = 1.2;
        float orbitSpeed  = 0.15;
        float angle = iTime * orbitSpeed;
        return vec3(cos(angle) * orbitRadius, orbitHeight, sin(angle) * orbitRadius);
    }

    mat4 getViewMatrix() {
        mat4 camToWorld = cameraToWorldMatrix(getOrbitCameraPos(), vec3(0.0), vec3(0.0, 1.0, 0.0));
        return inverse(camToWorld);
    }

    mat4 getProjMatrix() {
        return fakePerspectiveMatrix(radians(50.0), iResolution.x / iResolution.y, 0.05, 100.0);
    }
#endif