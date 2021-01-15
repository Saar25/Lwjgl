#version 400 core

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

in vec2 texCoord;

out float fragDepth;

uniform sampler2D texture;

void main() {

    if (sTexture(texture, texCoord).a < .1) {
        discard;
    }

    fragDepth = gl_FragCoord.z;
}
