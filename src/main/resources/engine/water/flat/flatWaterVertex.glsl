#version 400 core

layout (location = 0) in vec2 in_position;

const float tiling = 40;

out vec3 v_position;
out vec2 v_texCoord;
out vec4 v_clipSpace;
out vec3 v_toCameraVector;

uniform mat4 projectionViewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 in_cameraPosition;
uniform vec4 clipPlane;

void main(void) {
    vec3 position = vec3(in_position.x, 0, in_position.y);
    vec4 worldPosition4f = transformationMatrix * vec4(position, 1.0);
    gl_Position = v_clipSpace = projectionViewMatrix * worldPosition4f;
    gl_ClipDistance[0] = dot(worldPosition4f, clipPlane);

    v_texCoord = in_position * tiling;
    v_position = worldPosition4f.xyz / worldPosition4f.w;
    v_toCameraVector = in_cameraPosition - v_position;
}