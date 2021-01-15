/*
    TERRAIN FRAGMENT SHADER
*/
#version 400 core

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

#define MAX_LIGHTS 10

const int pcf = 3; // Percentage Closer Filtering
const float pixels = pow(pcf * 2 + 1, 2);
const float pixelSize = 1 / 8192.0;

const float ambient = 0.3;
const float minLength = 0.5;
const float maxLength = length(vec3(.5));

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

    in vec3 v_spacePosition;
    in vec3 v_position;
    in vec3 v_normal;
    in vec4 v_shadowHCoords;

    out vec4 fragColour;


/* ===================== */
/*                       */
/*   UNIFORM VARIABLES   */
/*                       */
/* ===================== */

    uniform vec3 colour;

    uniform vec3 cameraPosition;
    uniform Light lights[MAX_LIGHTS];
    uniform int lightsCount;

    uniform sampler2D shadowMap;
    uniform bool enableShadows;
    uniform float shadowPower;
    uniform float shadowBias;

/**
* Returns the attenuation factor
*
* @return the attenuation factor
*/
float calcAttenuationFactor(vec3 attenuation, vec3 toLightVector) {
    float d = length(toLightVector);
    float attFactor = 1 / (attenuation.x + attenuation.y * d + attenuation.z * d * d);
    return attFactor;
}

float calculateAmbientFactor(float power) {
    return power;
}

float calculateDiffuseFactor(float lIntensity, vec3 toLight) {
    return max(dot(toLight, v_normal), 0.0) * lIntensity;
}

float calculateSpecularFactor(vec3 fromLight, float sPower, float reflectance) {
    vec3 toCamera = normalize(cameraPosition - v_spacePosition);
    vec3 reflected = normalize(reflect(fromLight, v_normal));
    float specularFactor = dot(reflected, toCamera);
    specularFactor = max(specularFactor, 0.0);
    specularFactor = pow(specularFactor, sPower);
    return specularFactor * reflectance;
}

vec3 calculateSpecular(Light light) {
    vec3 toLight = normalize(light.directional ? light.position : light.position - v_spacePosition);
    float reflectance = 1 - smoothstep(minLength, maxLength, length(v_position));
    return light.colour * calculateSpecularFactor(-toLight, 50, reflectance);
}

vec3 calculateFinalSpecular() {
    float length = min(lightsCount, MAX_LIGHTS);
    vec3 finalSpecular = vec3(0);
    for (int i = 0 ; i < length ; i++) {
        finalSpecular += calculateSpecular(lights[i]);
    }
    return finalSpecular;
}

vec3 calculateLightColour(Light light) {
    vec3 toLight = light.directional ? light.position : light.position - v_spacePosition;
    float attFactor = calcAttenuationFactor(light.attenuation, toLight);
    if (attFactor < 0.05) {
        return vec3(0);
    }
    toLight = normalize(toLight);
    vec3 ambientColour = light.colour * calculateAmbientFactor(ambient);
    vec3 diffuseColour = light.colour * calculateDiffuseFactor(light.intensity, toLight);
    return (diffuseColour + ambientColour) * attFactor;
}

vec3 calculateFinalLights() {
    float length = min(lightsCount, MAX_LIGHTS);
    vec3 finalLight = vec3(0);
    for (int i = 0 ; i < length ; i++) {
        finalLight += calculateLightColour(lights[i]);
    }
    return finalLight;
}


/* ======================== */
/*                          */
/*   SHADOWS CALCULATIONS   */
/*                          */
/* ======================== */

// Vertex   variables : v_shadowHCoords
// Uniform  variables : shadowPower, shadowBias
// Constant variables : pixels, pixelSize, pcf
float calcShadowFactor() {
    if (v_shadowHCoords.w < 0.1) {
        return 1;
    }
    float shadowFactor = 1;
    for (int x = -pcf; x <= pcf; x++){
        if (v_shadowHCoords.z > sTexture(shadowMap, v_shadowHCoords.xy + vec2(x, x) * pixelSize).r + shadowBias) {
            shadowFactor -= shadowPower * v_shadowHCoords.w / pixels;
        }
    }
    for (int y = -pcf; y <= pcf; y++){
        if (v_shadowHCoords.z > sTexture(shadowMap, v_shadowHCoords.xy + vec2(-y, y) * pixelSize).r + shadowBias) {
            shadowFactor -= shadowPower * v_shadowHCoords.w / pixels;
        }
    }
    if (shadowFactor == 1 || shadowFactor == 0) {
        return shadowFactor;
    }
    for (int x = -pcf; x <= pcf; x++){
        for (int y = -pcf; y <= pcf; y++){
            if (x != y && y != -x && v_shadowHCoords.z > sTexture(shadowMap, v_shadowHCoords.xy + vec2(x, y) * pixelSize).r + shadowBias) {
                shadowFactor -= shadowPower * v_shadowHCoords.w / pixels;
            }
        }
    }
    return shadowFactor;
}


/* =============== */
/*                 */
/*   MAIN METHOD   */
/*                 */
/* =============== */

void main(void) {

    vec3 f_colour = colour;

    // Light
    f_colour *= calculateFinalLights();
    f_colour += calculateFinalSpecular();
    f_colour *= enableShadows ? calcShadowFactor() : 1;

    fragColour = vec4(f_colour, 1);
}