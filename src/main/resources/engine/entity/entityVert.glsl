/*
    ENTITY VERTEX SHADER
*/
#version 400

const float shadowTransition = 10;

// Per Vertex attibutes
layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texCoord;
layout (location = 2) in vec3 in_normal;

// Per Instance attibutes
// layout (location = 3) in mat4 transformation;
// layout (location = 7) in vec2 textureOffset;
// layout (location = 8) in int  textureRows;

out vec3 v_position;
out vec2 v_texCoord;
out vec3 v_normal;
out vec3 v_reflected;
out vec3 v_camPos;
out vec3 v_viewPosition;
out vec4 shadowMapCoords;

uniform mat4 mvpMatrix;
uniform mat4 transformation;
uniform mat4 viewMatrix;
uniform mat4 shadowSpaceMatrix;
uniform int textureRowsCount;
uniform vec2 textureOffset;

// uniform mat4 pvMatrix;
// uniform mat4 pvShadow;
uniform float shadowDistance;
uniform vec3 in_cameraPosition;
uniform vec4 clipPlane;

vec4 calculatePosition() {
    return transformation * vec4(in_position, 1.0);
}

vec2 calculateTexCoords() {
    return textureRowsCount > 1 ? (in_texCoord / textureRowsCount) + textureOffset : in_texCoord;
}

vec3 calculateNormal() {
    return normalize((transformation * vec4(in_normal, 0.0)).xyz);
}

vec3 calculateReflected(vec3 position, vec3 normal) {
    return normalize(reflect(in_cameraPosition - position, normal));
}

void main() {

    gl_Position = mvpMatrix * vec4(in_position, 1.0);

    vec4 worldPosition = calculatePosition();
    gl_ClipDistance[0] = dot(worldPosition, clipPlane);

    v_position  = worldPosition.xyz / worldPosition.w;
    v_texCoord  = calculateTexCoords();
    v_normal    = calculateNormal();
    v_camPos    = in_cameraPosition;
    v_reflected = calculateReflected(v_position, v_normal);

    vec4 viewPosition = viewMatrix * worldPosition;
    v_viewPosition = viewPosition.xyz / viewPosition.w;

    float distance = distance(v_camPos, v_position);
    shadowMapCoords   = shadowSpaceMatrix * vec4(in_position, 1.0);
    shadowMapCoords.w = (distance - shadowDistance + shadowTransition) / shadowTransition;
    shadowMapCoords.w = clamp(1 - shadowMapCoords.w, 0, 1);
}