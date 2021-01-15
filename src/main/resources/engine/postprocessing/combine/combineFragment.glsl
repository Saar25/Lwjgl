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
uniform float scalar1;
uniform float scalar2;

void main(void) {
    vec4 colour1 = sTexture(texture1, texCoord);
    vec4 colour2 = sTexture(texture2, texCoord);
    fragColour = colour1 * scalar1 + colour2 * scalar2;
}
