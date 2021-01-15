#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

out vec4 fragColour;

in vec2 texCoord1;
in vec2 texCoord2;
in float blend;

uniform sampler2D texture;

void main(void) {
    vec4 colour1 = sTexture(texture, texCoord1);
    vec4 colour2 = sTexture(texture, texCoord2);
    fragColour = mix(colour1, colour2, blend);
}
