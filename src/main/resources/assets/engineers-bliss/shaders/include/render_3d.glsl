













#ifdef MINECRAFT
    #moj_import <minecraft:globals.glsl>
    #moj_import <minecraft:projection.glsl>
    #moj_import <minecraft:dynamictransforms.glsl>
#endif




struct ImpostorFrame {
    vec3 center;
    vec3 rayOrigin;
    vec3 rayDir;
    vec3 uAxis;
    vec3 vAxis;
    vec3 normal;
    float quadExtent;
};





#ifdef MINECRAFT
    ImpostorFrame getImpostorFrame(vec3 worldPos, vec2 uv) {
        vec3 dPdx = dFdx(worldPos);
        vec3 dPdy = dFdy(worldPos);
        float dUdx = dFdx(uv.x), dUdy = dFdy(uv.x);
        float dVdx = dFdx(uv.y), dVdy = dFdy(uv.y);

        float det = dUdx * dVdy - dVdx * dUdy;
        det = abs(det) < 1e-8 ? 1e-8 : det;

        vec3 uAxis = (dVdy * dPdx - dVdx * dPdy) / det;
        vec3 vAxis = (dUdx * dPdy - dUdy * dPdx) / det;

        vec3 center = worldPos - (uv.x - 0.5) * uAxis - (uv.y - 0.5) * vAxis;
        vec3 localPos = worldPos - center;

        mat4 invMV = inverse(getViewMatrix());
        vec3 camWorld = (invMV * vec4(0.0, 0.0, 0.0, 1.0)).xyz;
        vec3 camLocal = camWorld - center;

        ImpostorFrame f;
        f.center = center;
        f.rayOrigin = camLocal;
        f.rayDir = normalize(localPos - camLocal);
        f.uAxis = uAxis;
        f.vAxis = vAxis;
        f.normal = normalize(cross(uAxis, vAxis));
        f.quadExtent = 0.5 * min(length(uAxis), length(vAxis));
        return f;
    }
#else
    ImpostorFrame getImpostorFrame(vec2 uv) {
        mat4 view = getViewMatrix();
        mat4 invView = inverse(view);

        mat4 proj = fakePerspectiveMatrix(radians(50.0), 1.0, 0.05, 100.0);
        mat4 invProj = inverse(proj);

        vec2 ndc = uv * 2.0 - 1.0;
        vec4 viewDir = invProj * vec4(ndc, -1.0, 1.0);
        viewDir /= viewDir.w;

        vec3 rayDirWorld = normalize((invView * vec4(normalize(viewDir.xyz), 0.0)).xyz);
        vec3 camWorldPos = (invView * vec4(0.0, 0.0, 0.0, 1.0)).xyz;

        ImpostorFrame f;
        f.center = vec3(0.0);        // the object lives at the world origin
        f.rayOrigin = camWorldPos;    // already "local" since center = 0
        f.rayDir = rayDirWorld;
        f.uAxis = (invView * vec4(1.0, 0.0, 0.0, 0.0)).xyz;
        f.vAxis = (invView * vec4(0.0, 1.0, 0.0, 0.0)).xyz;
        f.normal = normalize(camWorldPos);
        f.quadExtent = 1.0;
        return f;
    }
#endif




#ifdef MINECRAFT
    float impostorNdcDepth(ImpostorFrame f, vec3 hitPosLocal) {
        vec4 clipHit = getProjMatrix() * getViewMatrix() * vec4(hitPosLocal + f.center, 1.0);
        float ndcDepth = clipHit.z / clipHit.w;
        return ndcDepth * 0.5 + 0.5;
    }
#else
    float impostorNdcDepth(ImpostorFrame f, vec3 hitPosLocal) {
        return 2.0;
    }
#endif




