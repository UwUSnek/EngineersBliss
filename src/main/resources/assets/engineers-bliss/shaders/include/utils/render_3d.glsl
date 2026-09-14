
#ifdef MINECRAFT
    #moj_import <minecraft:globals.glsl>
    #moj_import <minecraft:projection.glsl>
    #moj_import <minecraft:dynamictransforms.glsl>
    #moj_import <engineers-bliss:utils/utils.glsl>
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

    //! Plane size is provided by the java code. This creates more consistent results than trying to calculate it.
    layout(std140) uniform PlaneSizeData {
        float PlaneSize;
    };

    ImpostorFrame getImpostorFrame(vec3 worldPos, vec2 uv) {
        mat4 invMV = inverse(getViewMatrix());
        vec3 camWorld = (invMV * vec4(0.0, 0.0, 0.0, 1.0)).xyz;

        vec3 uAxis = invMV[0].xyz * PlaneSize;  // Camera right
        vec3 vAxis = invMV[1].xyz * PlaneSize;  // Camera up

        vec3 center = worldPos - (uv.x - 0.5) * uAxis - (uv.y - 0.5) * vAxis;
        vec3 localPos = worldPos - center;
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
        f.rayOrigin = camWorldPos;    // already local since center = 0
        f.rayDir = rayDirWorld;
        f.uAxis = (invView * vec4(1.0, 0.0, 0.0, 0.0)).xyz;
        f.vAxis = (invView * vec4(0.0, 1.0, 0.0, 0.0)).xyz;
        f.normal = normalize(camWorldPos);
        f.quadExtent = 1.0;
        return f;
    }
#endif








#ifdef MINECRAFT
    /**
     * Computes the depth of the point at the specified local-space hit position.
     * @param frame ImpostorFrame data.
     * @param viewProj The view projection matrix.
     * @param hitPosLocal The hit position, local to the frame's center.
     * @return The computed depth [0-1].
     */
    float impostorNdcDepth(ImpostorFrame frame, mat4 viewProj, vec3 hitPosLocal) {
        vec4 clipHit = viewProj * vec4(hitPosLocal + frame.center, 1.0);
        float ndcDepth = clipHit.z / clipHit.w;
        return ndcDepth * 0.5 + 0.5;
    }
    /**
     * Computes the depth of the point at the specified local-space hit position.
     * If available, the view projection matrix can be provided to improve performance.
     * @param frame ImpostorFrame data.
     * @param hitPosLocal The hit position, local to the frame's center.
     * @return The computed depth [0-1].
     */
    float impostorNdcDepth(ImpostorFrame frame, vec3 hitPosLocal) {
        return impostorNdcDepth(frame, computeViewProjMatrix(), hitPosLocal);
    }
#else
    float impostorNdcDepth(ImpostorFrame frame, vec3 hitPosLocal) {
        return 2.0;
    }
    float impostorNdcDepth(ImpostorFrame frame, mat4 viewProj, vec3 hitPosLocal) {
        return 2.0;
    }
#endif









#ifdef MINECRAFT
    float _internal_sceneOcclusionVisibility(float sceneLinearDepth, float layerLinearDepth, float bias) {
        return smoothstep(layerLinearDepth - bias, layerLinearDepth + bias, sceneLinearDepth);
    }
    /**
     * Compares a layer's reported depth against the scene depth buffer and calculates the occlusion factor.
     * @param ndcDepth The precomputed ndc depth.
     * @param sceneLinearDepth The linear depth of the scene.
     * @param bias Occlusion bias. This controls how sharply the layer fades in and out.
     * @return The occlusion factor [0-1].
     */
    float sceneOcclusionVisibility(float ndcDepth, float sceneLinearDepth, float bias) {
        return _internal_sceneOcclusionVisibility(sceneLinearDepth, linearizeDepth(ndcDepth), bias);
    }
    /**
     * Compares a layer's reported depth against the scene depth buffer and calculates the occlusion factor.
     * If available, the ndc depth can be provided instead of frame, view projection and hit position, to improve performance.
     * @param frame The frame data.
     * @param viewProj The view projection matrix.
     * @param hitPosLocal The hit position, local to the frame's center.
     * @param sceneLinearDepth The linear depth of the scene.
     * @param bias Occlusion bias. This controls how sharply the layer fades in and out.
     * @return The occlusion factor [0-1].
     */
    float sceneOcclusionVisibility(ImpostorFrame frame, mat4 viewProj, vec3 hitPosLocal, float sceneLinearDepth, float bias) {
        return sceneOcclusionVisibility(impostorNdcDepth(frame, viewProj, hitPosLocal), sceneLinearDepth, bias);
    }
#else
    float sceneOcclusionVisibility(float ndcDepth, float sceneLinearDepth, float bias) {
        return 1.0;
    }
    float sceneOcclusionVisibility(ImpostorFrame frame, mat4 viewProj, vec3 hitPosLocal, float sceneLinearDepth, float bias) {
        return 1.0;
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
    if(fov) {
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
        if(r < startRadius) {
            float innerHalf = sqrt(max(startRadius * startRadius - r * r, 0.0));
            pathLength = 2.0 * (outerHalf - innerHalf);
        } else if(r < endRadius) {
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