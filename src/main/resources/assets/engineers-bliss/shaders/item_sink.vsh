#version 150

// Local billboard-plane offset: x/y in -0.5..0.5, z is always 0.
in vec3 Position;

layout(std140) uniform DynamicTransforms {
    mat4 ModelViewMat;
    vec4 ColorModulator;
    vec3 ModelOffset;
    mat4 TextureMat;
};

layout(std140) uniform Projection {
    mat4 ProjMat;
};

out vec4 vertexColor;
out vec2 texCoord;


void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position + ModelOffset, 1.0);
    vertexColor = ColorModulator;
    texCoord = Position.xz;
}