vec2 intersectBox(vec3 ro, vec3 rd, vec3 boxMin, vec3 boxMax, out vec3 tminOut) {
    vec3 invDir = 1.0 / rd;
    vec3 t0 = (boxMin - ro) * invDir;
    vec3 t1 = (boxMax - ro) * invDir;
    vec3 tmin = min(t0, t1);
    vec3 tmax = max(t0, t1);
    tminOut = tmin;
    float tNear = max(max(tmin.x, tmin.y), tmin.z);
    float tFar  = min(min(tmax.x, tmax.y), tmax.z);
    return vec2(tNear, tFar);
}




bool intersectSphere(vec3 ro, vec3 rd, float radius, out float tNear, out float tFar) {
    float b = dot(ro, rd);
    float c = dot(ro, ro) - pow(radius, 2.0);
    float h = b * b - c;
    if(h < 0.0) {
        return false;
    }
    h = sqrt(h);
    tNear = -b - h;
    tFar = -b + h;
    return true;
}




float intersectSphereGradient(vec3 ro, vec3 rd, vec2 uv, float quadExtent, float startRadius, float endRadius, float density, bool fov) {
    if (fov) {
        float d = length(ro);
        float r;

        #ifdef MINECRAFT
            float P = length(uv) * 2.0 * quadExtent;
            r = (P * d) / sqrt(d * d + P * P);
        #else
            float tanHalfFovY = 1.0 / getProjMatrix()[1][1];
            float theta = atan(length(uv) * 2.0 * tanHalfFovY);
            r = d * sin(theta);
        #endif

        float outerHalf = sqrt(max(endRadius * endRadius - r * r, 0.0));
        float pathLength;
        if (r < startRadius) {
            float innerHalf = sqrt(max(startRadius * startRadius - r * r, 0.0));
            pathLength = 2.0 * (outerHalf - innerHalf);
        } else if (r < endRadius) {
            pathLength = 2.0 * outerHalf;
        } else {
            pathLength = 0.0;
        }
        return 1.0 - exp(-pathLength * density);
    }

    float tNearOuter, tFarOuter;
    if(!intersectSphere(ro, rd, endRadius, tNearOuter, tFarOuter) || tFarOuter < 0.0) {
        return 0.0;
    }
    tNearOuter = max(tNearOuter, 0.0);

    float tNearInner, tFarInner;
    bool hitInner = intersectSphere(ro, rd, startRadius, tNearInner, tFarInner) && tFarInner >= 0.0;

    float pathLength;
    if(hitInner) {
        tNearInner = max(tNearInner, 0.0);
        pathLength = (tNearInner - tNearOuter) + (tFarOuter - tFarInner);
    }
    else {
        pathLength = tFarOuter - tNearOuter;
    }

    return 1.0 - exp(-pathLength * density);
}








#define NOISE_CONTRAST 3.5
#define NOISE_SCALE    5.0
#define NOISE_TWIST    -4.5
#define NOISE_SPEED    0.01
#define NOISE_DENSITY  2.0


/**
 * @param ro          Ray origin, local to the object's center (ImpostorFrame.rayOrigin).
 * @param rd          Normalized ray direction, local space (ImpostorFrame.rayDir).
 * @param innerRadius Inner radius of the shell.
 * @param outerRadius Outer radius of the shell.
 * @param _time       Animation time.
 * @param axisA,axisB,axisN  Orthonormal basis for the disk plane (tangent, bitangent, normal).
 */
