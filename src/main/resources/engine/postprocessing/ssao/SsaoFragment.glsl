#version 400

#if __VERSION__ < 130
#define sTexture texture2D
#else
#define sTexture texture
#endif

#define SAMPLES 64

out vec3 fragColour;

in vec2 texCoord;

uniform sampler2D albedo;
uniform sampler2D noise;
uniform sampler2D normal;
uniform sampler2D depth;

uniform vec3 samples[SAMPLES];

uniform ivec2 dimensions;

uniform mat4 projection;

const float scale = 4.0;
const float bias = .01;
const float radius = .0;

void main() {
    vec2 noiseScale = vec2(dimensions.x / 4.0, dimensions.y / 4.0);

    // get input for SSAO algorithm
    vec3 fragPos = sTexture(position, texCoord).xyz;
    vec3 normal = normalize(sTexture(normal, texCoord).rgb);
    vec3 randomVec = normalize(sTexture(noise, texCoord * noiseScale).xyz);
    // create TBN change-of-basis matrix: from tangent-space to view-space
    vec3 tangent = normalize(randomVec - normal * dot(randomVec, normal));
    vec3 bitangent = cross(normal, tangent);
    mat3 TBN = mat3(tangent, bitangent, normal);
    // iterate over the sample kernel and calculate occlusion factor
    float occlusion = 0.0;
    for (int i = 0; i < SAMPLES; ++i) {
        // get sample position
        vec3 kernelSampler = TBN * samples[i];// from tangent to view-space
        kernelSampler = fragPos + kernelSampler * radius;

        // project sample position (to sample texture) (to get position on screen/texture)
        vec4 offset = vec4(kernelSampler, 1.0);
        offset = projection * offset;// from view to clip-space
        offset.xyz /= offset.w;// perspective divide
        offset.xyz = offset.xyz * 0.5 + 0.5;// transform to range 0.0 - 1.0

        // get sample depth
        float sampleDepth = sTexture(position, offset.xy).z;// get depth value of kernel sample

        // range check & accumulate
        float rangeCheck = smoothstep(0.0, 1.0, radius / abs(fragPos.z - sampleDepth));
        occlusion += (sampleDepth >= kernelSampler.z + bias ? 1.0 : 0.0) * rangeCheck;
    }

    occlusion = 1.0 - (occlusion / SAMPLES);

    fragColour = vec3(occlusion);
    fragColour = sTexture(position, texCoord).xyz;
}