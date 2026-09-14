
#ifdef MINECRAFT
    #moj_import <engineers-bliss:utils/utils.glsl>
    #moj_import <engineers-bliss:utils/render_3d.glsl>
#endif




#ifndef RING_NOISE_SCALE
    #define RING_NOISE_SCALE    0.8
#endif
#ifndef RING_NOISE_SPEED
    #define RING_NOISE_SPEED    0.1
#endif
#ifndef RING_NOISE_AMOUNT
    #define RING_NOISE_AMOUNT   1.2
#endif
#ifndef RING_STEPS
    #define RING_STEPS         16
#endif
#ifndef RING_DENSITY
    #define RING_DENSITY       16.0
#endif
#ifndef RING_INNER_FALLOFF
    #define RING_INNER_FALLOFF  0.01
#endif
#ifndef RING_OUTER_FALLOFF
    #define RING_OUTER_FALLOFF  0.02
#endif

/**
 * @param ro        Ray origin, local to the object's center (ImpostorFrame.rayOrigin).
 * @param rd        Normalized ray direction, local space (ImpostorFrame.rayDir).
 * @param radius    Inner radius of the ring, world units (roughly the event horizon).
 * @param thickness Average radial thickness of the ring before noise modulation.
 * @param _time     Animation time.
 * @param outT      Output: ray distance to the ring's closest-approach point, used for depth.
 */
float volumetricPhotonRing(vec3 ro, vec3 rd, float radius, float thickness, float _time, out float outT) {
    float b = dot(ro, rd);
    outT = max(-b, 0.0);

    vec3 impact = ro - b * rd;
    float impactLen = length(impact);
    if(impactLen < 1e-5) {
        return 0.0;
    }

    mat4 camToWorld = inverse(getViewMatrix());
    vec3 camRight = normalize(camToWorld[0].xyz);
    vec3 camUp    = normalize(camToWorld[1].xyz);
    float angle = atan(dot(impact, camUp), dot(impact, camRight));

    vec3 noiseP = vec3(cos(angle), sin(angle), 0.0) * RING_NOISE_SCALE + vec3(0.0, 0.0, _time * RING_NOISE_SPEED);
    float n = fbm3D(noiseP);
    float thicknessMod = 1.0 + (n - 0.5) * 2.0 * RING_NOISE_AMOUNT;
    float localThickness = max(thickness * thicknessMod, 0.0001);

    float innerR = radius;
    float outerR = radius + localThickness;

    float searchOuter = radius + thickness * (1.0 + RING_NOISE_AMOUNT) + RING_OUTER_FALLOFF * 5.0;
    float tNearOuter, tFarOuter;
    if(!intersectSphere(ro, rd, searchOuter, tNearOuter, tFarOuter) || tFarOuter < 0.0) {
        return 0.0;
    }
    tNearOuter = max(tNearOuter, 0.0);
    float dt = (tFarOuter - tNearOuter) / float(RING_STEPS);
    float accumPath = 0.0;

    for(int i = 0; i < RING_STEPS; i++) {
        float t = tNearOuter + (float(i) + 0.5) * dt;
        float r = length(ro + rd * t);

        float shell;
        if(r < outerR) {
            shell = smoothstep(innerR - RING_INNER_FALLOFF, innerR + RING_INNER_FALLOFF, r);
        }
        else {
            shell = exp(-(r - outerR) / RING_OUTER_FALLOFF);
        }
        accumPath += shell * dt;
    }

    return 1.0 - exp(-accumPath * RING_DENSITY);
}
#undef RING_NOISE_SCALE
#undef RING_NOISE_SPEED
#undef RING_NOISE_AMOUNT
#undef RING_STEPS
#undef RING_DENSITY
#undef RING_INNER_FALLOFF
#undef RING_OUTER_FALLOFF