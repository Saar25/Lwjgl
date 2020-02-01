#version 400

out vec4 fragColour;

uniform uint colour;

vec4 getColour() {
    float r = ((colour << 0 ) >> 24);
    float g = ((colour << 8 ) >> 24);
    float b = ((colour << 16) >> 24);
    float a = ((colour << 24) >> 24);
    return vec4(r, g, b, a) / 255;
}

void main(void) {
    fragColour = getColour();

}
