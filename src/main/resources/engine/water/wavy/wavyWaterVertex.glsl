#version 400 core

const float PI = 3.14159265358979323846;
const float tiling = 15;

layout (location = 0) in vec2 in_position;
layout (location = 1) in vec2 in_position1;
layout (location = 2) in vec2 in_position2;

out vec3 v_position;
out vec2 v_texCoord;
out vec3 v_normal;
out vec4 v_clipSpace;
out vec3 v_toCameraVector;

uniform mat4 projectionViewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 in_cameraPosition;
uniform vec4 clipPlane;

uniform float amplitude;
uniform float time;

float noise(vec2 position) {
    vec4 wPosition = transformationMatrix * vec4(position.x, 0, position.y, 1);
    position = wPosition.xz / wPosition.w;
    float val1 = cos(position.x * .4297 + time) * sin(position.y * .2347);
    float val2 = sin(.5 * time + (3 * position.x + position.y));
    return (val1 + val2) * amplitude;
}

vec3 getNormal(vec2 p1, vec2 p2, vec2 p3) {
    vec4 wp1 = transformationMatrix * vec4(p1.x, 0, p1.y, 1);
    vec4 wp2 = transformationMatrix * vec4(p2.x, 0, p2.y, 1);
    vec4 wp3 = transformationMatrix * vec4(p3.x, 0, p3.y, 1);

    vec3 p11 = vec3(p1.x, noise(wp1.xz / wp1.w), p1.y);
    vec3 p12 = vec3(p2.x, noise(wp2.xz / wp2.w), p2.y);
    vec3 p13 = vec3(p3.x, noise(wp3.xz / wp3.w), p3.y);
    vec3 v1 = normalize(p11 - p12);
    vec3 v2 = normalize(p13 - p12);
    return cross(v1, v2);
}

void main(void) {
    float height = noise(in_position);
    vec3 position = vec3(in_position.x, 0, in_position.y);
    vec3 distortedPosition = vec3(in_position.x, height, in_position.y);

    vec4 homogeneousCoords = transformationMatrix * vec4(position, 1.0);
    vec4 distortedHCoords = transformationMatrix * vec4(distortedPosition, 1.0);

    gl_Position = projectionViewMatrix * distortedHCoords;
    v_clipSpace = projectionViewMatrix * homogeneousCoords;

    gl_ClipDistance[0] = dot(distortedHCoords, clipPlane);

    v_texCoord = in_position * tiling;
    v_normal = getNormal(in_position, in_position1, in_position2);
    v_position = distortedHCoords.xyz / distortedHCoords.w;
    v_toCameraVector = in_cameraPosition - v_position;
}