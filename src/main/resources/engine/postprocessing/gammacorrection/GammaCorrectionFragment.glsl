#version 400

out vec3 fragColour;

in vec2 texCoord;

uniform sampler2D texture;
uniform float factor;

void main(void) {
    fragColour = texture(texture, texCoord).rgb;

    fragColour = pow(fragColour, vec3(1 / factor));
}
