#version 400

out vec4 fragColour;

in vec2 texCoord1;
in vec2 texCoord2;
in float blend;

uniform sampler2D texture;

void main(void) {
    vec4 colour1 = texture(texture, texCoord1);
    vec4 colour2 = texture(texture, texCoord2);
    fragColour = mix(colour1, colour2, blend);
}
