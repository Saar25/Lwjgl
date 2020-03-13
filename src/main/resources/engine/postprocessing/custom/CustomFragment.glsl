#version 400

out vec3 fragColour;

in vec2 texCoord;

uniform sampler2D albedo;

void main(void) {

    fragColour = 1 - texture(albedo, texCoord).rgb;

}