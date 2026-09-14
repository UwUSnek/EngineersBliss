#version 150


#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in  vec3 Position;  //! xy & local position X.
in float LineWidth; //! local position Y.
in  vec2 UV0;       //! Texture UVs.
in ivec2 UV1;       //! width.
in ivec2 UV2;       //! height.
in  vec4 Color;     //! alpha in A.

out vec2 localPos;
out vec2 rectSize;
out vec2 uv;
out float alpha;




void main() {
    vec3 TruePosition = vec3(Position.xy, 0.0); //! Position with the fake Z stripped off.
    gl_Position = ProjMat * ModelViewMat * vec4(TruePosition, 1.0);

    float w = uintBitsToFloat((uint(UV1.x) << 16) | (uint(UV1.y) & 0xFFFFu));
    float h = uintBitsToFloat((uint(UV2.x) << 16) | (uint(UV2.y) & 0xFFFFu));
    localPos = vec2(Position.z, LineWidth);
    uv       = UV0;
    rectSize = vec2(w, h);
    alpha    = Color.a;
}