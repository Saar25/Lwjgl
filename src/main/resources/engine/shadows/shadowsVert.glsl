#version 400 core

layout (location=0) in vec3 in_position;
layout (location=1) in vec2 in_texCoord;

out vec2 texCoord;

uniform mat4 mvpMatrix;
uniform int textureRowsCount; // used for texture atlases
uniform vec2 textureOffset;   // used for texture atlases

void main() {

    gl_Position = mvpMatrix * vec4(in_position, 1.0);

    gl_ClipDistance[0] = 0; // no clipping is needed, disabling the clipping plane
                            // does not work on some drivers (including mine)

    if (textureRowsCount > 1) { // This is a texture atlas
        texCoord = (in_texCoord / textureRowsCount) + textureOffset;
    }
    else {                      // This is a regular texture
        texCoord = in_texCoord;
    }

}
