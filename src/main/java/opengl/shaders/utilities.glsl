//CONSTANTS
#define MAX_LIGHTS 10

//STRUCTS

//Light:
struct Light {
    vec3 position;
    vec3 colour;
    vec3 attenuation;
    bool directional;
    float intensity;
};

//Fog:
struct Fog {
    float density;
    float gradient;
    vec3 colour;
};

//METHODS

//++
float visibility(Fog fog, float cameraDistance) {
    return exp(-pow(cameraDistance * fog.density, fog.gradient));
}

//++
vec3 transformNormal(mat4 transformation, vec3 normal) {
    return normalize(transformation * vec4(normal, 0.0)).xyz;
}

//++
vec4 shadowCoords(mat4 shadowSpaceMatrix, vec3 position, float distance, float transition) {
    vec4 shadowMapCoord = shadowSpaceMatrix * vec4(position, 1.0);
    shadowMapCoord.w  = cameraDistance - shadowDistance + shadowTransition;
    shadowMapCoord.w  = clamp(1 - shadowMapCoord.w / shadowTransition, 0, 1);
    return shadowMapCoord;
}

//++
vec4 intToColour(uint colour) {
    float r = ((colour << 0 ) >> 24);
    float g = ((colour << 8 ) >> 24);
    float b = ((colour << 16) >> 24);
    float a = ((colour << 24) >> 24);
    return vec4(r, g, b, a) / 255;
}

//++
float calcSpecularFactor(vec3 fromLight, vec3 toCamera, vec3 normal, float power, float reflectance) {
    vec3 reflected = normalize(reflect(fromLight, normal));
    float specularFactor = dot(reflected, toCamera);
    specularFactor = max(specularFactor, 0.0);
    specularFactor = pow(specularFactor, power);
    return specularFactor * reflectance;
}

//++
float calcDiffuseFactor(vec3 toLight, float intensity, vec3 normal) {
    float diffuseFactor = dot(toLight, normal);
    return max(diffuseFactor, 0.0) * intensity;
}