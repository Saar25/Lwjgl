#version 400 core

const float PI = 3.14159265358979323846;
const float tiling = 15.0 / 800;

layout (location = 0) in vec2 in_position;
layout (location = 1) in vec2 in_position1;
layout (location = 2) in vec2 in_position2;

out vec3 v_position;
out vec2 out_texCoord;
out vec3 out_normal;
out vec4 clipSpace;
out vec3 toCameraVector;

uniform mat4 projectionViewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 in_cameraPosition;
uniform vec4 clipPlane;

uniform float time;

float rand(float n) {
    return fract(sin(n) * 43758.5453123);
}

float rand(vec2 n) {
    return fract(sin(dot(n, vec2(12.9898, 4.1414))) * 43758.5453);
}

float noise(vec2 position) {
    //float t = time * .5;
    //position = position * PI;
    //return (cos(position.x * 429 + t) * sin(position.y * 234 + t)) / 400
    //+ (cos(position.x * 278 + t) * sin(position.y * 971 + t)) / 400
    //    + (sin(time + (3 * position.x + position.y) * 800)) / 400;
    //return (sin(time + (3 * position.x + position.y) * 800)) / 400;
    return (cos(position.x * 4297) * sin(position.y * 2347) + sin(time + (3 * position.x + position.y) * 800)) / 1600;
}

vec3 getNormal(vec2 p1, vec2 p2, vec2 p3) {
    vec3 p11 = vec3(p1.x, noise(p1), p1.y);
    vec3 p12 = vec3(p2.x, noise(p2), p2.y);
    vec3 p13 = vec3(p3.x, noise(p3), p3.y);
    vec3 v1 = normalize(p11 - p12);
    vec3 v2 = normalize(p13 - p12);
    return cross(v1, v2);
}

void main(void) {
    float height = noise(in_position);
    vec3 position = vec3(in_position.x, height, in_position.y);
    vec4 worldPosition4f = transformationMatrix * vec4(position, 1.0);
    gl_Position = clipSpace = projectionViewMatrix * worldPosition4f;
    gl_ClipDistance[0] = dot(worldPosition4f, clipPlane);

    out_texCoord = in_position * tiling;
    out_normal = getNormal(in_position, in_position1, in_position2);
    v_position = worldPosition4f.xyz / worldPosition4f.w;
    toCameraVector = in_cameraPosition - v_position;
}