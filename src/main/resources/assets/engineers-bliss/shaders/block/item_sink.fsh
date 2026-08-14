

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








#define CORE_RADIUS_FALLOFF   0.95
#define LENSING_SCALE         4.0
#define PHOTON_RING_SCALE     1.05
#define DISK_OUTER_SCALE      2.4
#define SQUARE_AREA_SCALE     1.4


// How sharply each layer fades out when real world geometry is in front of it.
#define CORE_DEPTH_BIAS   0.03
#define RING_DEPTH_BIAS   0.03
#define SQUARES_DEPTH_BIAS  0.03
#define DISK_DEPTH_BIAS   0.12


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




    // Poll scene depth and linearize it
    vec2 screenUV = gl_FragCoord.xy / vec2(textureSize(sceneDepthSampler, 0));
    float sceneDepthRaw = texture(sceneDepthSampler, screenUV).x;
    float sceneLinear = linearizeDepth(sceneDepthRaw);


    // Calculate lensed screen UVs, then use them to poll the distorted scene background
    float lensingBoundary = horizon * PHOTON_RING_SCALE;
    vec2 lensedScreenUV = calculate_lensed_screen_uv(frame, sceneDepthSampler, frame.center, lensingBoundary, lensingBoundary * LENSING_SCALE, 0.35);
    vec4 lensedSceneColor = texture(sceneColorSampler, lensedScreenUV);


    // Compute core mask, depth, and color
    float tCoreNear, tCoreFar;
    bool coreHit = intersectSphere(frame.rayOrigin, frame.rayDir, horizon, tCoreNear, tCoreFar) && tCoreFar >= 0.0;
    float bCore = dot(frame.rayOrigin, frame.rayDir);
    float coreT = coreHit ? max(tCoreNear, 0.0) : max(-bCore, 0.0);
    float impactCore = sqrt(max(dot(frame.rayOrigin, frame.rayOrigin) - bCore * bCore, 0.0));
    float coreMask = bCore < 0.0 ? smoothstep(horizon, horizon - horizon * (1.0 - CORE_RADIUS_FALLOFF), impactCore) : 0.0;
    coreMask *= sceneOcclusionVisibility(frame, frame.rayOrigin + frame.rayDir * coreT, sceneLinear, CORE_DEPTH_BIAS);
    vec4 coreColor = vec4(vec3(0.0), coreMask);


    // Calculate disk coord data
    vec3 diskNormal = vec3(0.0, 1.0, 0.0);
    vec3 diskTangent, diskBitangent;
    buildOrthoBasis(diskNormal, diskTangent, diskBitangent);


    // Compute disk mask, depth, and color. This layer doesn't write gl_FragDepth
    float diskT;
    vec4 edgeNoise = volumetricEdgeNoise(frame.rayOrigin, frame.rayDir, horizon, horizon * DISK_OUTER_SCALE, time, diskTangent, diskBitangent, diskNormal, diskT);
    float turbulence = edgeNoise.r;
    float diskMask = edgeNoise.a;
    diskMask *= sceneOcclusionVisibility(frame, frame.rayOrigin + frame.rayDir * diskT, sceneLinear, DISK_DEPTH_BIAS);
    vec4 diskColor = vec4(mix(vec3(1.0, 0.2, 0.05) * 0.8, vec3(1.0, 0.15, 0.15) * 0.1, pow(turbulence, 1.0)), diskMask);


    // Compute photon ring mask, depth, and color
    float ringT;
    float photonRingMask = volumetricPhotonRing(frame.rayOrigin, frame.rayDir, horizon, horizon * PHOTON_RING_SCALE - horizon, time, ringT);
    photonRingMask *= sceneOcclusionVisibility(frame, frame.rayOrigin + frame.rayDir * ringT, sceneLinear, RING_DEPTH_BIAS);
    vec4 photonRingColor = vec4(vec3(1.0, (photonRingMask * 1.1) * vec2(0.9, 0.8)), photonRingMask);


    // Compute falling squares mask, depth, and color
    float squaresT;
    vec4 squaresRaw = fallingSquares(frame.rayOrigin, frame.rayDir, horizon, horizon * SQUARE_AREA_SCALE, time, diskTangent, diskBitangent, diskNormal, squaresT);
    float squaresAlpha = squaresRaw.a * 0.8;
    squaresAlpha *= sceneOcclusionVisibility(frame, frame.rayOrigin + frame.rayDir * squaresT, sceneLinear, SQUARES_DEPTH_BIAS);
    vec4 squaresColor = vec4(squaresRaw.rgb, squaresAlpha);




    // Sort core/ring/disk/squares by depth
    float dCore = coreT, dRing = ringT, dDisk = diskT, dSquares = squaresT;
    vec4 cCore = coreColor, cRing = photonRingColor, cDisk = diskColor, cSquares = squaresColor;
    depthCompareSwap(dCore, dRing,    cCore, cRing);
    depthCompareSwap(dDisk, dSquares, cDisk, cSquares);
    depthCompareSwap(dCore, dDisk,    cCore, cDisk);
    depthCompareSwap(dRing, dSquares, cRing, cSquares);
    depthCompareSwap(dRing, dDisk,    cRing, cDisk);


    // Composite layers and output the final image
    fragColor = over(cCore, cRing, cDisk, cSquares, lensedSceneColor);
    float writeT   =  1e30;
    float writeNdc = -1.0;
    if(coreMask       > 0.001 && coreT    < writeT) { writeT = coreT;    writeNdc = impostorNdcDepth(frame, frame.rayOrigin + frame.rayDir * coreT);    }
    if(photonRingMask > 0.001 && ringT    < writeT) { writeT = ringT;    writeNdc = impostorNdcDepth(frame, frame.rayOrigin + frame.rayDir * ringT);    }
    if(squaresAlpha   > 0.001 && squaresT < writeT) { writeT = squaresT; writeNdc = impostorNdcDepth(frame, frame.rayOrigin + frame.rayDir * squaresT); }
    float newFragDepth = (writeNdc >= 0.0) ? writeNdc : sceneDepthRaw;
    gl_FragDepth = min(gl_FragDepth, newFragDepth);
    // fragColor = vec4(vec3(gl_FragDepth / 3), 1.0);
}
