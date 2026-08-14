
#ifdef MINECRAFT
    #moj_import <minecraft:projection.glsl>
    #moj_import <engineers-bliss:utils/hash.glsl>
#endif



#define PI  3.14159265359
#define TAU 6.28318530718





float valueNoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    float a = hash12(i);
    float b = hash12(i + vec2(1.0, 0.0));
    float c = hash12(i + vec2(0.0, 1.0));
    float d = hash12(i + vec2(1.0, 1.0));
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}


float fbm(vec2 p) {
    float sum = 0.0;
    float amp = 0.5;
    for(int i = 0; i < 4; i++) {
        sum += amp * valueNoise(p);
        p *= 2.0;
        amp *= 0.5;
    }
    return sum;
}


float valueNoise3D(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    vec3 u = f * f * (3.0 - 2.0 * f);

    float n000 = hash13(i + vec3(0.0, 0.0, 0.0));
    float n100 = hash13(i + vec3(1.0, 0.0, 0.0));
    float n010 = hash13(i + vec3(0.0, 1.0, 0.0));
    float n110 = hash13(i + vec3(1.0, 1.0, 0.0));
    float n001 = hash13(i + vec3(0.0, 0.0, 1.0));
    float n101 = hash13(i + vec3(1.0, 0.0, 1.0));
    float n011 = hash13(i + vec3(0.0, 1.0, 1.0));
    float n111 = hash13(i + vec3(1.0, 1.0, 1.0));

    float nx00 = mix(n000, n100, u.x);
    float nx10 = mix(n010, n110, u.x);
    float nx01 = mix(n001, n101, u.x);
    float nx11 = mix(n011, n111, u.x);

    float nxy0 = mix(nx00, nx10, u.y);
    float nxy1 = mix(nx01, nx11, u.y);

    return mix(nxy0, nxy1, u.z);
}


float fbm3D(vec3 p) {
    float sum = 0.0;
    float amp = 0.5;
    for(int i = 0; i < 4; i++) {
        sum += amp * valueNoise3D(p);
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
vec4 over(vec4 l0, vec4 l1, vec4 l2) {
    return over(l0, over(l1, l2));
}
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3) {
    return over(l0, over(l1, l2, l3));
}
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3, vec4 l4) {
    return over(l0, over(l1, l2, l3, l4));
}
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3, vec4 l4, vec4 l5) {
    return over(l0, over(l1, l2, l3, l4, l5));
}
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3, vec4 l4, vec4 l5, vec4 l6) {
    return over(l0, over(l1, l2, l3, l4, l5, l6));
}
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3, vec4 l4, vec4 l5, vec4 l6, vec4 l7) {
    return over(l0, over(l1, l2, l3, l4, l5, l6, l7));
}
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3, vec4 l4, vec4 l5, vec4 l6, vec4 l7, vec4 l8) {
    return over(l0, over(l1, l2, l3, l4, l5, l6, l7, l8));
}








// Swaps depth and color pairs so depthA <= depthB
void depthCompareSwap(inout float depthA, inout float depthB, inout vec4 colorA, inout vec4 colorB) {
    if(depthA > depthB) {
        float td = depthA; depthA = depthB; depthB = td;
        vec4 tc = colorA; colorA = colorB; colorB = tc;
    }
}

vec3 adjustContrast(vec3 color, float contrast) {
    return clamp((color - 0.5) * contrast + 0.5, 0.0, 1.0);
}

float adjustContrast(float n, float contrast) {
    return clamp((n - 0.5) * contrast + 0.5, 0.0, 1.0);
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
            0.0,        f,   0.0, 0.0,
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