vec4 volumetricEdgeNoise(vec3 ro, vec3 rd, float innerRadius, float outerRadius, float _time, vec3 axisA, vec3 axisB, vec3 axisN) {
    float tNearOuter, tFarOuter;
    if (!intersectSphere(ro, rd, outerRadius, tNearOuter, tFarOuter) || tFarOuter < 0.0) {
        return vec4(0.0);
    }
    tNearOuter = max(tNearOuter, 0.0);



    const int STEPS = 24;
    float dt = (tFarOuter - tNearOuter) / float(STEPS);

    float accumAlpha = 0.0;
    float accumNoise = 0.0;
    float accumWeight = 0.0;

    for (int i = 0; i < STEPS; i++) {
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

        float n = fbm3D(samplePos * (NOISE_SCALE / innerRadius));

        float density = shell * n * NOISE_DENSITY;
        accumAlpha += density * dt;
        accumNoise += n * shell;
        accumWeight += shell;
    }

    // Calculate noise average and make colour and alpha super high constrast to boost the 3d effect
    float noiseAvg = accumWeight > 0.0001 ? accumNoise / accumWeight : 0.0;
    noiseAvg   = adjustContrast(noiseAvg,   NOISE_CONTRAST);
    accumAlpha = adjustContrast(accumAlpha, NOISE_CONTRAST);
    return vec4(vec3(noiseAvg), clamp(accumAlpha, 0.0, 1.0));
}
#undef NOISE_CONTRAST
#undef NOISE_SCALE
#undef NOISE_TWIST
#undef NOISE_SPEED
#undef NOISE_DENSITY







#define RING_NOISE_SCALE  0.8
#define RING_NOISE_SPEED  0.1
#define RING_NOISE_AMOUNT 1.2
#define RING_STEPS        16
#define RING_DENSITY      16.0
#define RING_INNER_FALLOFF 0.01
#define RING_OUTER_FALLOFF 0.02

/**
 * @param ro        Ray origin, local to the object's center (ImpostorFrame.rayOrigin).
 * @param rd        Normalized ray direction, local space (ImpostorFrame.rayDir).
 * @param radius    Inner radius of the ring, world units (roughly the event horizon).
 * @param thickness Average radial thickness of the ring before noise modulation.
 * @param _time     Animation time.
 */
