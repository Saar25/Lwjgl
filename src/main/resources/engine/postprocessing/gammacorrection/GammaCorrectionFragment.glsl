#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

out vec3 fragColour;

in vec2 texCoord;

uniform sampler2D texture;
uniform float factor;

void main(void) {
    fragColour = sTexture(texture, texCoord).rgb;

    fragColour = pow(fragColour, vec3(1 / factor));
}
