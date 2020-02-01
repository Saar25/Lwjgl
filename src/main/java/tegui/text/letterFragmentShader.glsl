#version 400

in vec2 texCoords;

out vec4 fragColour;

uniform vec2 mapSize;
uniform sampler2D asciiMap;

void main(void) {
    float a = texture(asciiMap, texCoords).a;
    if (a > 0.5) {
        fragColour = vec4(0.0, 0.0, 0.0, a);
    }
    else {
        fragColour = vec4(1.0, 1.0, 1.0, a);
    }
}
