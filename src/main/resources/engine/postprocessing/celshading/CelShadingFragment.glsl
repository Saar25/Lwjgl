#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

const int levels = 4;

out vec3 fragColour;

in vec2 texCoord;

uniform sampler2D u_texture;

float getBrightness(vec3 colour) {
    //return (colour.x + colour.y + colour.z) / 3;
    return 2.2 / exp(length(colour));
}

void main(void) {
    /*
    vec3 colour = sTexture(u_texture, texCoord).rgb;
    float brightness = getBrightness(colour);

    brightness = brightness * levels;
    brightness = int(brightness) + 1;
    brightness = brightness / levels;
    colour *= brightness;

    fragColour = colour;*/

    vec3 colour = sTexture(u_texture, texCoord).rgb;
    colour *= levels;
    colour.x = int(colour.x) + .5;
    colour.y = int(colour.y) + .5;
    colour.z = int(colour.z) + .5;
    colour /= levels;
    fragColour = colour;
}
