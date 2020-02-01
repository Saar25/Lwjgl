#version 400 core

#define MAX_LIGHTS 10

const float PI = 3.14159265358979323846;
const float specularPower = 50.0;
const float reflectance = 0.50;
const float tiling = 15;

struct Light {
    vec3 position;
    vec3 colour;
    bool directional;
    vec3 attenuation;
    float intensity;
};

layout (location = 0) in vec2 in_position;
layout (location = 1) in vec2 in_position1;
layout (location = 2) in vec2 in_position2;

out vec3 v_position;
out vec2 v_texCoord;
out vec3 v_normal;
out vec4 v_clipSpace;
out vec3 v_toCamera;
out vec3 v_toCameraVector;

flat out vec3 v_diffuseColour;
flat out vec3 v_specularColour;
flat out float v_fresnelFactor;

uniform mat4 projectionViewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 in_cameraPosition;
uniform vec4 clipPlane;

uniform float amplitude;
uniform float time;

uniform Light lights[MAX_LIGHTS];
uniform int lightsCount;

float noise(vec2 position) {
    float val1 = cos(position.x * .4297 + time) * sin(position.y * .2347);
    float val2 = sin(.5 * time + (3 * position.x + position.y));
    return (val1 + val2) * amplitude;
}

vec3 getNormal(vec2 p1, vec2 p2, vec2 p3) {
    vec3 p11 = vec3(p1.x, noise(p1), p1.y);
    vec3 p12 = vec3(p2.x, noise(p2), p2.y);
    vec3 p13 = vec3(p3.x, noise(p3), p3.y);
    vec3 v1 = normalize(p11 - p12);
    vec3 v2 = normalize(p13 - p12);
    return cross(v1, v2);
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
    return pow(fresnelFactor, reflectance);
}

void main(void) {
    float height = noise(in_position);
    vec3 position = vec3(in_position.x, 0, in_position.y);
    vec3 distortedPosition = vec3(in_position.x, height, in_position.y);

    vec4 homogeneousCoords = transformationMatrix * vec4(position, 1.0);
    vec4 distortedHCoords = transformationMatrix * vec4(distortedPosition, 1.0);

    gl_Position = projectionViewMatrix * distortedHCoords;
    v_clipSpace = projectionViewMatrix * homogeneousCoords;

    gl_ClipDistance[0] = dot(distortedHCoords, clipPlane);

    v_texCoord = in_position * tiling;
    v_normal = getNormal(in_position, in_position1, in_position2);
    v_position = distortedHCoords.xyz / distortedHCoords.w;
    v_toCameraVector = in_cameraPosition - v_position;
    v_toCamera = normalize(v_toCameraVector);

    v_specularColour = vec3(0);
    v_diffuseColour = vec3(0);
    for (int i = 0; i < lightsCount; i++) {
        Light light = lights[i];

        vec3 toLightVector = light.directional ? light.position : light.position - v_position;
        float attFactor = calcAttenuationFactor(light.attenuation, toLightVector);
        if (attFactor < 0.05) {
            continue;
        }

        vec3 toLight = normalize(toLightVector);
        vec3 fromLight = -toLight;

        float specularFactor = calcSpecularFactor(fromLight, v_toCamera, .8);
        vec3 specularColour = light.colour * specularFactor;

        v_specularColour += attFactor * specularColour;
        v_diffuseColour += calcDiffuseFactor(toLight, light.intensity);
    }

    v_fresnelFactor = calcFresnelFactor(v_toCamera);

}