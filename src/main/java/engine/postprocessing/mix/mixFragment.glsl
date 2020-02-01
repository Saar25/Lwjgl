#version 400

out vec4 fragColour;

in vec2 texCoord;

uniform sampler2D texture1;
uniform sampler2D texture2;
uniform float scalar;

void main(void) {

    vec4 colour1 = texture(texture1, texCoord) * scalar;
    vec4 colour2 = texture(texture2, texCoord) * (1 - scalar);
    fragColour = colour1 + colour2;
}
