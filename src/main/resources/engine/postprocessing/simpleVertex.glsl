#version 400

const float positions[8] = {
    -1, +1, -1, -1,
    +1, +1, +1, -1
};

out vec2 texCoord;

vec2 calculatePosition(int index) {
    return vec2(positions[index * 2], positions[index * 2 + 1]);
}

void main(void) {

    vec2 position = calculatePosition(gl_VertexID);
    gl_Position = vec4(position, 0, 1);

    texCoord = position.xy * .5 + .5;

}
