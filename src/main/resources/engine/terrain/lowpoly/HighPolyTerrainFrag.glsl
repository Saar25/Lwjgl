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

    in vec3 v_colour;
    in vec3 v_normal;
    in vec3 v_position;
    in vec4 v_shadowHCoords;

    layout (location = 0) out vec4 f_ALBEDO;
    layout (location = 1) out vec4 f_NORMAL;
    layout (location = 2) out vec4 f_POSITION;


/* ===================== */
/*                       */
/*   UNIFORM VARIABLES   */
/*                       */
/* ===================== */

    uniform sampler2D shadowMap;
    uniform bool enableShadows;
    uniform float shadowPower;
    uniform float shadowBias;

    uniform Light lights[MAX_LIGHTS];
    uniform int lightsCount;


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
    // Shadow mapping
    float shadowFactor = enableShadows ? calcShadowFactor() : 1;

    // Low poly colour
    vec3 colour = v_colour * calcFinalLights(v_position, v_normal) * shadowFactor;

    // Final colour
    f_ALBEDO = vec4(colour, 1);
    f_NORMAL = vec4(v_normal, 1);
    f_POSITION = vec4(v_position, 1);
}