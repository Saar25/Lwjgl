#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

const int pcf = 5; // Percentage Closer Filtering
const float pixels = pow(pcf * 2 + 1, 2);
const float pixelSize = 1 / 4096.0;
const float shadowTransition = 10;

out vec3 fragColour;

in vec2 texCoord;

uniform sampler2D colourTexture;
uniform sampler2D depthTexture;
uniform sampler2D shadowTexture;

uniform float shadowDistance;
uniform float shadowPower;
uniform float shadowBias;

uniform vec3 cameraPosition;
uniform mat4 sunSpaceMatrix;
uniform mat4 camProjMatrixInv;
uniform mat4 camViewMatrixInv;

vec3 getWorldPosition(float depth) {
    float z = depth * 2.0 - 1.0;

    vec4 clipSpacePosition = vec4(texCoord * 2.0 - 1.0, z, 1.0);
    vec4 viewSpacePosition = camProjMatrixInv * clipSpacePosition;

    viewSpacePosition /= viewSpacePosition.w;

    vec4 worldSpacePosition = camViewMatrixInv * viewSpacePosition;

    return worldSpacePosition.xyz / worldSpacePosition.w;
}

float calcShadowFactor(sampler2D shadowMap, vec4 shadowCoords) {
    if (shadowCoords.w < 0.1) {
        return 1;
    }
    float shadowFactor = 1;
    for (int i = -pcf; i <= pcf; i++){
        if (shadowCoords.z > sTexture(shadowMap, shadowCoords.xy + vec2(+i, +i) * pixelSize).r + shadowBias) {
            shadowFactor -= shadowPower * shadowCoords.w / pixels;
        }
        if (shadowCoords.z > sTexture(shadowMap, shadowCoords.xy + vec2(-i, +i) * pixelSize).r + shadowBias && i != 0) {
            shadowFactor -= shadowPower * shadowCoords.w / pixels;
        }
    }
    if (shadowFactor == 1 || shadowFactor == 0) {
        return shadowFactor;
    }
    for (int x = -pcf; x <= pcf; x++){
        for (int y = -pcf; y <= pcf; y++){
            if (x != y && y != -x && shadowCoords.z > sTexture(shadowMap, shadowCoords.xy + vec2(x, y) * pixelSize).r + shadowBias) {
                shadowFactor -= shadowPower * shadowCoords.w / pixels;
            }
        }
    }
    return shadowFactor;
}

void main(void) {
    vec3 colour = sTexture(colourTexture, texCoord).rgb;
    float depth = sTexture(depthTexture, texCoord).r;
    vec3 position = getWorldPosition(depth);

    float distance = distance(cameraPosition, position);

    vec4 shadowCoords  = sunSpaceMatrix * vec4(position, 1.0);
    shadowCoords.w = (distance - shadowDistance + shadowTransition) / shadowTransition;
    shadowCoords.w = clamp(1 - shadowCoords.w, 0, 1);

    float shadowFactor = calcShadowFactor(shadowTexture, shadowCoords);

    fragColour = colour * shadowFactor;
}
