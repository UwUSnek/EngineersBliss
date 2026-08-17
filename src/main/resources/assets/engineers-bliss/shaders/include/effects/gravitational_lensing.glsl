
#ifdef MINECRAFT
    #moj_import <engineers-bliss:utils/utils.glsl>
    #moj_import <engineers-bliss:utils/render_3d.glsl>
#endif








vec2 worldToScreenUV(vec3 worldPos) {
    vec4 clip = getProjMatrix() * getViewMatrix() * vec4(worldPos, 1.0);
    vec2 ndc = clip.xy / clip.w;
    return ndc * 0.5 + 0.5;
}


/**
 * Applies a gravitational lensing effect to the provided UVs.
 * @param SceneDepthSampler the scene sampler to read the background depth from.
 * @param centerWorld World space position of the lensing object's center.
 * @param screenUV The screen UV of the fragment being shaded.
 * @param uvToModify The screen UV to displace and return.
 * @param horizon The radius of the event horizon, in world units.
 * @param outerRadius The outer radius of the lensing falloff, in world units.
 * @param spin Frame dragging strength/direction. -1 to +1. 0 means no drag.
 **/
vec2 _internal_calculate_lensed_uv(ImpostorFrame frame, sampler2D SceneDepthSampler, vec3 centerWorld, vec2 screenUV, vec2 uvToModify, float horizon, float outerRadius, float spin) {
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

    vec2 direction = distance > 1e-5 ? (localUV / distance) : vec2(0.0);
    vec2 tangentDir = vec2(-direction.y, direction.x);
    vec2 fullPush = horizon * (-direction * lensStrength + tangentDir * dragAmount);




    // // Calculate depth gradient. This is used to blend the surroundings smoothly and avoid hard edges between normal/distorted areas.
    // float sceneDepth = texture(SceneDepthSampler, screenUV).x;
    // float sceneLinear = linearizeDepth(sceneDepth);
    float bRef = dot(frame.rayOrigin, frame.rayDir);
    // float refT = max(-bRef, 0.0); // near-side point of the sphere along the ray
    // vec3 refWorldPos = frame.rayOrigin + frame.rayDir * refT; // frame-local space
    vec3 refWorldPos = frame.rayOrigin + frame.rayDir * -bRef; // frame-local space
    float fragLinear  = linearizeDepth(impostorNdcDepth(frame, refWorldPos));
    // float depthEdge = 0.8;
    // float depthMask = smoothstep(fragLinear - depthEdge, fragLinear + depthEdge, sceneLinear);


    vec2 pushedUv = uvToModify + localToScreen * fullPush;

    // // // float destDepthRaw = texture(SceneDepthSampler, pushedUv).x;
    // // // float destLinear = linearizeDepth(destDepthRaw);
    // // // float destMask = smoothstep(fragLinear - depthEdge, fragLinear + depthEdge, destLinear);
    // // // float finalMask = min(depthMask, destMask);

    // // // float normalDepth = fragLinear;//linearizeDepth(texture(SceneDepthSampler, uvToModify).x);
    // // float normalDepth = linearizeDepth(texture(SceneDepthSampler, uvToModify).x);
    float pushedDepth = linearizeDepth(texture(SceneDepthSampler, pushedUv).x);
    // // // gl_FragDepth = pushedDepth;

    // // // // Return depth-tested distorted background
    // // // return mix(uvToModify, pushedUv, finalMask);
    // // // gl_FragDepth = min(pushedDepth, normalDepth);
    // // if(fragLinear < normalDepth) {
    // //     if(normalDepth > pushedDepth) {
    // //         return uvToModify;
    // //     }
    // //     else {
    // //         return pushedUv;
    // //     }
    // // }
    // // else {
    // //     return uvToModify;
    // // }
    // // // if(normalDepth > pushedDepth) {
    // //     // return pushedUv;
    // // // }
    // // // else {
    // //     // return uvToModify;
    // // // }
    // // // return normalDepth > pushedDepth ? pushedUv : uvToModify;

    // Depth of whatever's currently at this pixel (the potential occluder)
    float normalDepth = linearizeDepth(texture(SceneDepthSampler, uvToModify).x);

    // float occluderGap = normalDepth - fragLinear;                       //! Occluder gets lensed
    // float occluderGap = pushedDepth - fragLinear;                       //! Occluder leaves area of unlensed background
    // float occluderGap = max(pushedDepth, normalDepth) - fragLinear;     //! Occluder gets lensed
    float occluderGap = min(pushedDepth, normalDepth) - fragLinear;  //! Occluder leaves area of unlensed background

    // Fade the push strength to 0 as an occluder gets closer/more in-front,
    // fully strength once it's outside fadeRange.
    float fadeRange = horizon;
    float depthFade = clamp(occluderGap / fadeRange, 0.0, 1.0);

    // vec2 pushedUv = uvToModify + localToScreen * fullPush * depthFade;
    return pushedUv;
    // return uvToModify + localToScreen * fullPush * depthFade;

    //BUG lensing effect works but it shows through blocks
    //BUG trying to limit the effect to non-occluders doesn't work bc the data behind the occluders is not available
    //TODO making the effect fade out as the player moves further from it might work
    //TODO but idk how to implement that bc it needs to work on blocks that are far away but unobstructed
}








vec2 calculate_lensed_custom_uv(ImpostorFrame frame, sampler2D SceneDepthSampler, vec3 centerWorld, vec2 uvToModify, float horizon, float outerRadius, float spin) {
    vec2 screenUV = gl_FragCoord.xy / vec2(textureSize(SceneDepthSampler, 0));
    return _internal_calculate_lensed_uv(frame, SceneDepthSampler, centerWorld, screenUV, uvToModify, horizon, outerRadius, spin);
}


vec2 calculate_lensed_screen_uv(ImpostorFrame frame, sampler2D SceneDepthSampler, vec3 centerWorld, float horizon, float outerRadius, float spin) {
    vec2 screenUV = gl_FragCoord.xy / vec2(textureSize(SceneDepthSampler, 0));
    return _internal_calculate_lensed_uv(frame, SceneDepthSampler, centerWorld, screenUV, screenUV, horizon, outerRadius, spin);
}