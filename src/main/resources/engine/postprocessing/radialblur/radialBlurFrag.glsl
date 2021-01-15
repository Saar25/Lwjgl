#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

out vec3 fragColour;

in vec2 texCoord;

uniform sampler2D u_texture;
uniform vec2 center;
uniform int samples;
uniform float factor;

vec2 calculateOffset() {
    return (center - texCoord) / (samples * factor);
}

void main(void) {
    vec2 offset = calculateOffset();

    vec3 finalColour = vec3(0);
    for(int i = 0; i < samples; i++) {
        vec2 coords = clamp(texCoord + i * offset, 0, 1);
        finalColour += sTexture(u_texture, coords).rgb / samples;
    }
    fragColour = finalColour;
}
