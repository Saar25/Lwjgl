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

struct Light {
    vec3 position;
    vec3 colour;
    vec3 attenuation;
    bool directional;
    float intensity;
};

in vec3 v_normal;

in vec3 worldPosition;
in vec2 v_texCoords;
in vec3 toCameraVector;
in vec4 v_shadowHCoords;
in vec3 v_viewPosition;

layout (location = 0) out vec4 f_ALBEDO;
layout (location = 1) out vec3 f_NORMAL;
layout (location = 2) out vec3 f_POSITION;

uniform sampler2D blendMap;
uniform sampler2D dTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;

uniform sampler2D shadowMap;
uniform bool enableShadows;
uniform float shadowPower;
uniform float shadowBias;
uniform int tiling;

uniform Light lights[MAX_LIGHTS];
uniform int lightsCount;

float calcAttenuationFactor(vec3 attenuation, vec3 toLightVector) {
    float d = length(toLightVector);
    float attFactor = 1 / (attenuation.x + attenuation.y * d + attenuation.z * d * d);
    return attFactor;
}

float calcAmbientFactor() {
    return ambient;
}

float calcDiffuseFactor(float lIntensity, vec3 toLight, vec3 position, vec3 normal) {
    float diffuseFactor = dot(toLight, normal);
    diffuseFactor = max(diffuseFactor, 0.0) * lIntensity;
    return diffuseFactor;
}

float calcSpecularFactor(vec3 fromLight, vec3 toCamera, vec3 normal, float sPower, float reflectance) {
    vec3 reflected = normalize(reflect(fromLight, normal));
    float specularFactor = dot(reflected, toCamera);
    specularFactor = max(specularFactor, 0.0);
    specularFactor = pow(specularFactor, sPower);
    specularFactor = specularFactor * reflectance;
    return specularFactor;
}

vec3 calcLightColour(Light light, vec3 position, vec3 normal) {
    vec3 toLight = light.directional ? light.position : light.position - position;
    float attFactor = calcAttenuationFactor(light.attenuation, toLight);
    if (attFactor < 0.05) {
        return vec3(0);
    }
    toLight = normalize(toLight);
    vec3 ambientColour = light.colour * calcAmbientFactor();
    vec3 diffuseColour = light.colour * calcDiffuseFactor(light.intensity, toLight, position, normal);
    return (diffuseColour + ambientColour) * attFactor;
}

vec3 calcFinalLights(Light[MAX_LIGHTS] light, int lightsCount,
                        vec3 position, vec3 normal, float shadowFactor) {
    float length = lightsCount < MAX_LIGHTS ? lightsCount : MAX_LIGHTS;
    vec3 finalLight = vec3(0);
    for (int i = 0 ; i < length ; i++) {
        finalLight += calcLightColour(lights[i], position, normal) * (i == length - 1 ? shadowFactor : 1);
    }
    return finalLight;
}

vec4 calculateColour(vec2 blendTexCoords, vec2 texCoords) {
    vec4 blend = sTexture(blendMap , blendTexCoords);
    float dFactor = (1 - (blend.r + blend.g + blend.b));
    vec4 rColour = blend.r == 0 ? vec4(0) : blend.r * sTexture(rTexture, texCoords); // if texture is not shown on
    vec4 gColour = blend.g == 0 ? vec4(0) : blend.g * sTexture(gTexture, texCoords); // the blend map then texture
    vec4 bColour = blend.b == 0 ? vec4(0) : blend.b * sTexture(bTexture, texCoords); // sampling is useless and
    vec4 dColour = dFactor == 0 ? vec4(0) : dFactor * sTexture(dTexture, texCoords); // will surely be zero
    return dColour + rColour + gColour + bColour;
}

float calculateShadowFactor() {
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

void main() {

    vec4 colour = calculateColour(v_texCoords, v_texCoords * tiling);

    // Shadow mapping
    float shadowFactor = enableShadows ? calculateShadowFactor() : 1;

    // Light
    vec3 finalLight = calcFinalLights(lights, lightsCount, worldPosition, v_normal, shadowFactor);
    colour *= vec4(finalLight, 1.0);

    f_ALBEDO = colour;
    f_NORMAL = v_normal;
    f_POSITION = v_viewPosition;
}