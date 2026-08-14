
#ifdef MINECRAFT
    #moj_import <engineers-bliss:utils/utils.glsl>
    #moj_import <engineers-bliss:utils/render_3d.glsl>
#endif




#define NOISE_CONTRAST 3.5
#define NOISE_SCALE    5.0
#define NOISE_TWIST   -8.0
#define NOISE_SPEED    0.01
#define NOISE_DENSITY  0.82

/**
 * @param ro          Ray origin, local to the object's center (ImpostorFrame.rayOrigin).
 * @param rd          Normalized ray direction, local space (ImpostorFrame.rayDir).
 * @param innerRadius Inner radius of the shell.
 * @param outerRadius Outer radius of the shell.
 * @param _time       Animation time.
 * @param axisA,axisB,axisN  Orthonormal basis for the disk plane (tangent, bitangent, normal).
 * @param outT        Output: density-weighted average ray distance to the visible cloud
 */
vec4 volumetricEdgeNoise(vec3 ro, vec3 rd, float innerRadius, float outerRadius, float _time, vec3 axisA, vec3 axisB, vec3 axisN, out float outT) {
    outT = 0.0;
    float tNearOuter, tFarOuter;
    if(!intersectSphere(ro, rd, outerRadius, tNearOuter, tFarOuter) || tFarOuter < 0.0) {
        return vec4(0.0);
    }
    tNearOuter = max(tNearOuter, 0.0);
    outT = tNearOuter;

    const int STEPS = 24;
    float dt = (tFarOuter - tNearOuter) / float(STEPS);

    float accumAlpha = 0.0;
    float accumNoise = 0.0;
    float accumWeight = 0.0;
    float accumT = 0.0;

    for(int i = 0; i < STEPS; i++) {
        float t = tNearOuter + (float(i) + 0.5) * dt;
        vec3 p = ro + rd * t;
        if(dot(p, rd) >= 0.0) continue; // Don't render back side
        float r = length(p);

        float shell = (1.0 - smoothstep(innerRadius, outerRadius, r)) * smoothstep(innerRadius * 0.85, innerRadius, r);
        if(shell <= 0.0001) continue;

        vec3 pLocal = vec3(dot(p, axisA), dot(p, axisB), dot(p, axisN));
        float swirlAngle = _time * NOISE_SPEED + (innerRadius / max(r, 1e-4)) * NOISE_TWIST;
        float sA = sin(swirlAngle), cA = cos(swirlAngle);
        vec3 samplePos = vec3(mat2(cA, -sA, sA, cA) * pLocal.xy, pLocal.z);

        float n = fbm3D(samplePos * (NOISE_SCALE   / max(innerRadius, 1e-4)));
        float density = shell * n * (NOISE_DENSITY / max(innerRadius, 1e-4));
        accumAlpha += density * dt;
        accumNoise += n * shell;
        accumWeight += shell;
        accumT += t * shell;
    }

    // Calculate noise average and make colour and alpha super high constrast to boost the 3d effect
    float noiseAvg = accumWeight > 0.0001 ? accumNoise / accumWeight : 0.0;
    noiseAvg   = adjustContrast(noiseAvg,   NOISE_CONTRAST);
    accumAlpha = adjustContrast(accumAlpha, NOISE_CONTRAST);
    if(accumWeight > 0.0001) outT = accumT / accumWeight;
    return vec4(vec3(noiseAvg), clamp(accumAlpha, 0.0, 1.0));
}
#undef NOISE_CONTRAST
#undef NOISE_SCALE
#undef NOISE_TWIST
#undef NOISE_SPEED
#undef NOISE_DENSITY