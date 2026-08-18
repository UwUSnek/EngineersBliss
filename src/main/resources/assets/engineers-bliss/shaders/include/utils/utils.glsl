
#ifdef MINECRAFT
    #moj_import <minecraft:projection.glsl>
    #moj_import <engineers-bliss:utils/hash.glsl>
#endif



#define PI  3.14159265359
#define TAU 6.28318530718








float valueNoise(float p) {
    float i = floor(p);
    float f = fract(p);
    float a = hash11(i);
    float b = hash11(i + 1.0);
    float u = f * f * (3.0 - 2.0 * f);
    return mix(a, b, u);
}

float fbm(float p) {
    float sum = 0.0;
    float amp = 0.5;
    for(int i = 0; i < 4; i++) {
        sum += amp * valueNoise(p);
        p *= 2.0;
        amp *= 0.5;
    }
    return sum;
}


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


float valueNoise(vec3 p) {
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
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3, vec4 l4, vec4 l5, vec4 l6, vec4 l7, vec4 l8, vec4 l9) {
    return over(l0, over(l1, l2, l3, l4, l5, l6, l7, l8, l9));
}
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3, vec4 l4, vec4 l5, vec4 l6, vec4 l7, vec4 l8, vec4 l9, vec4 l10) {
    return over(l0, over(l1, l2, l3, l4, l5, l6, l7, l8, l9, l10));
}
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3, vec4 l4, vec4 l5, vec4 l6, vec4 l7, vec4 l8, vec4 l9, vec4 l10, vec4 l11) {
    return over(l0, over(l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11));
}
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3, vec4 l4, vec4 l5, vec4 l6, vec4 l7, vec4 l8, vec4 l9, vec4 l10, vec4 l11, vec4 l12) {
    return over(l0, over(l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12));
}
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3, vec4 l4, vec4 l5, vec4 l6, vec4 l7, vec4 l8, vec4 l9, vec4 l10, vec4 l11, vec4 l12, vec4 l13) {
    return over(l0, over(l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13));
}
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3, vec4 l4, vec4 l5, vec4 l6, vec4 l7, vec4 l8, vec4 l9, vec4 l10, vec4 l11, vec4 l12, vec4 l13, vec4 l14) {
    return over(l0, over(l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14));
}
vec4 over(vec4 l0, vec4 l1, vec4 l2, vec4 l3, vec4 l4, vec4 l5, vec4 l6, vec4 l7, vec4 l8, vec4 l9, vec4 l10, vec4 l11, vec4 l12, vec4 l13, vec4 l14, vec4 l15) {
    return over(l0, over(l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14, l15));
}








// Depth-sorts 2 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, vec4 background) {
    float depth[2] = float[2](d0, d1);
    vec4  color[2] =  vec4[2](l0, l1);
    for(int i = 1; i < 2; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1]), background);
}

// Depth-sorts 3 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, vec4 background) {
    float depth[3] = float[3](d0, d1, d2);
    vec4  color[3] =  vec4[3](l0, l1, l2);
    for(int i = 1; i < 3; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2]), background);
}

// Depth-sorts 4 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, vec4 background) {
    float depth[4] = float[4](d0, d1, d2, d3);
    vec4  color[4] =  vec4[4](l0, l1, l2, l3);
    for(int i = 1; i < 4; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3]), background);
}

// Depth-sorts 5 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, float d4, vec4 l4, vec4 background) {
    float depth[5] = float[5](d0, d1, d2, d3, d4);
    vec4  color[5] =  vec4[5](l0, l1, l2, l3, l4);
    for(int i = 1; i < 5; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3], color[4]), background);
}

// Depth-sorts 6 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, float d4, vec4 l4, float d5, vec4 l5, vec4 background) {
    float depth[6] = float[6](d0, d1, d2, d3, d4, d5);
    vec4  color[6] =  vec4[6](l0, l1, l2, l3, l4, l5);
    for(int i = 1; i < 6; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3], color[4], color[5]), background);
}

