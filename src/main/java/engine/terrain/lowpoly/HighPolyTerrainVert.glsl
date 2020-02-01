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
    layout (location = 1) in vec3 colour;
    layout (location = 2) in vec3 normal;

    out vec3 v_colour;
    out vec3 v_normal;
    out vec3 v_position;
    out vec4 v_shadowHCoords;


/* ===================== */
/*                       */
/*   UNIFORM VARIABLES   */
/*                       */
/* ===================== */

    uniform mat4 projectionViewMatrix;
    uniform vec3 cameraPosition;
    uniform vec4 clipPlane;

    uniform float amplitude;

    uniform float shadowDistance;

    uniform mat4 transformationMatrix;
    uniform mat4 shadowSpaceMatrix;


/* ========================== */
/*                            */
/*   UTILITIES CALCULATIONS   */
/*                            */
/* ========================== */

vec3 calculateNormal() {
    return normalize(transformationMatrix * vec4(normal, 0.0)).xyz;
}

vec3 perspectiveDivide(vec4 v) {
    return v.xyz / v.w;
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
    vec4 homogeneousCoords = transformationMatrix * vec4(position * vec3(1, amplitude, 1) , 1.0);
    gl_Position = projectionViewMatrix * homogeneousCoords;
    gl_ClipDistance[0] = dot(homogeneousCoords, clipPlane);

    v_normal = calculateNormal();
    v_position = perspectiveDivide(homogeneousCoords);
    v_colour = colour;

    vec3 toCamera = cameraPosition - v_position;
    float cameraDistance = length(toCamera);
    v_shadowHCoords = calculateShadowMapCoord(cameraDistance);
}