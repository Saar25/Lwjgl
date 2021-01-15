/*
    WATER FRAGMENT SHADER
*/
#version 400 core

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

const float edgeSoftness = 10;

in vec3 v_position;
in vec2 v_texCoord;
in vec3 v_normal;
in vec4 v_clipSpace;
in vec3 v_toCamera;
in vec3 v_toCameraVector;

flat in vec3 v_diffuseColour;
flat in vec3 v_specularColour;
flat in float v_fresnelFactor;

layout (location = 0) out vec4 f_ALBEDO;
layout (location = 1) out vec4 f_NORMAL;
layout (location = 2) out vec4 f_POSITION;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D depthTexture;
uniform int availableTextures;
uniform vec3 waterColour;

uniform float farPlane;
uniform float nearPlane;

int getBit(int number, int bit) {
    return (number >> bit) & 1;
}

bool hasTexture(int unit) {
    return getBit(availableTextures, unit) == 1;
}

float calcFresnelFactor(vec3 toCamera) {
    return v_fresnelFactor;
}

vec3 applyFresnelEffect(vec3 reflection, vec3 refraction) {
    float fresnel = calcFresnelFactor(v_toCamera);
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

    finalColour = finalColour * v_diffuseColour;
    finalColour = finalColour + v_specularColour;

    float softEdge = clamp(depthFactor / edgeSoftness, 0, 1);
    f_ALBEDO = vec4(finalColour, softEdge);
    f_NORMAL = vec4(v_normal, 1.0);
    f_POSITION = vec4(v_position, 1.0);

}