// Depth-sorts 7 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, float d4, vec4 l4, float d5, vec4 l5, float d6, vec4 l6, vec4 background) {
    float depth[7] = float[7](d0, d1, d2, d3, d4, d5, d6);
    vec4  color[7] =  vec4[7](l0, l1, l2, l3, l4, l5, l6);
    for(int i = 1; i < 7; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3], color[4], color[5], color[6]), background);
}

// Depth-sorts 8 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, float d4, vec4 l4, float d5, vec4 l5, float d6, vec4 l6, float d7, vec4 l7, vec4 background) {
    float depth[8] = float[8](d0, d1, d2, d3, d4, d5, d6, d7);
    vec4  color[8] =  vec4[8](l0, l1, l2, l3, l4, l5, l6, l7);
    for(int i = 1; i < 8; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3], color[4], color[5], color[6], color[7]), background);
}

// Depth-sorts 9 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, float d4, vec4 l4, float d5, vec4 l5, float d6, vec4 l6, float d7, vec4 l7, float d8, vec4 l8, vec4 background) {
    float depth[9] = float[9](d0, d1, d2, d3, d4, d5, d6, d7, d8);
    vec4  color[9] =  vec4[9](l0, l1, l2, l3, l4, l5, l6, l7, l8);
    for(int i = 1; i < 9; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3], color[4], color[5], color[6], color[7], color[8]), background);
}

// Depth-sorts 10 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, float d4, vec4 l4, float d5, vec4 l5, float d6, vec4 l6, float d7, vec4 l7, float d8, vec4 l8, float d9, vec4 l9, vec4 background) {
    float depth[10] = float[10](d0, d1, d2, d3, d4, d5, d6, d7, d8, d9);
    vec4  color[10] =  vec4[10](l0, l1, l2, l3, l4, l5, l6, l7, l8, l9);
    for(int i = 1; i < 10; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3], color[4], color[5], color[6], color[7], color[8], color[9]), background);
}

// Depth-sorts 11 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, float d4, vec4 l4, float d5, vec4 l5, float d6, vec4 l6, float d7, vec4 l7, float d8, vec4 l8, float d9, vec4 l9, float d10, vec4 l10, vec4 background) {
    float depth[11] = float[11](d0, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10);
    vec4  color[11] =  vec4[11](l0, l1, l2, l3, l4, l5, l6, l7, l8, l9, l10);
    for(int i = 1; i < 11; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3], color[4], color[5], color[6], color[7], color[8], color[9], color[10]), background);
}

// Depth-sorts 12 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, float d4, vec4 l4, float d5, vec4 l5, float d6, vec4 l6, float d7, vec4 l7, float d8, vec4 l8, float d9, vec4 l9, float d10, vec4 l10, float d11, vec4 l11, vec4 background) {
    float depth[12] = float[12](d0, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11);
    vec4  color[12] =  vec4[12](l0, l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11);
    for(int i = 1; i < 12; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3], color[4], color[5], color[6], color[7], color[8], color[9], color[10], color[11]), background);
}

// Depth-sorts 13 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, float d4, vec4 l4, float d5, vec4 l5, float d6, vec4 l6, float d7, vec4 l7, float d8, vec4 l8, float d9, vec4 l9, float d10, vec4 l10, float d11, vec4 l11, float d12, vec4 l12, vec4 background) {
    float depth[13] = float[13](d0, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
    vec4  color[13] =  vec4[13](l0, l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12);
    for(int i = 1; i < 13; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3], color[4], color[5], color[6], color[7], color[8], color[9], color[10], color[11], color[12]), background);
}

// Depth-sorts 14 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, float d4, vec4 l4, float d5, vec4 l5, float d6, vec4 l6, float d7, vec4 l7, float d8, vec4 l8, float d9, vec4 l9, float d10, vec4 l10, float d11, vec4 l11, float d12, vec4 l12, float d13, vec4 l13, vec4 background) {
    float depth[14] = float[14](d0, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
    vec4  color[14] =  vec4[14](l0, l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13);
    for(int i = 1; i < 14; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3], color[4], color[5], color[6], color[7], color[8], color[9], color[10], color[11], color[12], color[13]), background);
}

