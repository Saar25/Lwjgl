/*
    SKY BOX FRAGMENT SHADER
*/
#version 400 core

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

const float skyFactor = 0.4;
const float fogTransition = 0.1;
const float fogStart = 0.01;

in vec3 texCoord;

out vec4 fragColour;

uniform samplerCube cubeMap;
uniform vec3 skyColour;
uniform vec4 fogColour;

float calculateFogFactor() {
    float factor = (texCoord.y - fogStart) / fogTransition;
    return clamp((1 - factor) * fogColour.a, 0, 1);
}

void main() {
    fragColour = sTexture(cubeMap, texCoord);

    float fogFactor = calculateFogFactor();
    fragColour = mix(fragColour, vec4(skyColour, 1), skyFactor);
    fragColour = mix(fragColour, vec4(fogColour.rgb, 1), fogFactor);

}
