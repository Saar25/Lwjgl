#version 400

const vec2[4] positions = vec2[](
vec2(0, 1), vec2(0, 0),
vec2(1, 1), vec2(1, 0)
);

/* ====================== */
/*                        */
/*   IN / OUT VARIABLES   */
/*                        */
/* ====================== */

    out vec2 v_position;
    out vec4 v_bounds;
    out vec4 v_borders;
    out vec4 v_backgroundColour;


/* ===================== */
/*                       */
/*   UNIFORM VARIABLES   */
/*                       */
/* ===================== */

    uniform mat4 transformation;

    uniform vec4 bounds;
    uniform vec4 borders;
    uniform ivec2 windowSize;
    uniform ivec4 cornersColours;


/* =========== */
/*             */
/*   METHODS   */
/*             */
/* =========== */

vec2 toNdc(vec2 v) {
    return v / windowSize;
}

vec4 toNdc(vec4 v) {
    return v / vec4(windowSize, windowSize);
}

vec2 currentPosition() {
    return positions[gl_VertexID];
}

vec2 calculatePosition(vec2 p, vec2 s) {
    vec2 current = positions[gl_VertexID];
    vec2 pos = (p + s * current) * 2 - 1;
    return pos * vec2(1, -1);
}

vec2 mapInRectangle(vec4 rectangle, vec2 position) {
    return rectangle.xy + position * rectangle.zw;
}

vec4 getBackgroundColour(int id) {
    uint colour = cornersColours[id];
    float r = ((colour << 0 ) >> 24);
    float g = ((colour << 8 ) >> 24);
    float b = ((colour << 16) >> 24);
    float a = ((colour << 24) >> 24);
    return vec4(r, g, b, a) / 255;
}

void main(void) {
    vec4 bordersRect = bounds + vec4(-borders.xy, borders.xy + borders.zw);

    vec2 p = toNdc(bordersRect.xy);
    vec2 s = toNdc(bordersRect.zw);
    vec2 pos = calculatePosition(p, s);

    gl_Position = transformation * vec4(pos, 0, 1);
    gl_ClipDistance[0] = 0;

    v_position = currentPosition();
    v_position *= bordersRect.zw;
    v_position += bordersRect.xy;
    v_position -= bounds.xy;
    v_position /= bounds.zw;

    v_bounds = toNdc(bounds);
    v_borders = toNdc(bordersRect);

    v_backgroundColour = getBackgroundColour(gl_VertexID);
}
