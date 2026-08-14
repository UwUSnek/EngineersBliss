
#ifdef MINECRAFT
    #moj_import <engineers-bliss:utils/utils.glsl>
    #moj_import <engineers-bliss:utils/render_3d.glsl>
#endif




#define SQUARE_RADIAL_DENSITY   20.0
#define SQUARE_FACE_DENSITY     25.0
#define SQUARE_FALL_SPEED       0.05
#define SQUARE_DENSITY_THRESH   0.5
#define SQUARE_SIZE_MIN         0.05
#define SQUARE_SIZE_MAX         0.25
#define SQUARE_PLANE_THICKNESS  0.1
#define SQUARE_ANGULAR_SUBSTEPS 4   //! 4 kinda works and looks kinda consistent. Higher quality makes the shader too laggy
#define SQUARE_MAX_SHELLS       256

void testSquareCell(vec3 ro, vec3 rd, float t, float horizon, float outerRadius, vec3 axisA, vec3 axisB, vec3 axisN, float _time, inout float alpha, inout float shade, inout float outT, inout bool foundHit) {
    vec3 p = ro + rd * t;
    float r = length(p);
    if(r < 1e-4) return;

    float fade = smoothstep(horizon * 0.9, horizon * 1.5, r) * (1.0 - smoothstep(outerRadius * 0.9, outerRadius, r));
    if(fade <= 0.0001) return;

    vec3 d = vec3(dot(p, axisA), dot(p, axisB), dot(p, axisN)) / r;
    vec3 ad = abs(d);
    float face, faceScale;
    vec2 uv;
    if(ad.x >= ad.y && ad.x >= ad.z) {
        faceScale = ad.x; uv = vec2(d.y, d.z);
        face = d.x > 0.0 ? 0.0 : 1.0;
    }
    else if(ad.y >= ad.z) {
        faceScale = ad.y; uv = vec2(d.x, d.z);
        face = d.y > 0.0 ? 2.0 : 3.0;
    }
    else {
        faceScale = ad.z; uv = vec2(d.x, d.y);
        face = d.z > 0.0 ? 4.0 : 5.0;
    }

    uv /= faceScale;
    // Equi angular correction to counteract gnomonic stretching
    uv = atan(uv) / atan(1.0);

    float logR = log(r);
    vec3 gridUV = vec3(
        uv.x * SQUARE_FACE_DENSITY * 0.5,
        logR * SQUARE_RADIAL_DENSITY + _time * SQUARE_FALL_SPEED,
        uv.y * SQUARE_FACE_DENSITY * 0.5
    );

    vec3 cellId = floor(gridUV);
    cellId.x += face * 4096.0;
    vec3 cellUV = fract(gridUV) - 0.5;

    float rnd = hash31(cellId);
    if(rnd > SQUARE_DENSITY_THRESH) return;

    vec3 offset = vec3(
        hash31(cellId + vec3(10.0, 0.0, 0.0)),
        hash31(cellId + vec3(20.0, 0.0, 0.0)),
        hash31(cellId + vec3(30.0, 0.0, 0.0))
    ) * 0.6 - 0.3;
    float size = mix(SQUARE_SIZE_MIN, SQUARE_SIZE_MAX, hash31(cellId + vec3(4.0, 0.0, 0.0)));

    vec3 pCell = cellUV - offset;
    vec3 halfExtents = vec3(size, SQUARE_PLANE_THICKNESS, size);
    vec3 inBox = step(abs(pCell), halfExtents);
    float plane = inBox.x * inBox.y * inBox.z;

    float contribution = plane * fade;
    if(contribution > 0.0001) {
        outT = foundHit ? min(outT, t) : t;
        foundHit = true;
    }
    alpha = max(alpha, mix(0.5, 1.0, hash31(cellId + vec3(1.0, 0.0, 0.0))) * contribution);
    shade = max(shade, mix(0.0, 0.2, hash31(cellId + vec3(7.0, 0.0, 0.0))) * contribution);
}




vec4 fallingSquares(vec3 ro, vec3 rd, float horizon, float outerRadius, float _time, vec3 axisA, vec3 axisB, vec3 axisN, out float outT) {
    outT = 1e6;
    float tNearOuter, tFarOuter;
    if(!intersectSphere(ro, rd, outerRadius, tNearOuter, tFarOuter) || tFarOuter < 0.0) {
        return vec4(0.0);
    }
    tNearOuter = max(tNearOuter, 0.0);

    float alpha = 0.0;
    float shade = 0.0;
    bool foundHit = false;

    float tCA = clamp(-dot(ro, rd), tNearOuter, tFarOuter);
    float segStarts[2] = float[2](tNearOuter, tCA);
    float segEnds[2]   = float[2](tCA, tFarOuter);

    for(int s = 0; s < 2; s++) {
        float ta = segStarts[s], tb = segEnds[s];
        if(tb - ta < 1e-5) continue;

        float rA = length(ro + rd * ta);
        float rB = length(ro + rd * tb);
        float gA = log(max(rA, 1e-4)) * SQUARE_RADIAL_DENSITY + _time * SQUARE_FALL_SPEED;
        float gB = log(max(rB, 1e-4)) * SQUARE_RADIAL_DENSITY + _time * SQUARE_FALL_SPEED;

        int nStart = int(floor(min(gA, gB)));
        int nEnd   = min(int(floor(max(gA, gB))), nStart + SQUARE_MAX_SHELLS);

        for(int n = nStart; n <= nEnd; n++) {
            float tLo = mix(ta, tb, clamp((float(n)       - gA) / (gB - gA), 0.0, 1.0));
            float tHi = mix(ta, tb, clamp((float(n) + 1.0 - gA) / (gB - gA), 0.0, 1.0));
            if(tHi < tLo) { float tmp = tLo; tLo = tHi; tHi = tmp; }

            for(int a = 0; a < SQUARE_ANGULAR_SUBSTEPS; a++) {
                float t = mix(tLo, tHi, (float(a) + 0.5) / float(SQUARE_ANGULAR_SUBSTEPS));
                testSquareCell(ro, rd, t, horizon, outerRadius, axisA, axisB, axisN, _time, alpha, shade, outT, foundHit);
            }
        }
    }

    return vec4(vec3(shade), alpha);
}
#undef SQUARE_RADIAL_DENSITY
#undef SQUARE_FACE_DENSITY
#undef SQUARE_FALL_SPEED
#undef SQUARE_DENSITY_THRESH
#undef SQUARE_SIZE_MIN
#undef SQUARE_SIZE_MAX
#undef SQUARE_PLANE_THICKNESS
#undef SQUARE_ANGULAR_SUBSTEPS
#undef SQUARE_MAX_SHELLS