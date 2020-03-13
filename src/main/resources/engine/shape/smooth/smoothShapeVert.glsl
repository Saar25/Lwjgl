/*
    TERRAIN VERTEX SHADER
*/
#version 400 core

const float shadowTransition = 10;

/* ====================== */
/*                        */
/*   IN / OUT VARIABLES   */
/*                        */
/* ====================== */

    layout (location = 0) in vec3 position;
    layout (location = 1) in vec3 normal;

    out vec3 v_spacePosition;
    out vec3 v_position;
    out vec3 v_normal;
    out vec4 v_shadowHCoords;


/* ===================== */
/*                       */
/*   UNIFORM VARIABLES   */
/*                       */
/* ===================== */

    uniform mat4 transformation;
    uniform mat4 mvpMatrix;
    uniform vec4 clipPlane;

    uniform vec3 cameraPosition;
    uniform float shadowDistance;
    uniform mat4 shadowSpaceMatrix;


vec3 calculateNormal() {
    return (transformation * vec4(normal, 0)).xyz;
}


/* ======================== */
/*                          */
/*   SHADOWS CALCULATIONS   */
/*                          */
/* ======================== */

vec4 calculateShadowMapCoord(float cameraDistance) {
    vec4 shadowSpaceCoords = shadowSpaceMatrix * vec4(position, 1.0);
    shadowSpaceCoords.w = cameraDistance - shadowDistance + shadowTransition;
    shadowSpaceCoords.w = clamp(1 - shadowSpaceCoords.w / shadowTransition, 0, 1);
    return shadowSpaceCoords;
}

/* =============== */
/*                 */
/*   MAIN METHOD   */
/*                 */
/* =============== */

void main(void) {
    vec4 homogeneous = transformation * vec4(position, 1);
    gl_ClipDistance[0] = dot(homogeneous, clipPlane);
    gl_Position = mvpMatrix * vec4(position, 1);

    v_spacePosition = homogeneous.xyz / homogeneous.w;
    v_normal = calculateNormal();
    v_position = position;
    v_shadowHCoords = calculateShadowMapCoord(distance(v_spacePosition, cameraPosition));
}