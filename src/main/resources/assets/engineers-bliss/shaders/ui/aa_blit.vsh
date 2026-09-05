#version 150


#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV1;
in ivec2 UV2; //! Floats packed to 16-bits shorts

out vec4 vertexColor;
out vec2 localPos;
out vec2 rectSize;
out vec2 texCoord;




void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexColor = Color;
    localPos = UV0;
    rectSize = vec2(UV1);
    texCoord = vec2(UV2) / 32767.0; //! Unpack uv2 and pass it to the fragment shader as floats
}