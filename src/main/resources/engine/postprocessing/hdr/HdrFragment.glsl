#version 400

out vec3 fragColour;

in vec2 texCoord;

uniform sampler2D texture;
uniform float exposure;
uniform float gamma;

void main(void) {
    fragColour = texture(texture, texCoord).rgb;

    fragColour = vec3(1.0) - exp(-fragColour * exposure);
    fragColour = pow(fragColour, vec3(1.0 / gamma));
}
