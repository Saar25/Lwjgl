#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

out vec3 fragColour;

in vec2 texCoord;

uniform sampler2D u_texture;
uniform float exposure;
uniform float gamma;

void main(void) {
    fragColour = sTexture(u_texture, texCoord).rgb;

    fragColour = vec3(1.0) - exp(-fragColour * exposure);
    fragColour = pow(fragColour, vec3(1.0 / gamma));
}
