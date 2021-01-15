#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

struct Fog {
    float minDistance;
    float maxDistance;
    vec3 colour;
};

out vec3 fragColour;

in vec2 texCoord;

uniform sampler2D colourTexture;
uniform sampler2D depthTexture;
uniform Fog fog;

uniform mat4 projectionMatrixInv;


float calculateVisibility(float distance) {
    float factor = (fog.maxDistance - distance) / (fog.maxDistance - fog.minDistance);
    factor = clamp(factor, 0, 1);
    return smoothstep(0, 1, factor);
}

vec3 getViewPosition(float depth) {
    float z = depth * 2.0 - 1.0;

    vec4 clipSpacePosition = vec4(texCoord * 2.0 - 1.0, z, 1.0);
    vec4 viewSpacePosition = projectionMatrixInv * clipSpacePosition;

    return viewSpacePosition.rgb / viewSpacePosition.w;
}

void main(void) {
    float depth = sTexture(depthTexture, texCoord).r;
    float distance = length(getViewPosition(depth));
    float visibility = calculateVisibility(distance);
    vec3 colour = sTexture(colourTexture, texCoord).rgb;

    fragColour = mix(fog.colour, colour, visibility);
}
