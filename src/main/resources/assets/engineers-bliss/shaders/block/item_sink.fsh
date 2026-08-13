

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
    #define time (GameTime * 12000.0)
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
#define LENSING_SCALE         4.0
#define PHOTON_RING_SCALE     1.05
#define DISK_OUTER_SCALE      2.4
#define CUBE_AREA_SCALE       1.4


// uv0: coords that go from  0.0 to 1.0
// uv:  coords that go from -0.5 to 0.5

#ifdef MINECRAFT
void main() {
    ImpostorFrame frame = getImpostorFrame(worldPos, uv0);
    float horizon = 0.4; // world scale radius. horizon * LENSING_SCALE must be < quad size
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



    // Calculate lensed screen UVs, then use them to poll the distorted scene background
    float lensingBoundary = horizon * PHOTON_RING_SCALE;
    vec2 lensedScreenUV = calculate_lensed_screen_uv(sceneDepthSampler, frame.center, lensingBoundary, lensingBoundary * LENSING_SCALE, 0.35);
    vec4 lensedSceneColor = texture(sceneColorSampler, lensedScreenUV);





    // Compute core mask and color
    float bCore = dot(frame.rayOrigin, frame.rayDir);
    float impactCore = sqrt(max(dot(frame.rayOrigin, frame.rayOrigin) - bCore * bCore, 0.0));
    float coreMask = bCore < 0.0 ? smoothstep(horizon, horizon - horizon * (1.0 - CORE_RADIUS_FALLOFF), impactCore) : 0.0;
    vec4 coreColor = vec4(vec3(0.0), coreMask);


    //if(hitPlane) {
        //vec3 planePosLocal = frame.rayOrigin + frame.rayDir * tPlane;
        //gl_FragDepth = impostorNdcDepth(frame, planePosLocal);

        // Calculate disk coord data
        vec3 diskNormal = vec3(0.0, 1.0, 0.0);
        vec3 diskTangent, diskBitangent;
        buildOrthoBasis(diskNormal, diskTangent, diskBitangent);

        // Compute disk mask and color
        vec4 edgeNoise = volumetricEdgeNoise(frame.rayOrigin, frame.rayDir, horizon, horizon * DISK_OUTER_SCALE, time, diskTangent, diskBitangent, diskNormal);
        float turbulence = edgeNoise.r;
        float diskMask = edgeNoise.a;
        vec4 diskColor = vec4(mix(vec3(1.0, 0.2, 0.05) * 0.8, vec3(1.0, 0.15, 0.15) * 0.1, pow(turbulence, 1.0)), diskMask);

        //float squares = fallingSquares(diskUV, time, horizon, outerRadius) * 0.5 * diskMask;

        // color = diskColor * diskMask; //TODO
        // color = mix(color, vec3(0.0), squares); //TODO
        // objectBgColor = vec4(vec3(1.0, 0.95, 0.9), photonRing); //TODO

        //alpha = max(squares, max(photonRing, diskMask));
    //}

    //float photonRingMask = intersectSphereGradient(frame.rayOrigin, frame.rayDir, uv, frame.quadExtent, photonRingStart, photonRingEnd, 1.0, false);
    //vec4 photonRingColor = vec4(vec3(1.0), photonRingMask * 4.0); // Flipped source color and contrast

    // Compute photon ring mask and color
    float photonRingMask = volumetricPhotonRing(frame.rayOrigin, frame.rayDir, horizon, horizon * PHOTON_RING_SCALE - horizon, time);
    vec4 photonRingColor = vec4(vec3(1.0, (photonRingMask * 1.1) * vec2(0.9, 0.8)), photonRingMask);


    // Compute falling cubes mask and color
    vec4 cubesMask = fallingCubes3D(frame.rayOrigin, frame.rayDir, horizon, horizon * CUBE_AREA_SCALE, time, diskTangent, diskBitangent, diskNormal);
    vec4 cubesColor = vec4(vec3(0.0), cubesMask.a * 0.8);




    // Composite layers and output the final image
    vec4 bgColor =
        over(cubesColor,
        over(photonRingColor,
        over(diskColor,
        over(lensedSceneColor,
        vec4(0.0)
    ))));
    vec4 fgColor =
        over(coreColor,
        vec4(0.0)
    );
    fragColor = over(fgColor, bgColor);
    #ifndef MINECRAFT
        if(uv0.x < -1.0) fragColor = vec4(uv0, 0.0, 1.0);
    #endif
}
