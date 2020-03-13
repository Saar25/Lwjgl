/*
    TERRAIN VERTEX SHADER
*/
#version 400 core

const float shadowTransition = 10;

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 normal;

smooth out vec3 smthNormal;
flat   out vec3 flatNormal;

out vec3 worldPosition;
out vec2 outTexCoord;
out vec3 toCameraVector;
out vec4 shadowMapCoords;

uniform mat4 projectionViewMatrix;
uniform mat4 transformationMatrix;
uniform mat4 shadowMapSpaceMatrix;
uniform vec3 in_cameraPosition;
uniform float shadowDistance;
uniform vec4 clipPlane;

vec3 calculateNormal() {
    return normalize(transformationMatrix * vec4(normal, 0.0)).xyz;
}

vec4 calculateShadowMapCoord(float cameraDistance) {
    vec4 shadowMapCoord = shadowMapSpaceMatrix * vec4(position, 1.0);
    shadowMapCoord.w  = cameraDistance - shadowDistance + shadowTransition;
    shadowMapCoord.w  = clamp(1 - shadowMapCoord.w / shadowTransition, 0, 1);
    return shadowMapCoord;
}

void main(void) {
    vec4 worldPosition4f = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionViewMatrix * worldPosition4f;
    gl_ClipDistance[0] = dot(worldPosition4f, clipPlane);

    outTexCoord = texCoord;
    smthNormal = calculateNormal();
    flatNormal = smthNormal;
    worldPosition = worldPosition4f.xyz;
    toCameraVector = in_cameraPosition - worldPosition;

    float cameraDistance = length(toCameraVector);

    shadowMapCoords = calculateShadowMapCoord(cameraDistance);
}