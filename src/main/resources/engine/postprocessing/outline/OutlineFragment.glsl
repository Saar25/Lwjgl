#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

out vec4 fragColour;

in vec2 texCoord;

uniform sampler2D albedo;
uniform sampler2D normal;

void main(void) {
    const float eps = .005;
    const float minDot = .95;

    vec3 up = normalize(sTexture(normal, texCoord + vec2(0, +eps)).rgb);
    vec3 down = normalize(sTexture(normal, texCoord + vec2(0, -eps)).rgb);
    vec3 right = normalize(sTexture(normal, texCoord + vec2(+eps, 0)).rgb);
    vec3 left = normalize(sTexture(normal, texCoord + vec2(-eps, 0)).rgb);

    vec3 normal = normalize(sTexture(normal, texCoord).rgb);

    float dot1 = dot(normal, up);
    float dot2 = dot(normal, down);
    float dot3 = dot(normal, right);
    float dot4 = dot(normal, left);


    bvec4 flags = bvec4(dot1 >= minDot, dot2 >= minDot,
                        dot3 >= minDot, dot4 >= minDot);

    if (flags != bvec4(true)) {
        fragColour = vec4(0, 0, 0, 1);
    } else {
        fragColour = sTexture(albedo, texCoord);
    }

}
