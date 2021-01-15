#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

out vec3 fragColour;

in vec2 texCoord;

uniform sampler2D albedo;

void main(void) {

    fragColour = 1 - sTexture(albedo, texCoord).rgb;

}