// Depth-sorts 15 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, float d4, vec4 l4, float d5, vec4 l5, float d6, vec4 l6, float d7, vec4 l7, float d8, vec4 l8, float d9, vec4 l9, float d10, vec4 l10, float d11, vec4 l11, float d12, vec4 l12, float d13, vec4 l13, float d14, vec4 l14, vec4 background) {
    float depth[15] = float[15](d0, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14);
    vec4  color[15] =  vec4[15](l0, l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14);
    for(int i = 1; i < 15; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3], color[4], color[5], color[6], color[7], color[8], color[9], color[10], color[11], color[12], color[13], color[14]), background);
}

// Depth-sorts 16 layers and composites them over the background.
vec4 compositeColorLayers(float d0, vec4 l0, float d1, vec4 l1, float d2, vec4 l2, float d3, vec4 l3, float d4, vec4 l4, float d5, vec4 l5, float d6, vec4 l6, float d7, vec4 l7, float d8, vec4 l8, float d9, vec4 l9, float d10, vec4 l10, float d11, vec4 l11, float d12, vec4 l12, float d13, vec4 l13, float d14, vec4 l14, float d15, vec4 l15, vec4 background) {
    float depth[16] = float[16](d0, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14, d15);
    vec4  color[16] =  vec4[16](l0, l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14, l15);
    for(int i = 1; i < 16; i++) {
        for(int j = i; j > 0; j--) {
            depthCompareSwap(depth[j - 1], depth[j], color[j - 1], color[j]);
        }
    }
    return over(over(color[0], color[1], color[2], color[3], color[4], color[5], color[6], color[7], color[8], color[9], color[10], color[11], color[12], color[13], color[14], color[15]), background);
}








vec2 nearestDepthLayer(float d0, float ndc0, float alpha0) {
    return alpha0 > 0.001 ? vec2(d0, ndc0) : vec2(1e30, -1.0);
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0);
    vec2 b = nearestDepthLayer(d1, ndc1, alpha1);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1);
    vec2 b = nearestDepthLayer(d2, ndc2, alpha2);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2);
    vec2 b = nearestDepthLayer(d3, ndc3, alpha3);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3);
    vec2 b = nearestDepthLayer(d4, ndc4, alpha4);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4);
    vec2 b = nearestDepthLayer(d5, ndc5, alpha5);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5);
    vec2 b = nearestDepthLayer(d6, ndc6, alpha6);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6);
    vec2 b = nearestDepthLayer(d7, ndc7, alpha7);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7);
    vec2 b = nearestDepthLayer(d8, ndc8, alpha8);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8);
    vec2 b = nearestDepthLayer(d9, ndc9, alpha9);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float d10, float ndc10, float alpha10) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9);
    vec2 b = nearestDepthLayer(d10, ndc10, alpha10);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float d10, float ndc10, float alpha10, float d11, float ndc11, float alpha11) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9, d10, ndc10, alpha10);
    vec2 b = nearestDepthLayer(d11, ndc11, alpha11);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float d10, float ndc10, float alpha10, float d11, float ndc11, float alpha11, float d12, float ndc12, float alpha12) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9, d10, ndc10, alpha10, d11, ndc11, alpha11);
    vec2 b = nearestDepthLayer(d12, ndc12, alpha12);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float d10, float ndc10, float alpha10, float d11, float ndc11, float alpha11, float d12, float ndc12, float alpha12, float d13, float ndc13, float alpha13) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9, d10, ndc10, alpha10, d11, ndc11, alpha11, d12, ndc12, alpha12);
    vec2 b = nearestDepthLayer(d13, ndc13, alpha13);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float d10, float ndc10, float alpha10, float d11, float ndc11, float alpha11, float d12, float ndc12, float alpha12, float d13, float ndc13, float alpha13, float d14, float ndc14, float alpha14) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9, d10, ndc10, alpha10, d11, ndc11, alpha11, d12, ndc12, alpha12, d13, ndc13, alpha13);
    vec2 b = nearestDepthLayer(d14, ndc14, alpha14);
    return a.x < b.x ? a : b;
}
vec2 nearestDepthLayer(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float d10, float ndc10, float alpha10, float d11, float ndc11, float alpha11, float d12, float ndc12, float alpha12, float d13, float ndc13, float alpha13, float d14, float ndc14, float alpha14, float d15, float ndc15, float alpha15) {
    vec2 a = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9, d10, ndc10, alpha10, d11, ndc11, alpha11, d12, ndc12, alpha12, d13, ndc13, alpha13, d14, ndc14, alpha14);
    vec2 b = nearestDepthLayer(d15, ndc15, alpha15);
    return a.x < b.x ? a : b;
}


// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float d10, float ndc10, float alpha10, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9, d10, ndc10, alpha10);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float d10, float ndc10, float alpha10, float d11, float ndc11, float alpha11, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9, d10, ndc10, alpha10, d11, ndc11, alpha11);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float d10, float ndc10, float alpha10, float d11, float ndc11, float alpha11, float d12, float ndc12, float alpha12, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9, d10, ndc10, alpha10, d11, ndc11, alpha11, d12, ndc12, alpha12);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float d10, float ndc10, float alpha10, float d11, float ndc11, float alpha11, float d12, float ndc12, float alpha12, float d13, float ndc13, float alpha13, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9, d10, ndc10, alpha10, d11, ndc11, alpha11, d12, ndc12, alpha12, d13, ndc13, alpha13);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float d10, float ndc10, float alpha10, float d11, float ndc11, float alpha11, float d12, float ndc12, float alpha12, float d13, float ndc13, float alpha13, float d14, float ndc14, float alpha14, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9, d10, ndc10, alpha10, d11, ndc11, alpha11, d12, ndc12, alpha12, d13, ndc13, alpha13, d14, ndc14, alpha14);
    return best.x < 1e30 ? best.y : fallback;
}
// Computes the NDC depth of the nearest layer whose alpha exceeds 0.001. Returns the fallback value is none qualify.
float compositeDepthLayers(float d0, float ndc0, float alpha0, float d1, float ndc1, float alpha1, float d2, float ndc2, float alpha2, float d3, float ndc3, float alpha3, float d4, float ndc4, float alpha4, float d5, float ndc5, float alpha5, float d6, float ndc6, float alpha6, float d7, float ndc7, float alpha7, float d8, float ndc8, float alpha8, float d9, float ndc9, float alpha9, float d10, float ndc10, float alpha10, float d11, float ndc11, float alpha11, float d12, float ndc12, float alpha12, float d13, float ndc13, float alpha13, float d14, float ndc14, float alpha14, float d15, float ndc15, float alpha15, float fallback) {
    vec2 best = nearestDepthLayer(d0, ndc0, alpha0, d1, ndc1, alpha1, d2, ndc2, alpha2, d3, ndc3, alpha3, d4, ndc4, alpha4, d5, ndc5, alpha5, d6, ndc6, alpha6, d7, ndc7, alpha7, d8, ndc8, alpha8, d9, ndc9, alpha9, d10, ndc10, alpha10, d11, ndc11, alpha11, d12, ndc12, alpha12, d13, ndc13, alpha13, d14, ndc14, alpha14, d15, ndc15, alpha15);
    return best.x < 1e30 ? best.y : fallback;
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
            vec4(r,   0.0),
            vec4(u,   0.0),
            vec4(-f,  0.0),
            vec4(eye, 1.0)
        );
    }

    mat4 fakePerspectiveMatrix(float fovY, float aspect, float near, float far) {
        float f = 1.0 / tan(fovY * 0.5);
        return mat4(
            f / aspect, 0.0, 0.0, 0.0,
            0.0,        f,   0.0, 0.0,
            0.0, 0.0, (      far + near) / (near - far), -1.0,
            0.0, 0.0, (2.0 * far * near) / (near - far),  0.0
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


mat4 computeViewProjMatrix() {
    return getProjMatrix() * getViewMatrix();
}