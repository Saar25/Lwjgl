#version 400 core

layout (location = 0) in vec3 position;

out vec3 texCoord;

uniform mat4 mvpMatrix;

void main() {

    gl_Position = mvpMatrix * vec4(position, 0.0);
    gl_Position = gl_Position.xyww;

    gl_ClipDistance[0] = 0;

    texCoord = position;
}
