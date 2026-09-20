#version 150


#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in  vec3 Position;  //! xy
in  vec2 UV0;       //! Texture UVs.
in  vec4 Color;     //! alpha in A.

out vec2 uv;
out float alpha;




void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    uv       = UV0;
    alpha    = Color.a;
}