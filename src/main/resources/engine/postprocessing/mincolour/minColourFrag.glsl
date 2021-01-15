#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

out vec4 fragColour;

in vec2 texCoord;

uniform sampler2D u_texture;
uniform vec3 minColour;

vec4 getColour() {
    vec4 colour = sTexture(u_texture, texCoord);
    bvec3 cmp = greaterThanEqual(colour.rgb, minColour);
    if (cmp == bvec3(true)) {
        return colour;
    } else {
        return vec4(0);
    }
}

void main(void) {

    fragColour = getColour();

}
