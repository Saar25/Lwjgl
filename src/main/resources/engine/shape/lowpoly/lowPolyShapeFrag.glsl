/*
    TERRAIN FRAGMENT SHADER
*/
#version 400 core

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

const int pcf = 3; // Percentage Closer Filtering
const float pixels = pow(pcf * 2 + 1, 2);
const float pixelSize = 1 / 8192.0;

/* ====================== */
/*                        */
/*   IN / OUT VARIABLES   */
/*                        */
/* ====================== */

    flat in vec4 v_colour;
    in vec4 v_shadowHCoords;

    out vec4 fragColour;


/* ===================== */
/*                       */
/*   UNIFORM VARIABLES   */
/*                       */
/* ===================== */

    uniform sampler2D shadowMap;
    uniform bool enableShadows;
    uniform float shadowPower;
    uniform float shadowBias;


/* ======================== */
/*                          */
/*   SHADOWS CALCULATIONS   */
/*                          */
/* ======================== */

// Vertex   variables : v_shadowHCoords
// Uniform  variables : shadowPower, shadowBias
// Constant variables : pixels, pixelSize, pcf
float calcShadowFactor() {
    if (v_shadowHCoords.w < 0.1) {
        return 1;
    }
    float shadowFactor = 1;
    for (int x = -pcf; x <= pcf; x++){
        if (v_shadowHCoords.z > sTexture(shadowMap, v_shadowHCoords.xy + vec2(x, x) * pixelSize).r + shadowBias) {
            shadowFactor -= shadowPower * v_shadowHCoords.w / pixels;
        }
    }
    for (int y = -pcf; y <= pcf; y++){
        if (v_shadowHCoords.z > sTexture(shadowMap, v_shadowHCoords.xy + vec2(-y, y) * pixelSize).r + shadowBias) {
            shadowFactor -= shadowPower * v_shadowHCoords.w / pixels;
        }
    }
    if (shadowFactor == 1 || shadowFactor == 0) {
        return shadowFactor;
    }
    for (int x = -pcf; x <= pcf; x++){
        for (int y = -pcf; y <= pcf; y++){
            if (x != y && y != -x && v_shadowHCoords.z > sTexture(shadowMap, v_shadowHCoords.xy + vec2(x, y) * pixelSize).r + shadowBias) {
                shadowFactor -= shadowPower * v_shadowHCoords.w / pixels;
            }
        }
    }
    return shadowFactor;
}

/* =============== */
/*                 */
/*   MAIN METHOD   */
/*                 */
/* =============== */

void main(void) {

    fragColour = v_colour;

    fragColour.rgb *= enableShadows ? calcShadowFactor() : 1;
}