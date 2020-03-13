#version 400

out vec4 fragColour;

in vec2 texCoord;

uniform sampler2D texture;
uniform float factor;

void main(void) {
    fragColour = texture(texture, texCoord);

    fragColour.rgb = (fragColour.rgb - .5) * factor + .5;

}
