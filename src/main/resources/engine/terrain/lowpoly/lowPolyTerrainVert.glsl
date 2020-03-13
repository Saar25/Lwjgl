/*
    TERRAIN VERTEX SHADER
*/
#version 400 core

#define MAX_LIGHTS 10
const float shadowTransition = 10;
const float ambientFactor = 0.1;

struct Light {
    vec3 position;
    vec3 colour;
    vec3 attenuation;
    bool directional;
    float intensity;
};


/* ====================== */
/*                        */
/*   IN / OUT VARIABLES   */
/*                        */
/* ====================== */

    layout (location = 0) in vec3 position;
    layout (location = 1) in vec3 colour;
    layout (location = 2) in vec3 normal;

    flat out vec3 v_colour;
    flat out vec3 v_normal;
    out vec4 v_shadowHCoords;


/* ===================== */
/*                       */
/*   UNIFORM VARIABLES   */
/*                       */
/* ===================== */

    uniform mat4 projectionViewMatrix;
    uniform vec3 cameraPosition;
    uniform vec4 clipPlane;

    uniform Light lights[MAX_LIGHTS];
    uniform int lightsCount;

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


/* ========================= */
/*                           */
/*   LIGHTING CALCULATIONS   */
/*                           */
/* ========================= */

float calculateAttenuationFactor(vec3 attenuation, vec3 toLightVector) {
    float d = length(toLightVector);
    return 1 / (attenuation.x + attenuation.y * d + attenuation.z * d * d);
}

float calculateAmbientFactor() {
    return ambientFactor;
}

float calculateDiffuseFactor(vec3 normal, float intensity, vec3 toLight) {
    return max(dot(toLight, normal), 0.0) * intensity;
}

vec3 calculateAmbientColour(Light light) {
    return light.colour * calculateAmbientFactor();
}

vec3 calculateDiffuseColour(Light light, vec3 normal, vec3 toLight) {
    return light.colour * calculateDiffuseFactor(normal, light.intensity, toLight);
}

vec3 calculateToLightVector(Light light, vec3 position) {
    return light.directional ? light.position : light.position - position;
}

vec3 calculateLightColour(Light light, vec3 position, vec3 normal) {
    vec3 toLight = calculateToLightVector(light, position);

    // Attenuation factor
    float attenuation = calculateAttenuationFactor(light.attenuation, toLight);
    if (attenuation < 0.05) {
        return vec3(0);
    }

    // Ambient and Diffuse lighting
    toLight = normalize(toLight);
    vec3 ambientColour = calculateAmbientColour(light);
    vec3 diffuseColour = calculateDiffuseColour(light, normal, toLight);
    return (diffuseColour + ambientColour) * attenuation;
}

vec3 calcFinalLights(vec3 position, vec3 normal) {
    int length = lightsCount < MAX_LIGHTS ? lightsCount : MAX_LIGHTS;
    vec3 finalLight = vec3(0);
    for (int i = 0 ; i < length ; i++) {
        finalLight += calculateLightColour(lights[i], position, normal);
    }
    return finalLight;
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
    vec3 v_position = perspectiveDivide(homogeneousCoords);
    v_colour = colour * calcFinalLights(v_position, v_normal);

    vec3 toCamera = cameraPosition - v_position;
    float cameraDistance = length(toCamera);
    v_shadowHCoords = calculateShadowMapCoord(cameraDistance);
}