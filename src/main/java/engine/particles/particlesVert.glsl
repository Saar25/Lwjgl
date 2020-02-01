#version 400

const float positions[8] = {
    -0.5f, +0.5f, -0.5f, -0.5f,
    +0.5f, +0.5f, +0.5f, -0.5f
};

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 scaleAge;

out vec2 texCoord1;
out vec2 texCoord2;
out float blend;

uniform mat4 viewMatrix;
uniform mat4 projectionViewMatrix;
uniform int textureRows;
uniform vec4 clipPlane;

vec2 calculatePosition() {
    float x = positions[gl_VertexID * 2];
    float y = positions[gl_VertexID * 2 + 1];
    return vec2(x, y);
}

mat4 buildTransformation() {
    mat4 m = transpose(viewMatrix);
    m[0][3] = m[1][3] = m[2][3] = 0;
    m[3] = vec4(position, 1);
    return m;
}

vec2 calculateTexCoord(int index, vec2 pos) {
    float x = mod(index, textureRows);
    float y = floor(float(index) / textureRows);
    return (vec2(x, y) + pos + .5) / textureRows;
}

void main(void) {
    vec2 pos = calculatePosition();
    mat4 transformation = buildTransformation();
    vec4 worldPosition = transformation * vec4(pos * scaleAge.x, 0, 1);
    gl_Position = projectionViewMatrix * worldPosition;

    gl_ClipDistance[0] = dot(worldPosition, clipPlane);

    int textures = textureRows * textureRows;
    float indexf = scaleAge.y * textures;
    int index = int(indexf);
    int next = min(index + 1, textures - 1);

    texCoord1 = calculateTexCoord(index, pos);
    texCoord2 = calculateTexCoord(next, pos);
    blend = indexf - index;
}
