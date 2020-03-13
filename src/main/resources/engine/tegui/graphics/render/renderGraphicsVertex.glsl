#version 400

layout (location = 0) in vec2 in_position;

uniform ivec2 positionOnScreen;
uniform ivec2 windowSize;

vec2 calculatePosition() {
    vec2 position = positionOnScreen / windowSize;
    return position * 2 - 1 + in_position;
}

void main(void) {
    vec2 position = calculatePosition();

    gl_Position = vec4(pos, 0, 1);
    gl_ClipDistance[0] = 0;

}
