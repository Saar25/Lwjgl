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
#define HEIGHT_BIOMES 3

const int pcf = 3; // Percentage Closer Filtering
const float pixels = pow(pcf * 2 + 1, 2);
const float pixelSize = 1 / 8192.0;

const vec3 colour1 = vec3(.76, .69, .50);
const vec3 colour2 = vec3(0.5, 0.8, 0.0);
const vec3 colour3 = vec3(.56, .42, .25);
const vec3 colour4 = vec3(1.0, 1.0, 1.0);

struct HeightBiome {
    vec3 colour;
    float height;
};

const HeightBiome[HEIGHT_BIOMES] heightBiomes = HeightBiome[HEIGHT_BIOMES](
    HeightBiome(vec3(.76, .69, .50), -5), // sand
    HeightBiome(vec3(0.5, 0.8, 0.0), 8 ), // grass
    HeightBiome(vec3(1.0, 1.0, 1.0), 35)  // snow
    //HeightBiome(vec3(.56, .42, .25), -20)  // mud
);

struct Light {
    vec3 position;
    vec3 colour;
    vec3 attenuation;
    bool directional;
    float intensity;
};

smooth in vec3 smthNormal;
flat   in vec3 flatNormal;

in vec3 worldPosition;
in vec2 outTexCoord;
in vec3 toCameraVector;
in vec4 shadowMapCoords;

out vec4 fragColour;

uniform sampler2D u_texture;
uniform sampler2D shadowMap;
uniform bool multiTexturing;
uniform bool enableShadows;
uniform float shadowPower;
uniform float shadowBias;
uniform bool flatShading;

uniform Light lights[MAX_LIGHTS];
uniform int lightsCount;

float calcAttenuationFactor(vec3 attenuation, vec3 toLightVector) {
    float d = length(toLightVector);
    float attFactor = 1 / (attenuation.x + attenuation.y * d + attenuation.z * d * d);
    return attFactor;
}

float calcAmbientFactor(float power) {
    return power;
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

vec3 calcLightColour(Light light, vec3 position, vec3 normal, float ambient) {
    vec3 toLight = light.directional ? light.position : light.position - position;
    float attFactor = calcAttenuationFactor(light.attenuation, toLight);
    if (attFactor < 0.05) {
        return vec3(0);
    }
    toLight = normalize(toLight);
    vec3 ambientColour = light.colour * calcAmbientFactor(ambient);
    vec3 diffuseColour = light.colour * calcDiffuseFactor(light.intensity, toLight, position, normal);
    return (diffuseColour + ambientColour) * attFactor;
}

vec3 calcFinalLights(Light[MAX_LIGHTS] light, int lightsCount,
                        vec3 position, vec3 normal, float shadowFactor, float ambient) {
    float length = lightsCount < MAX_LIGHTS ? lightsCount : MAX_LIGHTS;
    vec3 finalLight = vec3(0);
    for (int i = 0 ; i < length ; i++) {
        finalLight += calcLightColour(lights[i], position, normal, ambient) * (i == length - 1 ? shadowFactor : 1);
    }
    return finalLight;
}

vec4 calcTextureColour(float tiling) {
    return sTexture(u_texture, outTexCoord * tiling);
}

vec4 calcLowPolyColour(float height) {
    vec3 result = heightBiomes[0].colour;
    for (int i = 1 ; i < HEIGHT_BIOMES ; i++) {
        float h1 = heightBiomes[i].height;
        float h2 = heightBiomes[i - 1].height;
        float factor = clamp((height - h2) / (h1 - h2), 0, 1);
        result = mix(result, heightBiomes[i].colour, pow(factor, 2));
    }
    return vec4(result, 1);
}

float calcShadowFactor(sampler2D shadowMap, vec4 shadowMapCoords) {
    if (shadowMapCoords.w < 0.1) {
        return 1;
    }
    float shadowFactor = 1;
    for (int x = -pcf; x <= pcf; x++){
        if (shadowMapCoords.z > sTexture(shadowMap, shadowMapCoords.xy + vec2(x, x) * pixelSize).r + shadowBias) {
            shadowFactor -= shadowPower * shadowMapCoords.w / pixels;
        }
    }
    for (int y = -pcf; y <= pcf; y++){
        if (shadowMapCoords.z > sTexture(shadowMap, shadowMapCoords.xy + vec2(-y, y) * pixelSize).r + shadowBias) {
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

void main() {

    vec4 colour = multiTexturing
            ? calcTextureColour(40)
            : calcLowPolyColour(worldPosition.y);

    // Shadow mapping
    float shadowFactor = !enableShadows ? 1 : calcShadowFactor(shadowMap, shadowMapCoords);

    // Light
    vec3 normal = flatShading ? flatNormal : smthNormal;
    vec3 finalLight = calcFinalLights(lights, lightsCount, worldPosition, normal, shadowFactor, 0.2);
    colour *= vec4(finalLight, 1.0);

    fragColour = colour;
}