#version 150

// local billboard plane
in vec3 Position;
in vec2 UV0;

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
out vec2 uv0;
out vec3 worldPos;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position + ModelOffset, 1.0);
    vertexColor = ColorModulator;
    uv0 = UV0;
    worldPos = Position + ModelOffset;
}
