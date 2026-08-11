#version 150
// #define MINECRAFT

// #ifdef MINECRAFT
    #moj_import <minecraft:globals.glsl>
    #moj_import <minecraft:projection.glsl>
    #moj_import <minecraft:dynamictransforms.glsl>
    #moj_import <engineers-bliss:utils.glsl>
    #moj_import <engineers-bliss:gravitational_lensing.glsl>
    #moj_import <engineers-bliss:render_3d.glsl>

    in vec4 vertexColor;
    in vec2 uv0;
    in vec3 worldPos;
// in vec3 DiskAxis;

    uniform sampler2D SceneSampler;
    uniform sampler2D SceneDepthSampler;

    out vec4 fragColor;
// #endif










// float fallingSquares(vec2 uv, float time, float horizon, float outerRadius) {
//     float distance = length(uv);
//     float angle  = atan(uv.y, uv.x);
//     angle += pow(horizon / distance, 6.0) * 3.0;


//     // log-radius -> constant fall speed
//     float logR = log(distance);

//     float angularDensity = 140.0;
//     float radialDensity  = 60.0;
//     float fallSpeed      = 0.8;

//     vec2 gridUV = vec2(angle * angularDensity / TAU, logR * radialDensity + time * fallSpeed);

//     vec2 cellId = floor(gridUV);
//     vec2 cellUV = fract(gridUV) - 0.5;

//     float rnd = hash21(cellId);
//     if(rnd > 0.35) return 0.0;

//     vec2 offset = vec2(hash21(cellId + 1.0), hash21(cellId + 2.0)) * 0.6 - 0.3;
//     float size  = mix(0.1, 0.5, hash21(cellId + 3.0));
//     float rot   = hash21(cellId + 4.0) * TAU;



//     vec2 p = rotate(cellUV - offset, rot);
//     float square = step(max(abs(p.x), abs(p.y)), size);
//     float fade = smoothstep(horizon * 0.9, horizon * 1.5, distance) * (1.0 - smoothstep(outerRadius * 0.9, outerRadius, distance));
//     return square * fade;
// }








// #ifdef MINECRAFT
// void main() {
// #else
// void mainImage(out vec4 fragColor, in vec2 fragCoord) {
//     vec2 texCoord = fragCoord / iResolution.xy;
// #endif


//     #ifdef MINECRAFT
//         float t = GameTime * 1200.0;
//         vec2 uv = uv0 - vec2(0.5);
//     #else
//         float t = iTime;
//         vec2 uv = fragCoord / iResolution.xy;
//         uv = uv - vec2(0.5);
//         uv.x *= iResolution.x / iResolution.y;
//     #endif




//     float distance = length(uv);
//     float horizon  = 0.2;
//     float horizonFalloff  = 0.9;
//     float outerRadius  = horizon * 2.0;
//     float angle = atan(uv.y, uv.x);


//     // Rotation based on the distance from the center
//     float spin = horizon / distance;
//     float swirledAngle = angle + spin * 1.5 + t * 0.06;
//     vec2 swirlPos = vec2(cos(swirledAngle), sin(swirledAngle)) * distance;

//     // Disk noise texture
//     vec2 driftPos = vec2(cos(angle), sin(angle)) * distance;
//     float turbulence = fbm(swirlPos * 24.0 + driftPos * 12.0);

//     // Event horizon, black core
//     float horizonMask = smoothstep(horizon * horizonFalloff, horizon, distance);

//     // Photon ring
//     float photonRing = exp(-pow((distance - horizon) * (8.0 / horizon), 2.0));

//     // Accretion disk
//     float diskMask = 1.0 - smoothstep(horizon, outerRadius, distance);
//     diskMask = pow(diskMask, 3.0);
//     vec3 diskColor = mix(vec3(0.2, 0.1, 0.05), vec3(1.0, 0.15, 0.15), turbulence);
//     diskColor = mix(diskColor, vec3(1.0, 0.95, 0.85), turbulence * turbulence * 0.4);


//     // Calculate falling squares layer
//     float squares = fallingSquares(uv, t, horizon, outerRadius) * 0.5 * diskMask;




//     vec3 color = diskColor * diskMask;
//     color += vec3(1.0, 0.95, 0.9) * photonRing;
//     color = mix(color, vec3(1.0), squares);
//     color *= horizonMask;




//     vec4 objectColor = vec4(color, max(squares, max(photonRing, max(diskMask, 1.0 - horizonMask))));
//     #ifdef MINECRAFT
//         fragColor = apply_lensing_background(SceneSampler, SceneDepthSampler, objectColor, uv, horizon, 0.35);
//     #else
//         fragColor = objectColor;
//     #endif
// }













