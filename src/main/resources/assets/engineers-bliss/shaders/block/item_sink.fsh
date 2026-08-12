

#version 150
#define MINECRAFT




#ifdef MINECRAFT
    #moj_import <minecraft:globals.glsl>
    #moj_import <minecraft:projection.glsl>
    #moj_import <minecraft:dynamictransforms.glsl>
    #moj_import <engineers-bliss:utils.glsl>
    #moj_import <engineers-bliss:gravitational_lensing.glsl>
    #moj_import <engineers-bliss:render_3d.glsl>

    in vec4 vertexColor;
    in vec2 uv0;
    in vec3 worldPos;

    uniform sampler2D SceneSampler;
    uniform sampler2D SceneDepthSampler;

    out vec4 fragColor;



    #define sceneColorSampler (SceneSampler)
    #define sceneDepthSampler (SceneDepthSampler)
    #define time (GameTime * 1200.0)
#else
    #define sceneColorSampler (iChannel0)
    #define sceneDepthSampler (iChannel1)
    #define time (iTime)
#endif














float fallingSquares(vec2 uv, float _time, float horizon, float outerRadius) {
    float distance = length(uv);
    float angle = atan(uv.y, uv.x);
    angle += pow(horizon / distance, 6.0) * 3.0;

    float logR = log(distance);

    float angularDensity = 140.0;
    float radialDensity = 60.0;
    float fallSpeed = 0.8;

    vec2 gridUV = vec2(angle * angularDensity / TAU, logR * radialDensity + _time * fallSpeed);

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









#define CORE_RADIUS_FALLOFF   0.95
#define LENSING_SCALE         2.0
#define PHOTON_RING_THICKNESS 0.06


// uv0: coords that go from  0.0 to 1.0
// uv:  coords that go from -0.5 to 0.5

#ifdef MINECRAFT
void main() {
    ImpostorFrame frame = getImpostorFrame(worldPos, uv0);
    float horizon = 0.5; // world scale radius
#else
void mainImage(out vec4 fragColor, in vec2 fragCoord) {
    vec2 uncorrectedUV = fragCoord / iResolution.xy;
    vec2 uv0 = (fragCoord - 0.5 * iResolution.xy) / min(iResolution.x, iResolution.y) + 0.5;
    if(max(abs(uv0.x - 0.5), abs(uv0.y - 0.5)) > 0.5) uv0 = vec2(-999.0, -999.0);

    ImpostorFrame frame = getImpostorFrame(uv0);
    float horizon = texture(iChannel2, vec2(0)).x; // world scale radius, controlled with keys
#endif




    vec2 uv = uv0 - vec2(0.5);
    gl_FragDepth = gl_FragCoord.z; //FIXME calculate proper depth based on the 3d rendering

    float outerRadius = horizon * 2.0;

    vec3 diskNormal = length(frame.rayOrigin) > 0.0001 ? normalize(frame.rayOrigin) : vec3(0.0, 1.0, 0.0);
    vec3 diskTangent, diskBitangent;
    buildOrthoBasis(diskNormal, diskTangent, diskBitangent);




    float rayLen;
    bool hitPlane = intersectPlane(frame.rayOrigin, frame.rayDir, diskNormal, rayLen);
    vec2 diskUV; //TODO actually use this
    if(hitPlane) {
        vec3 planePosLocal = frame.rayOrigin + frame.rayDir * rayLen;
        diskUV = planeUV(planePosLocal, diskTangent, diskBitangent, 1.0);
    }
    vec2 lensedScreenUV = calculate_lensed_screen_uv(sceneDepthSampler, frame.center, horizon, horizon * LENSING_SCALE, 0.35);




    //float coreRadius = impostorApparentRadius(frame.rayOrigin, horizon, frame.quadExtent);
    //bool hitCore = length(uv) < coreRadius;
    #ifdef MINECRAFT
        float coreRadius = impostorApparentRadius(frame.rayOrigin, horizon, frame.quadExtent);
    #else
        //
        float camDist = length(frame.rayOrigin);
        float angularRadius = asin(clamp(horizon / camDist, 0.0, 1.0));
        float halfFovY = radians(50.0) * 0.5;
        float coreRadius = 0.5 * tan(angularRadius) / tan(halfFovY);
        //TODO merge with photon ring logic
    #endif



    vec4 objectFgColor = vec4(0.0);
    vec4 objectBgColor = vec4(0.0);


    // Compute core mask and color
    float coreMask = smoothstep(coreRadius, coreRadius - coreRadius * (1.0 - CORE_RADIUS_FALLOFF), length(uv));
    objectFgColor = over(vec4(vec3(0.0), coreMask), objectFgColor);


    if(hitPlane) {
        //vec3 planePosLocal = frame.rayOrigin + frame.rayDir * tPlane;
        //gl_FragDepth = impostorNdcDepth(frame, planePosLocal);

        //float distance = length(diskUV);
        //float angle = atan(diskUV.y, diskUV.x);

        //float spin = horizon / max(distance, 1e-4);
        //float swirledAngle = angle + spin * 1.5 + time * 0.06;
        //vec2 swirlPos = vec2(cos(swirledAngle), sin(swirledAngle)) * distance;

        //vec2 driftPos = vec2(cos(angle), sin(angle)) * distance;
        //float turbulence = fbm(swirlPos * 24.0 + driftPos * 12.0);

        //float lensedUvDistance = length(lensedUV);
        //float photonRing = exp(-pow((distance - horizon) * (8.0 / horizon), 2.0));

        //float diskMask = 1.0 - smoothstep(horizon, outerRadius, distance);
        //diskMask = pow(diskMask, 3.0);
        //vec3 diskColor = mix(vec3(0.2, 0.1, 0.05), vec3(1.0, 0.15, 0.15), turbulence);
        //diskColor = mix(diskColor, vec3(1.0, 0.95, 0.85), turbulence * turbulence * 0.4);

        //float squares = fallingSquares(diskUV, time, horizon, outerRadius) * 0.5 * diskMask;

        // color = diskColor * diskMask; //TODO
        // color = mix(color, vec3(0.0), squares); //TODO
        // objectBgColor = vec4(vec3(1.0, 0.95, 0.9), photonRing); //TODO

        //alpha = max(squares, max(photonRing, diskMask));
    }

    objectBgColor = vec4(intersectSphereGradient(frame.rayOrigin, frame.rayDir, uv, frame.quadExtent, horizon, horizon + PHOTON_RING_THICKNESS, 2.0, true));

    vec4 bgColor = over(objectBgColor, texture(sceneColorSampler, lensedScreenUV));
    vec4 fgColor = objectFgColor; //TODO move some logic to this
    fragColor = over(fgColor, bgColor);

    #ifndef MINECRAFT
        if(uv0.x < -1.0) fragColor = vec4(uv0, 0.0, 1.0);
    #endif
    fragColor = objectBgColor;
}
