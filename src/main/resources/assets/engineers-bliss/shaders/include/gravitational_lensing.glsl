

#ifdef MINECRAFT
    #moj_import <engineers-bliss:utils.glsl>
#endif






vec2 worldToScreenUV(vec3 worldPos) {
    vec4 clip = getProjMatrix() * getViewMatrix() * vec4(worldPos, 1.0);
    vec2 ndc = clip.xy / clip.w;
    return ndc * 0.5 + 0.5;
}


/**
 * Applies a gravitational lensing effect to the background color.
 * @param SceneDepthSampler the scene sampler to read the background depth from.
 * @param centerWorld World space position of the lensing object's center.
 * @param screenUV The screen UV of the fragment being shaded.
 * @param uvToModify The screen UV to displace and return.
 * @param horizon The radius of the event horizon, in world units.
 * @param outerRadius The outer radius of the lensing falloff, in world units.
 * @param spin Frame dragging strength/direction. -1 to +1. 0 means no drag.
 **/
vec2 _internal_calculate_lensed_uv(sampler2D SceneDepthSampler, vec3 centerWorld, vec2 screenUV, vec2 uvToModify, float horizon, float outerRadius, float spin){

    mat4 camToWorld = inverse(getViewMatrix());
    vec3 camRight = normalize(camToWorld[0].xyz);
    vec3 camUp    = normalize(camToWorld[1].xyz);

    vec2 screenCenter = worldToScreenUV(centerWorld);
    float eps = 0.01;
    vec2 basisX = (worldToScreenUV(centerWorld + camRight * eps) - screenCenter) / eps;
    vec2 basisY = (worldToScreenUV(centerWorld + camUp    * eps) - screenCenter) / eps;
    mat2 localToScreen = mat2(basisX, basisY);
    mat2 screenToLocal = inverse(localToScreen);


    float det = determinant(localToScreen);
    if(abs(det) < 1e-5) {
        return uvToModify;
    }


    vec2 localUV = screenToLocal * (screenUV - screenCenter);
    float distance = length(localUV);

    // Calculate the distance from the event horizon
    float maxHorizonDistance = outerRadius - horizon;
    float horizonDistance = clamp(abs(distance - horizon), 0.0, maxHorizonDistance);

    // Calculate lensing effect strength. The closer to the horizon, the stronger the effect.
    float lensStrength = 1.0 - (horizonDistance / maxHorizonDistance);
    lensStrength = pow(lensStrength, 2.0);
    float dragAmount = lensStrength * spin * 0.15;

    // Calculate depth gradient. This is used to blend the surroundings smoothly and avoid hard edges between normal/distorted areas.
    float sceneDepth = texture(SceneDepthSampler, screenUV).x;
    float sceneLinear = linearizeDepth(sceneDepth);
    float fragLinear  = linearizeDepth(gl_FragCoord.z);
    float depthEdge = 0.8;
    float depthMask = smoothstep(fragLinear - depthEdge, fragLinear + depthEdge, sceneLinear);

    vec2 direction = distance > 1e-5 ? (localUV / distance) : vec2(0.0);
    vec2 tangentDir = vec2(-direction.y, direction.x);

    vec2 localPush = -direction * lensStrength * depthMask * horizon + tangentDir * dragAmount * horizon;
    return uvToModify + localToScreen * localPush;
}





vec2 calculate_lensed_custom_uv(sampler2D SceneDepthSampler, vec3 centerWorld, vec2 uvToModify, float horizon, float outerRadius, float spin){
    vec2 screenUV = gl_FragCoord.xy / vec2(textureSize(SceneDepthSampler, 0));
    return _internal_calculate_lensed_uv(SceneDepthSampler, centerWorld, screenUV, uvToModify, horizon, outerRadius, spin);
}
vec2 calculate_lensed_screen_uv(sampler2D SceneDepthSampler, vec3 centerWorld, float horizon, float outerRadius, float spin){
    vec2 screenUV = gl_FragCoord.xy / vec2(textureSize(SceneDepthSampler, 0));
    return _internal_calculate_lensed_uv(SceneDepthSampler, centerWorld, screenUV, screenUV, horizon, outerRadius, spin);
}