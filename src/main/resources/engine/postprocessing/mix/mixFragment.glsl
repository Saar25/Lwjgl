#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

out vec4 fragColour;

in vec2 texCoord;

uniform sampler2D texture1;
uniform sampler2D texture2;
uniform float scalar;

void main(void) {

    vec4 colour1 = sTexture(texture1, texCoord) * scalar;
    vec4 colour2 = sTexture(texture2, texCoord) * (1 - scalar);
    fragColour = colour1 + colour2;
}
