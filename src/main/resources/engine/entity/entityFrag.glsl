/*
    ENTITY FRAGMENT SHADER
*/
#version 400 core

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

#define MAX_LIGHTS 10
const int pcf = 5; // Percentage Closer Filtering
const float pixels = pow(pcf * 2 + 1, 2);
const float pixelSize = 1 / 8096.0;

struct Light {
    vec3 position;
    vec3 colour;
    vec3 attenuation;
    bool directional;
    float intensity;
};

in vec3 v_position;
in vec2 v_texCoord;
in vec3 v_normal;
in vec3 v_reflected;
in vec3 v_camPos;
in vec3 v_viewPosition;
in vec4 shadowMapCoords;

layout (location = 0) out vec4 f_colour;
layout (location = 1) out vec4 f_normal;
layout (location = 2) out vec4 f_position;

uniform sampler2D u_texture;
uniform float specularPower;
uniform Light lights[MAX_LIGHTS];
uniform int lightsCount;

uniform float shadowPower;
uniform float shadowBias;
uniform bool enableShadows;
uniform sampler2D shadowMap;

float calcAttenuationFactor(vec3 attenuation, vec3 toLightVector) {
    float d = length(toLightVector);
    float attFactor = 1 / (attenuation.x + attenuation.y * d + attenuation.z * d * d);
    return attFactor;
}

float calcAmbientFactor() {
    return 0.2;
}

float calcDiffuseFactor(float lIntensity, vec3 toLight, vec3 normal) {
    float diffuseFactor = dot(toLight, normal);
    diffuseFactor = max(diffuseFactor, 0.0) * lIntensity;
    return diffuseFactor;
}

float calcSpecularFactor(vec3 toLight, vec3 reflected, vec3 normal, float sPower, float reflectance) {
    float specularFactor = dot(reflected, toLight);
    specularFactor = max(specularFactor, 0.0);
    specularFactor = pow(specularFactor, sPower);
    specularFactor = specularFactor * reflectance;
    return specularFactor;
}

vec3 calcFinalLight(Light light, float specularPower, float reflectance, float shadowFactor) {
    vec3 toLightVector = light.position - v_position;
    float attFactor = calcAttenuationFactor(light.attenuation, toLightVector);
    if (attFactor < 0.05) {
        return vec3(0);
    }

    vec3 toLight = normalize(light.directional ? light.position : toLightVector);

    vec3 ambientColour  = light.colour * calcAmbientFactor();
    vec3 diffuseColour  = light.colour * calcDiffuseFactor(light.intensity, toLight, v_normal);
    vec3 specularColour = light.colour * calcSpecularFactor(-toLight, v_reflected, v_normal, specularPower, reflectance);

    vec3 finalLight = (ambientColour + diffuseColour  + specularColour * shadowFactor) * attFactor * shadowFactor;
    return finalLight;
}

vec3 calculateLightColour(float shadowFactor) {
    vec3 finalLight = vec3(0);
    int lMin = min(MAX_LIGHTS, lightsCount);
    for(int i = 0 ; i < lMin ; i++) {
        Light light = lights[i];
        finalLight = finalLight + calcFinalLight(light, specularPower, 1, shadowFactor);
    }
    return finalLight;
}

float calcShadowFactor(sampler2D shadowMap, vec4 shadowMapCoords) {
    if (!enableShadows || shadowMapCoords.w < 0.1) {
        return 1;
    }
    float shadowFactor = 1;
    for (int i = -pcf; i <= pcf; i++){
        if (shadowMapCoords.z > sTexture(shadowMap, shadowMapCoords.xy + vec2(+i, +i) * pixelSize).r + shadowBias) {
            shadowFactor -= shadowPower * shadowMapCoords.w / pixels;
        }
        if (shadowMapCoords.z > sTexture(shadowMap, shadowMapCoords.xy + vec2(-i, +i) * pixelSize).r + shadowBias && i != 0) {
            shadowFactor -= shadowPower * shadowMapCoords.w / pixels;
        }
    }
    if (shadowFactor == 1 || shadowFactor == 0) {
        return shadowFactor;
    }
    for (int x = -pcf; x <= pcf; x++){
        for (int y = -pcf; y <= pcf; y++){
            if (x != y && y != -x && shadowMapCoords.z > sTexture(shadowMap, shadowMapCoords.xy + vec2(x, y) * pixelSize).r + shadowBias) {
                shadowFactor -= shadowPower * shadowMapCoords.w / pixels;
            }
        }
    }
    return shadowFactor;
}

vec4 calculateColour() {
    return sTexture(u_texture, v_texCoord);
}

void main() {
    vec4 colour = calculateColour();
    if (colour.a < .6) {
        discard;
    }

    float shadowFactor = calcShadowFactor(shadowMap, shadowMapCoords);

    vec3 finalLight = calculateLightColour(shadowFactor);

    f_colour = vec4(finalLight, 1.0) * colour;
    f_normal = vec4(v_normal, 1);
    f_position = vec4(v_viewPosition, 1);
}