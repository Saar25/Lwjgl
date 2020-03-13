#version 400

out vec4 fragColour;

in vec2 texCoord;

uniform sampler2D texture1;
uniform sampler2D texture2;
uniform float scalar1;
uniform float scalar2;

void main(void) {
    vec4 colour1 = texture(texture1, texCoord);
    vec4 colour2 = texture(texture2, texCoord);
    fragColour = colour1 * scalar1 + colour2 * scalar2;
}
