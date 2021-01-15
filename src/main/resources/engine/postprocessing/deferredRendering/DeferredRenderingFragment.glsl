#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

out vec3 fragColour;

in vec2 texCoord;

uniform sampler2D albedo;
uniform sampler2D normal;
uniform sampler2D depth;
uniform mat4 projMatrixInv;
uniform mat4 viewMatrixInv;
uniform float zNear;
uniform float zFar;

uniform int u_index;

vec3 getViewPosition(float depth) {
    float z = depth * 2.0 - 1.0;

    vec4 clipSpacePosition = vec4(texCoord * 2.0 - 1.0, z, 1.0);
    vec4 viewSpacePosition = projMatrixInv * clipSpacePosition;

    viewSpacePosition /= viewSpacePosition.w;

    vec4 worldSpacePosition = viewMatrixInv * viewSpacePosition;

    return worldSpacePosition.rgb / worldSpacePosition.w;
}

float getLinearDepth(float depth) {
    return 2.0 * zNear * zFar / (zFar + zNear -
            (2.0 * depth - 1.0) * (zFar - zNear));
}

void main(void) {
    int index = int(texCoord.x * 2) * 2 + int(texCoord.y * 2);
    float depthValue = sTexture(depth, texCoord * 2).r;
    if (depthValue == 1) index = 4;
    switch (index) {
        case 0:
            fragColour = sTexture(albedo, texCoord * 2).rgb;
            break;
        case 1:
            fragColour = sTexture(normal, texCoord * 2).rgb;
            break;
        case 2:
            fragColour = vec3(getLinearDepth(depthValue) / 500);
            break;
        case 3:
            fragColour = getViewPosition(depthValue).rgb;
            break;
        default:
            fragColour = vec3(0);
    }

    depthValue = sTexture(depth, texCoord).r;
    switch (u_index % 4) {
        case 0:
            fragColour = sTexture(albedo, texCoord).rgb;
            break;
        case 1:
            fragColour = sTexture(normal, texCoord).rgb;
            break;
        case 2:
            if (depthValue == 1) fragColour = vec3(0);
            else fragColour = vec3(getLinearDepth(depthValue) / 500);
            break;
        case 3:
            if (depthValue == 1) fragColour = vec3(0);
            else fragColour = getViewPosition(depthValue).rgb;
            break;
        default:
            fragColour = vec3(0);
    }
}
