#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

out vec4 fragColour;

in vec2 texCoord;

uniform sampler2D texture;
uniform float radius2;
uniform vec2 center;

float distance2() {
    return pow(center.x - texCoord.x, 2) + pow(center.y - texCoord.y, 2);
}

bool checkDistance() {
    return distance2() <= radius2;
}

void main(void) {
    if (checkDistance() && sTexture(texture, texCoord).r == 1) {
        fragColour = vec4(1);
    } else {
        fragColour = vec4(0);
    }
}
