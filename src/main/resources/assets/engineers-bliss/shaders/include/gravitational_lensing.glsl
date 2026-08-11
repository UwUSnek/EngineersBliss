#ifdef MINECRAFT
    #moj_import <engineers-bliss:utils.glsl>
#endif





/**
 * Applies a gravitational lensing effect to the background color.
 * @param SceneDepthSampler the scene sampler to read the background depth from.
 * @param uv The UV coordinates. -0.5 to +0.5.
 * @param horizon The radius of the event horizon, in UV units.
 * @param spin Frame dragging strength/direction. -1 to +1. 0 means no drag.
 **/
vec2 calculate_lensed_uv(sampler2D SceneDepthSampler, vec2 uv, float horizon, float outerRadius, float spin){
    float distance = length(uv);

    // Calculate the distance from the event horizon
    float maxHorizonDistance = outerRadius - horizon;
    float horizonDistance = clamp(abs(distance - horizon), 0.0, maxHorizonDistance);

    // Calculate lensing effect strength. The closer to the horizon, the stronger the effect.
    float lensStrength = 1.0 - (horizonDistance / maxHorizonDistance);
    lensStrength = pow(lensStrength, 2.0);




    // Calculate screen UVs and bend amount
    vec2 screenUV = gl_FragCoord.xy / vec2(textureSize(SceneDepthSampler, 0));
    //float bendAmount = lensStrength * 1.0;
    float dragAmount = lensStrength * spin * 0.15;

    // Calculate depth gradient. This is used to blend the surroundings smoothly and avoid hard edges between normal/distorted areas.
    float sceneDepth = texture(SceneDepthSampler, screenUV).r;
    float sceneLinear = linearizeDepth(sceneDepth);
    float fragLinear  = linearizeDepth(gl_FragCoord.z);
    float depthEdge = 0.8;
    float depthMask = smoothstep(fragLinear - depthEdge, fragLinear + depthEdge, sceneLinear);
    //bendAmount *= depthMask;

    // Calculate lensed & dragged UVs
    vec2 direction = normalize(uv);
    vec2 tangent = vec2(-direction.y, direction.x);
    //return clamp(screenUV - direction * (lensStrength * depthMask) + tangent * dragAmount, 0.0, 1.0);
    return screenUV - direction * (lensStrength * depthMask) + tangent * dragAmount;
}
