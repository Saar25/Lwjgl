#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

const int levels = 11;
const int hlevels = levels / 2;
const float[] blurLevels =
    {0.0093, 0.028002, 0.065984, 0.121703, 0.175713, 0.198596, 0.175713, 0.121703, 0.065984, 0.028002, 0.0093};
    //{1.0/levels, 1.0/levels, 1.0/levels, 1.0/levels, 1.0/levels, 1.0/levels, 1.0/levels, 1.0/levels, 1.0/levels, 1.0/levels, 1.0/levels};

out vec4 fragColour;

in vec2 texCoord;

uniform sampler2D u_texture;
uniform bool verticalBlur;
uniform int width;
uniform int height;

vec4 doVerticalBlur() {
    vec4 colour = vec4(0);
    float offset = 1.0 / width;
    for (int x = -hlevels ; x <= hlevels ; x++) {
        vec2 coord = texCoord + vec2(x * offset, 0);
        colour += blurLevels[x + hlevels] * sTexture(u_texture, coord);
    }
    return colour;
}

vec4 doHorizontalBlur() {
    vec4 colour = vec4(0);
    float offset = 1.0 / height;
    for (int y = -hlevels ; y <= hlevels ; y++) {
        vec2 coord = texCoord + vec2(0, y * offset);
        colour += blurLevels[y + hlevels] * sTexture(u_texture, coord);
    }
    return colour;
}

void main(void) {

    if (verticalBlur) {
        fragColour = doVerticalBlur();
    } else {
        fragColour = doHorizontalBlur();
    }

}
