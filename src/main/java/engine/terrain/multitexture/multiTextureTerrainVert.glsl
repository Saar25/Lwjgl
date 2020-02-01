/*
    TERRAIN VERTEX SHADER
*/
#version 400 core

const float shadowTransition = 10;

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 normal;

out vec3 v_normal;
out vec3 worldPosition;
out vec2 v_texCoords;
out vec3 toCameraVector;
out vec4 v_shadowHCoords;
out vec3 v_viewPosition;

uniform mat4 projectionViewMatrix;
uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 shadowSpaceMatrix;
uniform vec3 cameraPosition;
uniform float shadowDistance;
uniform vec4 clipPlane;

vec3 calculateNormal() {
    return normalize(transformationMatrix * vec4(normal, 0.0)).xyz;
}

vec4 calculateShadowSpaceCoord(float cameraDistance) {
    vec4 shadowSpaceCoord = shadowSpaceMatrix * vec4(position, 1.0);
    shadowSpaceCoord.w = cameraDistance - shadowDistance + shadowTransition;
    shadowSpaceCoord.w = clamp(1 - shadowSpaceCoord.w / shadowTransition, 0, 1);
    return shadowSpaceCoord;
}

void main(void) {
    vec4 worldPosition4f = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionViewMatrix * worldPosition4f;
    gl_ClipDistance[0] = dot(worldPosition4f, clipPlane);

    v_texCoords = texCoord;
    v_normal = calculateNormal();
    worldPosition = worldPosition4f.xyz;
    toCameraVector = cameraPosition - worldPosition;

    vec4 viewPosition = viewMatrix * worldPosition4f;
    v_viewPosition = viewPosition.xyz / viewPosition.w;

    float cameraDistance = length(toCameraVector);

    v_shadowHCoords = calculateShadowSpaceCoord(cameraDistance);
}