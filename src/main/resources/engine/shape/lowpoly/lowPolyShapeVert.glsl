/*
    TERRAIN VERTEX SHADER
*/
#version 400 core

#define MAX_LIGHTS 10

const float shadowTransition = 10;

const float ambient = 0.3;
const float minLength = 0.5;
const float maxLength = 0.866025404; //length(vec3(.5));

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

    layout (location=0) in vec3 position;
    layout (location=1) in vec3 normal;

    flat out vec4 v_colour;
    out vec4 v_shadowHCoords;


/* ===================== */
/*                       */
/*   UNIFORM VARIABLES   */
/*                       */
/* ===================== */

    uniform mat4 transformation;
    uniform mat4 mvpMatrix;
    uniform vec4 clipPlane;

    uniform vec4 colour;

    uniform vec3 cameraPosition;
    uniform float shadowDistance;
    uniform mat4 shadowSpaceMatrix;

    uniform Light lights[MAX_LIGHTS];
    uniform int lightsCount;


/* ===================== */
/*                       */
/*   UTILITY FUNCTIONS   */
/*                       */
/* ===================== */

vec3 perspectiveDivide(vec4 v) {
    return v.xyz / v.w;
}

vec3 calculateNormal() {
    return normalize((transformation * vec4(normal, 0)).xyz);
}


/* ====================== */
/*                        */
/*   LIGHT CALCULATIONS   */
/*                        */
/* ====================== */

float calcAttenuationFactor(vec3 a, vec3 toLightVector) {
    float d = length(toLightVector);
    float attFactor = 1 / (a.x + a.y * d + a.z * d * d);
    return attFactor;
}

float calculateAmbientFactor(float power) {
    return power;
}

float calculateDiffuseFactor(vec3 normal, float lIntensity, vec3 toLight) {
    return max(dot(toLight, normal), 0.0) * lIntensity;
}

float calculateSpecularFactor(vec3 position, vec3 normal, vec3 fromLight, float sPower, float reflectance) {
    vec3 toCamera = normalize(cameraPosition - position);
    vec3 reflected = normalize(reflect(fromLight, normal));
    float specularFactor = dot(reflected, toCamera);
    specularFactor = max(specularFactor, 0.0);
    specularFactor = pow(specularFactor, sPower);
    return specularFactor * reflectance;
}

vec3 calculateSpecular(vec3 position, vec3 normal, Light light) {
    vec3 toLight = normalize(light.directional ? light.position : light.position - position);
    float reflectance = 1 - smoothstep(minLength, maxLength, length(position));
    return light.colour * calculateSpecularFactor(position, normal, -toLight, 50, reflectance);
}

vec3 calculateFinalSpecular(vec3 position, vec3 normal) {
    float length = min(lightsCount, MAX_LIGHTS);
    vec3 finalSpecular = vec3(0);
    for (int i = 0 ; i < length ; i++) {
        finalSpecular += calculateSpecular(position, normal, lights[i]);
    }
    return finalSpecular;
}

vec3 calculateLightColour(vec3 position, vec3 normal, Light light) {
    vec3 toLight = light.directional ? light.position : light.position - position;
    float attFactor = calcAttenuationFactor(light.attenuation, toLight);
    if (attFactor < 0.05) {
        return vec3(0);
    }
    toLight = normalize(toLight);
    vec3 ambientColour = light.colour * calculateAmbientFactor(ambient);
    vec3 diffuseColour = light.colour * calculateDiffuseFactor(normal, light.intensity, toLight);
    return (diffuseColour + ambientColour) * attFactor;
}

vec3 calculateFinalLights(vec3 position, vec3 normal) {
    float length = min(lightsCount, MAX_LIGHTS);
    vec3 finalLight = vec3(0);
    for (int i = 0 ; i < length ; i++) {
        finalLight += calculateLightColour(position, normal, lights[i]);
    }
    return finalLight;
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

    vec3 w_position = perspectiveDivide(homogeneous);

    float cameraDistance = distance(w_position, cameraPosition);
    v_shadowHCoords = calculateShadowMapCoord(cameraDistance);

    vec3 w_normal = calculateNormal();
    v_colour = colour;
    v_colour.rgb *= calculateFinalLights(w_position, w_normal);
    v_colour.rgb += calculateFinalSpecular(w_position, w_normal);
}