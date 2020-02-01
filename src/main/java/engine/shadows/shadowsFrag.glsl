#version 400 core

in vec2 texCoord;

out float fragDepth;

uniform sampler2D texture;

void main() {

    if(texture(texture, texCoord).a < .1) {
        discard;
    }

    fragDepth = gl_FragCoord.z;
}
