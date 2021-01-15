/*
    WATER FRAGMENT SHADER
*/
#version 400 core

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

#define MAX_LIGHTS 10

const vec3 UP = vec3(0, 1, 0);

const float specularPower = 50.0;
const float reflectance = 0.50;

const float edgeSoftness = 10;

struct Light {
    vec3 position;
    vec3 colour;
    bool directional;
    vec3 attenuation;
    float intensity;
};

in vec3 v_position;
in vec2 v_texCoord;
in vec3 v_normal;
in vec4 v_clipSpace;
in vec3 v_toCameraVector;

out vec4 f_ALBEDO;
out vec3 f_NORMAL;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D depthTexture;
uniform int availableTextures;
uniform vec3 waterColour;

uniform Light lights[MAX_LIGHTS];
uniform int lightsCount;

uniform float farPlane;
uniform float nearPlane;

int getBit(int number, int bit) {
    return (number >> bit) & 1;
}

bool hasTexture(int unit) {
    return getBit(availableTextures, unit) == 1;
}

float calcAttenuationFactor(vec3 att, vec3 toLightVector) {
    float d = length(toLightVector);
    float attFactor = att.x + att.y * d + att.z * d * d;
    return 1 / attFactor;
}

float calcDiffuseFactor(vec3 toLight, float intensity) {
    float diffuseFactor = dot(toLight, v_normal);
    diffuseFactor = max(diffuseFactor, 0.0);
    return diffuseFactor * intensity;
}

float calcSpecularFactor(vec3 fromLight, vec3 toCamera, float reflectance) {
    vec3 reflected = normalize(reflect(fromLight, v_normal));
    float specularFactor = dot(reflected, toCamera);
    specularFactor = max(specularFactor, 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specularFactor = specularFactor * reflectance;
    return specularFactor;
}

float calcFresnelFactor(vec3 toCamera) {
    float fresnelFactor = dot(v_normal, toCamera);
    fresnelFactor = clamp(fresnelFactor, 0, 1);
    fresnelFactor = pow(fresnelFactor, reflectance);
    return fresnelFactor;
}

vec3 applyFresnelEffect(vec3 reflection, vec3 refraction) {
    float fresnel = calcFresnelFactor(normalize(v_toCameraVector));
    return mix(reflection, refraction, fresnel);
}

float toLinearDepth(float depth) {
    return 2.0 * nearPlane * farPlane / (farPlane + nearPlane -
    (2 * depth - 1) * (farPlane - nearPlane));
}

float calculateWaterDepth(vec2 texCoords) {
    if (hasTexture(2)) {
        float waterDistance = toLinearDepth(gl_FragCoord.z);
        float depth = sTexture(depthTexture, texCoords).r;
        float floorDistance = toLinearDepth(depth);
        return floorDistance - waterDistance;
    }
    return edgeSoftness;
}

vec3 calculateReflectionColour(vec2 ndc) {
    if (hasTexture(0)) {
        vec2 reflectionCoord = vec2(ndc.x, 1 - ndc.y);
        reflectionCoord = clamp(reflectionCoord, 0.001, 0.999);
        return sTexture(reflectionTexture, reflectionCoord).rgb;
    }
    return waterColour;
}

vec3 calculateRefractionColour(vec2 ndc) {
    if (hasTexture(1)) {
        vec2 refractionCoord = ndc;
        refractionCoord = clamp(refractionCoord, 0.001, 0.999);
        return sTexture(refractionTexture, refractionCoord).rgb;
    }
    return waterColour;
}

void main(void) {
    vec2 ndc = (v_clipSpace.xy / v_clipSpace.w) * 0.5 + 0.5;
    float depthFactor = calculateWaterDepth(ndc);

    vec3 reflectionColour = calculateReflectionColour(ndc);
    vec3 refractionColour = calculateRefractionColour(ndc);

    vec3 finalColour = applyFresnelEffect(reflectionColour, refractionColour);

    // Add light
    vec3 finalDiffuse = vec3(0);
    vec3 finalSpecular = vec3(0);
    for (int i = 0; i < lightsCount; i++) {
        Light light = lights[i];

        vec3 toLightVector = light.directional ? light.position : light.position - v_position;
        float attFactor = calcAttenuationFactor(light.attenuation, toLightVector);
        if (attFactor < 0.05) {
            continue;
        }
        toLightVector = normalize(toLightVector);

        // Specular light
        vec3 fromLight = -toLightVector;
        vec3 toCamera = normalize(v_toCameraVector);
        float specularFactor = calcSpecularFactor(fromLight, toCamera, clamp(depthFactor / 20, 0, 1));
        vec3 specularColour = light.colour * specularFactor;

        finalSpecular += attFactor * specularColour;
        finalDiffuse += calcDiffuseFactor(toLightVector, light.intensity);
    }

    // Add light, fog and soft edges to the water
    finalColour = finalColour * finalDiffuse;
    finalColour = finalColour + finalSpecular;

    float softEdge = clamp(depthFactor / edgeSoftness, 0, 1);
    f_ALBEDO = vec4(finalColour, softEdge);
    f_NORMAL = v_normal;
}
