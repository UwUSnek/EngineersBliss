#version 150

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in  vec3 Position;
in float LineWidth;
in  vec4 Color;
in  vec2 UV0;

out vec4  vertexColor;
out float vCrossDist;
out float vHalfThickness;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    vertexColor    = Color;
    vCrossDist     = UV0.x;
    vHalfThickness = LineWidth * 0.5;
}