float fallingSquares(vec2 uv, float time, float horizon, float outerRadius) {
    float distance = length(uv);
    float angle = atan(uv.y, uv.x);
    angle += pow(horizon / distance, 6.0) * 3.0;

    float logR = log(distance);

    float angularDensity = 140.0;
    float radialDensity = 60.0;
    float fallSpeed = 0.8;

    vec2 gridUV = vec2(angle * angularDensity / TAU, logR * radialDensity + time * fallSpeed);

    vec2 cellId = floor(gridUV);
    vec2 cellUV = fract(gridUV) - 0.5;

    float rnd = hash21(cellId);
    if (rnd > 0.35) {
        return 0.0;
    }

    vec2 offset = vec2(hash21(cellId + 1.0), hash21(cellId + 2.0)) * 0.6 - 0.3;
    float size = mix(0.1, 0.5, hash21(cellId + 3.0));
    float rot = hash21(cellId + 4.0) * TAU;

    vec2 p = rotate(cellUV - offset, rot);
    float square = step(max(abs(p.x), abs(p.y)), size);
    float fade = smoothstep(horizon * 0.9, horizon * 1.5, distance) * (1.0 - smoothstep(outerRadius * 0.9, outerRadius, distance));
    return square * fade;
}

void main() {
    float t = GameTime * 1200.0;

    gl_FragDepth = gl_FragCoord.z;

    ImpostorFrame frame = getImpostorFrame(worldPos, uv0);

    float coreRadius = 0.4;
    float uvScale = 1.0 / (2.0 * frame.quadExtent);
    float horizon = coreRadius * uvScale;
    float outerRadius = horizon * 2.0;

    // // vec3 diskNormal = length(DiskAxis) > 0.0001 ? normalize(DiskAxis) : vec3(0.0, 1.0, 0.0);
    vec3 diskNormal = length(frame.rayOrigin) > 0.0001 ? normalize(frame.rayOrigin) : vec3(0.0, 1.0, 0.0);
    vec3 diskTangent, diskBitangent;
    buildOrthoBasis(diskNormal, diskTangent, diskBitangent);

    float tPlane;
    bool hitPlane = intersectPlane(frame.rayOrigin, frame.rayDir, diskNormal, tPlane);
    vec2 diskUV = vec2(outerRadius * 10.0, 0.0);
    if (hitPlane) {
        vec3 planePosLocal = frame.rayOrigin + frame.rayDir * tPlane;
        diskUV = planeUV(planePosLocal, diskTangent, diskBitangent, uvScale);
    }

    float tNear, tFar;
    bool hitCore = intersectSphere(frame.rayOrigin, frame.rayDir, coreRadius, tNear, tFar) && tFar >= 0.0;

    vec3 color;
    float alpha;

    if (hitCore) {
        float tHit = tNear >= 0.0 ? tNear : tFar;
        vec3 hitPosLocal = frame.rayOrigin + frame.rayDir * tHit;
        gl_FragDepth = impostorNdcDepth(frame, hitPosLocal);

        color = vec3(0.0);
        alpha = 1.0;
    } else if (hitPlane) {
        vec3 planePosLocal = frame.rayOrigin + frame.rayDir * tPlane;
        gl_FragDepth = impostorNdcDepth(frame, planePosLocal);

        float distance = length(diskUV);
        float angle = atan(diskUV.y, diskUV.x);

        float spin = horizon / max(distance, 1e-4);
        float swirledAngle = angle + spin * 1.5 + t * 0.06;
        vec2 swirlPos = vec2(cos(swirledAngle), sin(swirledAngle)) * distance;

        vec2 driftPos = vec2(cos(angle), sin(angle)) * distance;
        float turbulence = fbm(swirlPos * 24.0 + driftPos * 12.0);

        float photonRing = exp(-pow((distance - horizon) * (8.0 / horizon), 2.0));

        float diskMask = 1.0 - smoothstep(horizon, outerRadius, distance);
        diskMask = pow(diskMask, 3.0);
        vec3 diskColor = mix(vec3(0.2, 0.1, 0.05), vec3(1.0, 0.15, 0.15), turbulence);
        diskColor = mix(diskColor, vec3(1.0, 0.95, 0.85), turbulence * turbulence * 0.4);

        float squares = fallingSquares(diskUV, t, horizon, outerRadius) * 0.5 * diskMask;

        color = diskColor * diskMask;
        color += vec3(1.0, 0.95, 0.9) * photonRing;
        color = mix(color, vec3(0.0), squares);

        alpha = max(squares, max(photonRing, diskMask));
    } else {
        color = vec3(0.0);
        alpha = 0.0;
    }

    vec4 objectColor = vec4(color, alpha);
    fragColor = apply_lensing_background(SceneSampler, SceneDepthSampler, objectColor, diskUV, horizon, 0.35);
}