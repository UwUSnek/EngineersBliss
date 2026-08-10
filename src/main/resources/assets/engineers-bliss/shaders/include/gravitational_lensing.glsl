



/**
 * Applies a gravitational lensing effect to the background color.
 * @param SceneSampler the scene sampler to read the background color from.
 * @param SceneDepthSampler the scene sampler to read the background depth from.
 * @param objectColor The color of the object to draw in front of the distorted background.
 * @param uv The UV coordinates.
 * @param distance The distance from the center, in normalized UV units.
 * @param horizon The radius of the event horizon, in normalized UV units.
 * @param spin Frame dragging strength/direction (-1 to 1). 0 means no drag.
 **/
vec4 apply_lensing_background(sampler2D SceneSampler, sampler2D SceneDepthSampler, vec4 objectColor, vec2 uv, float distance, float horizon, float spin){

    // Calculate the distance from the event horizon
    float maxHorizonDistance = 0.5 - horizon;
    float horizonDistance = clamp(abs(distance - horizon), 0.0, maxHorizonDistance);

    // Calculate lensing effect strength. The closer to the horizon, the stronger the effect.
    float lensStrength = 1.0 - (horizonDistance / maxHorizonDistance);
    lensStrength = pow(lensStrength, 3.0);




    // Calculate screen UVs and blend amount
    vec2 screenUV = gl_FragCoord.xy / vec2(textureSize(SceneSampler, 0));
    float bendAmount = lensStrength * 0.2;
    float dragAmount = lensStrength * spin * 0.15;

    // Calculate depth gradient. This is used to blend the surroundings smoothly and avoid hard edges between normal/distorted areas.
    float sceneDepth = texture(SceneDepthSampler, screenUV).r;
    float sceneLinear = linearizeDepth(sceneDepth);
    float fragLinear  = linearizeDepth(gl_FragCoord.z);
    float depthEdge = 0.8;
    float depthMask = smoothstep(fragLinear - depthEdge, fragLinear + depthEdge, sceneLinear);
    bendAmount *= depthMask;

    // Calculate lensed & dragged UVs
    vec2 direction = normalize(uv);
    vec2 tangent = vec2(-direction.y, direction.x);
    vec2 distortedUV = screenUV - direction * bendAmount + tangent * dragAmount;




    // Sample the background using the lensed UVs and overlay the object's color on top of it
    vec4 sceneColor = texture(SceneSampler, clamp(distortedUV, 0.0, 1.0));
    float finalAlpha = objectColor.a * depthMask;
    float visibility = max(finalAlpha, bendAmount);
    if(visibility < 0.0001) discard;
    return vec4(mix(sceneColor.rgb, objectColor.rgb, finalAlpha), 1.0);
}