float volumetricPhotonRing(vec3 ro, vec3 rd, float radius, float thickness, float _time) {
    float b = dot(ro, rd);
    vec3 impact = ro - b * rd;
    float impactLen = length(impact);
    if (impactLen < 1e-5) {
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







#define CUBE_RADIAL_DENSITY   20.0
#define CUBE_FACE_DENSITY     25.0
#define CUBE_FALL_SPEED       0.1
#define CUBE_DENSITY_THRESH   0.15
#define CUBE_SIZE_MIN         0.05
#define CUBE_SIZE_MAX         0.25
#define CUBE_PLANE_THICKNESS  0.1

vec4 fallingCubes3D(vec3 ro, vec3 rd, float horizon, float outerRadius, float _time, vec3 axisA, vec3 axisB, vec3 axisN) {
    float tNearOuter, tFarOuter;
    if(!intersectSphere(ro, rd, outerRadius, tNearOuter, tFarOuter) || tFarOuter < 0.0) {
        return vec4(0.0);
    }
    tNearOuter = max(tNearOuter, 0.0);

    const int STEPS = 128;
    float dt = (tFarOuter - tNearOuter) / float(STEPS);

    float alpha = 0.0;
    float shade = 0.0;

    for(int i = 0; i < STEPS; i++) {
        float t = tNearOuter + (float(i) + 0.5) * dt;
        vec3 p = ro + rd * t;
        float r = length(p);
        if(r < 1e-4) continue;

        float fade = smoothstep(horizon * 0.9, horizon * 1.5, r) * (1.0 - smoothstep(outerRadius * 0.9, outerRadius, r));
        if(fade <= 0.0001) continue;

        vec3 d = vec3(dot(p, axisA), dot(p, axisB), dot(p, axisN)) / r;

        vec3 ad = abs(d);
        float face, faceScale;
        vec2 uv;
        if(ad.x >= ad.y && ad.x >= ad.z) {
            faceScale = ad.x; uv = vec2(d.y, d.z); face = d.x > 0.0 ? 0.0 : 1.0;
        } else if(ad.y >= ad.z) {
            faceScale = ad.y; uv = vec2(d.x, d.z); face = d.y > 0.0 ? 2.0 : 3.0;
        } else {
            faceScale = ad.z; uv = vec2(d.x, d.y); face = d.z > 0.0 ? 4.0 : 5.0;
        }
        uv /= faceScale;

        float logR = log(r);

        vec3 gridUV = vec3(
            uv.x * CUBE_FACE_DENSITY * 0.5,
            logR * CUBE_RADIAL_DENSITY + _time * CUBE_FALL_SPEED,
            uv.y * CUBE_FACE_DENSITY * 0.5
        );

        vec3 cellId = floor(gridUV);
        cellId.x += face * 4096.0;
        vec3 cellUV = fract(gridUV) - 0.5;

        float rnd = hash31(cellId);
        if (rnd > CUBE_DENSITY_THRESH) continue;

        vec3 offset = vec3(
            hash31(cellId + vec3(1.0, 0.0, 0.0)),
            hash31(cellId + vec3(2.0, 0.0, 0.0)),
            hash31(cellId + vec3(3.0, 0.0, 0.0))
        ) * 0.6 - 0.3;
        float size = mix(CUBE_SIZE_MIN, CUBE_SIZE_MAX, hash31(cellId + vec3(4.0, 0.0, 0.0)));

        vec3 pCell = cellUV - offset;

        vec3 halfExtents = vec3(size, CUBE_PLANE_THICKNESS, size);
        vec3 inBox = step(abs(pCell), halfExtents);
        float plane = inBox.x * inBox.y * inBox.z;

        float contribution = plane * fade;

        alpha = max(alpha, contribution);
        shade = max(shade, mix(0.5, 1.0, hash31(cellId + vec3(7.0, 0.0, 0.0))) * contribution);
    }

    return vec4(vec3(shade), alpha);
}
#undef CUBE_RADIAL_DENSITY
#undef CUBE_FACE_DENSITY
#undef CUBE_FALL_SPEED
#undef CUBE_DENSITY_THRESH
#undef CUBE_SIZE_MIN
#undef CUBE_SIZE_MAX
#undef CUBE_PLANE_THICKNESS








bool intersectPlane(vec3 ro, vec3 rd, vec3 normal, out float rayLen) {
    float denom = dot(rd, normal);
    if(abs(denom) < 1e-6) {
        return false;
    }
    rayLen = -dot(ro, normal) / denom;
    return rayLen >= 0.0;
}




vec2 planeLocalUV(vec3 hitPosLocal, vec3 uAxis, vec3 vAxis) {
    return vec2(dot(hitPosLocal, uAxis) / dot(uAxis, uAxis), dot(hitPosLocal, vAxis) / dot(vAxis, vAxis));
}

vec3 boxNormal(vec3 rayDir, vec3 tmin) {
    vec3 mask = step(tmin.yzx, tmin.xyz) * step(tmin.zxy, tmin.xyz);
    return -sign(rayDir) * mask;
}

vec3 sphereNormal(vec3 hitPosLocal) {
    return normalize(hitPosLocal);
}

float impostorApparentRadius(vec3 rayOrigin, float objectRadius, float quadExtent) {
    float d = max(length(rayOrigin), objectRadius * 1.001);
    float projected = objectRadius * d / sqrt(max(d * d - objectRadius * objectRadius, 1e-6));
    return projected / (2.0 * quadExtent);
}

void buildOrthoBasis(vec3 normal, out vec3 tangent, out vec3 bitangent) {
    vec3 up = abs(normal.y) < 0.99 ? vec3(0.0, 1.0, 0.0) : vec3(1.0, 0.0, 0.0);
    tangent = normalize(cross(up, normal));
    bitangent = cross(normal, tangent);
}

vec2 planeUV(vec3 hitPosLocal, vec3 tangent, vec3 bitangent, float scale) {
    return vec2(dot(hitPosLocal, tangent), dot(hitPosLocal, bitangent)) * scale;
}

