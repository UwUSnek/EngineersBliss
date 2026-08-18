
#version 150
#define MINECRAFT








// Noise settings
#define NOISE_TWIST 48.0

// Squares settings
#define SQUARE_RADIAL_DENSITY   2.0
#define SQUARE_FACE_DENSITY     25.0
#define SQUARE_FALL_SPEED      -0.01
#define SQUARE_DENSITY_THRESH   0.5
#define SQUARE_SIZE_MIN         0.05
#define SQUARE_SIZE_MAX         0.1








#ifdef MINECRAFT
    #moj_import <minecraft:globals.glsl>
    #moj_import <minecraft:projection.glsl>
    #moj_import <minecraft:dynamictransforms.glsl>
    #moj_import <engineers-bliss:utils/render_3d.glsl>
    #moj_import <engineers-bliss:effects/gravitational_lensing.glsl>
    #moj_import <engineers-bliss:parts/edge_noise.glsl>
    #moj_import <engineers-bliss:parts/falling_squares.glsl>
    #moj_import <engineers-bliss:parts/ring_noise.glsl>

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
#define SQUARE_AREA_SCALE     2.0


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
    float horizon = 0.4 * (PlaneSize / 4.0);
    //  ^ world scale radius. horizon * LENSING_SCALE must be < quad size
    //! ^ PlaneSize is defined by render_3d
#else
void mainImage(out vec4 fragColor, in vec2 fragCoord) {
    vec2 uncorrectedUV = fragCoord / iResolution.xy;
    vec2 uv0 = (fragCoord - 0.5 * iResolution.xy) / min(iResolution.x, iResolution.y) + 0.5;
    if(max(abs(uv0.x - 0.5), abs(uv0.y - 0.5)) > 0.5) uv0 = vec2(-999.0, -999.0);

    ImpostorFrame frame = getImpostorFrame(uv0);
    float horizon = texture(iChannel2, vec2(0)).x; // world scale radius, controlled with keys
#endif

    vec2 uv = uv0 - vec2(0.5);
    mat4 viewProj = computeViewProjMatrix();




    // Calculate lensed screen UVs, then use them to poll the distorted scene background
    float lensingBoundary = horizon * PHOTON_RING_SCALE;
    vec2 lensedScreenUV = calculate_lensed_screen_uv(frame, viewProj, sceneDepthSampler, frame.center, lensingBoundary, lensingBoundary * LENSING_SCALE, 0.35);
    vec4 lensedSceneColor = texture(sceneColorSampler, lensedScreenUV);


    // Poll scene depth and linearize it
    float lensedSceneDepth = texture(sceneDepthSampler, lensedScreenUV).x;
    float sceneLinear = linearizeDepth(lensedSceneDepth);


    // Compute core mask, depth, and color
    float tCoreNear, tCoreFar;
    bool coreHit = intersectSphere(frame.rayOrigin, frame.rayDir, horizon, tCoreNear, tCoreFar) && tCoreFar >= 0.0;
    float bCore = dot(frame.rayOrigin, frame.rayDir);
    float coreT = coreHit ? max(tCoreNear, 0.0) : max(-bCore, 0.0);
    float impactCore = sqrt(max(dot(frame.rayOrigin, frame.rayOrigin) - bCore * bCore, 0.0));
    float coreMask = bCore < 0.0 ? smoothstep(horizon, horizon - horizon * (1.0 - CORE_RADIUS_FALLOFF), impactCore) : 0.0;
    float coreNdcDepth = impostorNdcDepth(frame, viewProj, frame.rayOrigin + frame.rayDir * coreT);
    coreMask *= sceneOcclusionVisibility(coreNdcDepth, sceneLinear, CORE_DEPTH_BIAS);
    vec4 coreColor = vec4(vec3(0.95, 0.95, 1.0), coreMask);


    // Calculate disk coord data
    vec3 diskNormal = vec3(0.0, 1.0, 0.0);
    vec3 diskTangent, diskBitangent;
    buildOrthoBasis(diskNormal, diskTangent, diskBitangent);


    // Compute disk mask, depth, and color. This layer doesn't write gl_FragDepth
    float diskT;
    vec4 edgeNoise = volumetricEdgeNoise(frame.rayOrigin, frame.rayDir, horizon, horizon * DISK_OUTER_SCALE, time, diskTangent, diskBitangent, diskNormal, diskT);
    float turbulence = edgeNoise.r;
    float diskMask = edgeNoise.a;
    diskMask *= sceneOcclusionVisibility(frame, viewProj, frame.rayOrigin + frame.rayDir * diskT, sceneLinear, DISK_DEPTH_BIAS);
    vec4 diskColor = vec4(mix(vec3(0.1, 0.5, 1.0) * 0.8, vec3(0.15, 0.5, 1.0) * 0.1, pow(turbulence, 1.0)), diskMask);


    // Compute photon ring mask, depth, and color
    float ringT;
    float photonRingMask = volumetricPhotonRing(frame.rayOrigin, frame.rayDir, horizon, horizon * PHOTON_RING_SCALE - horizon, time, ringT);
    float photonRingNdcDepth = impostorNdcDepth(frame, viewProj, frame.rayOrigin + frame.rayDir * ringT);
    photonRingMask *= sceneOcclusionVisibility(photonRingNdcDepth, sceneLinear, RING_DEPTH_BIAS);
    vec4 photonRingColor = vec4(vec3(0.5, vec2(0.9, 0.8)), photonRingMask);


    // Compute falling squares mask, depth, and color
    float squaresT;
    vec4 squaresRaw = fallingSquares(frame.rayOrigin, frame.rayDir, horizon, horizon * SQUARE_AREA_SCALE, time, diskTangent, diskBitangent, diskNormal, squaresT);
    float squaresAlpha = squaresRaw.a * 0.8;
    float squaresNdcDepth = impostorNdcDepth(frame, viewProj, frame.rayOrigin + frame.rayDir * squaresT);
    squaresAlpha *= sceneOcclusionVisibility(squaresNdcDepth, sceneLinear, SQUARES_DEPTH_BIAS);
    vec4 squaresColor = vec4(squaresRaw.rgb, squaresAlpha);




    // Depth-sort and composite core/ring/disk/squares
    fragColor = compositeLayers(
        coreT, coreColor,
        ringT, photonRingColor,
        diskT, diskColor,
        squaresT, squaresColor,
        lensedSceneColor
    );


    // Composite depth layers and write gl_FragDepth
    gl_FragDepth = min(gl_FragDepth, compositeDepthLayers(
        coreT,    coreNdcDepth,       coreMask,
        ringT,    photonRingNdcDepth, photonRingMask,
        squaresT, squaresNdcDepth,    squaresAlpha,
        lensedSceneDepth
    ));


    // Draw black bars on the sides if rendering in ShaderToy
    #ifndef MINECRAFT
        gl_FragDepth = gl_FragCoord.z;
        if(uv0.x < -1.0) fragColor = vec4(uv0, 0.0, 1.0);
    #endif
}
