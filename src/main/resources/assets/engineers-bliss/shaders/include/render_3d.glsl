

#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:dynamictransforms.glsl>







struct ImpostorFrame {
    vec3 center;
    vec3 rayOrigin;
    vec3 rayDir;
    vec3 uAxis;
    vec3 vAxis;
    vec3 normal;
    // vec3 camRight;
    // vec3 camUp;
    float quadExtent;
};




ImpostorFrame getImpostorFrame(vec3 worldPos, vec2 uv0) {
    vec3 dPdx = dFdx(worldPos);
    vec3 dPdy = dFdy(worldPos);
    float dUdx = dFdx(uv0.x), dUdy = dFdy(uv0.x);
    float dVdx = dFdx(uv0.y), dVdy = dFdy(uv0.y);

    float det = dUdx * dVdy - dVdx * dUdy;
    det = abs(det) < 1e-8 ? 1e-8 : det;

    vec3 uAxis = (dVdy * dPdx - dVdx * dPdy) / det;
    vec3 vAxis = (dUdx * dPdy - dUdy * dPdx) / det;

    vec3 center = worldPos - (uv0.x - 0.5) * uAxis - (uv0.y - 0.5) * vAxis;
    vec3 localPos = worldPos - center;

    mat4 invMV = inverse(ModelViewMat);
    vec3 camWorld = (invMV * vec4(0.0, 0.0, 0.0, 1.0)).xyz;
    vec3 camLocal = camWorld - center;

    ImpostorFrame f;
    f.center = center;
    f.rayOrigin = camLocal;
    f.rayDir = normalize(localPos - camLocal);
    f.uAxis = uAxis;
    f.vAxis = vAxis;
    f.normal = normalize(cross(uAxis, vAxis));
    // f.camRight = normalize((invMV * vec4(1.0, 0.0, 0.0, 0.0)).xyz);
    // f.camUp    = normalize((invMV * vec4(0.0, 1.0, 0.0, 0.0)).xyz);
    f.quadExtent = 0.5 * min(length(uAxis), length(vAxis));
    return f;
}




float impostorNdcDepth(ImpostorFrame f, vec3 hitPosLocal) {
    vec4 clipHit = ProjMat * ModelViewMat * vec4(hitPosLocal + f.center, 1.0);
    float ndcDepth = clipHit.z / clipHit.w;
    return ndcDepth * 0.5 + 0.5;
}




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
    float c = dot(ro, ro) - radius * radius;
    float h = b * b - c;
    if(h < 0.0) {
        return false;
    }
    h = sqrt(h);
    tNear = -b - h;
    tFar = -b + h;
    return true;
}




bool intersectPlane(vec3 ro, vec3 rd, vec3 normal, out float t) {
    float denom = dot(rd, normal);
    if(abs(denom) < 1e-6) {
        return false;
    }
    t = -dot(ro, normal) / denom;
    return t >= 0.0;
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