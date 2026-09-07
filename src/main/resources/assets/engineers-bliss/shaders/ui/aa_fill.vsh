#version 150


#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in float LineWidth;
in vec2 UV0;
in vec4 Color;

out vec4 vertexColor;
out vec2 localPos;
out vec2 rectSize;




void main() {
    vec3 TruePosition = vec3(Position.xy, 0.0); //! Position with the fake Z stripped off.
    gl_Position = ProjMat * ModelViewMat * vec4(TruePosition, 1.0);

    localPos = vec2(Position.z, LineWidth);
    rectSize = vec2(UV0);
    vertexColor = Color;
}