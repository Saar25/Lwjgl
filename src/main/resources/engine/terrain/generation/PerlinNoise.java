package engine.terrain.generation;

import maths.noise.Noise2f;

import java.util.Random;

public class PerlinNoise implements HeightGenerator, Noise2f {

    private static final float ROUGHNESS = 0.03f;
    private static final float AMPLITUDE = 100f;
    private static final int OCTAVES = 3;

    private final Random random = new Random();
    private final long seed;

    private final float roughness;
    private final float amplitude;
    private final int octaves;
    private final float shear;

    public PerlinNoise() {
        this.seed = random.nextInt(1000000);
        this.roughness = ROUGHNESS;
        this.amplitude = AMPLITUDE;
        this.octaves = OCTAVES;
        this.shear = 50;
    }

    public PerlinNoise(float amplitude, float shear) {
        this.seed = random.nextInt(1000000);
        this.roughness = ROUGHNESS;
        this.amplitude = amplitude;
        this.octaves = OCTAVES;
        this.shear = shear;
    }

    public PerlinNoise(int seed, float amplitude, float shear) {
        this.seed = seed;
        this.roughness = ROUGHNESS;
        this.amplitude = amplitude;
        this.octaves = OCTAVES;
        this.shear = shear;
    }

    public PerlinNoise(float roughness, float amplitude, int octaves, float shear) {
        this(0, roughness, amplitude, octaves, shear);
    }

    public PerlinNoise(long seed, float roughness, float amplitude, int octaves, float shear) {
        this.seed = seed;
        this.roughness = roughness;
        this.amplitude = amplitude;
        this.octaves = octaves;
        this.shear = shear;
    }

    public PerlinNoise(long seed) {
        this.seed = seed;
        this.roughness = ROUGHNESS;
        this.amplitude = AMPLITUDE;
        this.octaves = OCTAVES;
        this.shear = 50;
    }

    public PerlinNoise(long seed, float roughness, float amplitude, int octaves) {
        this.seed = seed;
        this.roughness = roughness;
        this.amplitude = amplitude;
        this.octaves = octaves;
        this.shear = 50;
    }

    @Override
    public float getHeight(float x, float y) {
        return noise(x, y);
    }

    @Override
    public float noise(float x, float y) {
        x /= shear;
        y /= shear;
        float height = 0;
        float d = (float) Math.pow(2, octaves - 1);
        for (int i = 0; i < octaves; i++) {
            float freq = (float) (Math.pow(2, i) / d);
            float amp = (float) Math.pow(roughness, i) * amplitude;
            height += getInterpolatedNoise(x * freq, y * freq) * amp;
        }
        return height;
    }

    private float getInterpolatedNoise(float x, float z) {
        int intX = (int) Math.floor(x);
        int intZ = (int) Math.floor(z);
        float fractionX = x - intX;
        float fractionZ = z - intZ;

        float v1 = getSmoothNoise(intX, intZ);
        float v2 = getSmoothNoise(intX + 1, intZ);
        float v3 = getSmoothNoise(intX, intZ + 1);
        float v4 = getSmoothNoise(intX + 1, intZ + 1);
        float i1 = interpolate(v1, v2, fractionX);
        float i2 = interpolate(v3, v4, fractionX);
        return interpolate(i1, i2, fractionZ);
    }

    private float interpolate(float a, float b, float blend) {
        double theta = blend * Math.PI;
        float f = (float) (1f - Math.cos(theta)) * 0.5f;
        return a * (1f - f) + b * f;
    }

    private float getSmoothNoise(int x, int z) {
        float height = 0;
        height += (getNoise(x - 1, z - 1) + getNoise(x + 1, z - 1)
                + getNoise(x - 1, z + 1) + getNoise(x + 1, z + 1)) / 16f;
        height += (getNoise(x - 1, z) + getNoise(x + 1, z)
                + getNoise(x, z - 1) + getNoise(x, z + 1)) / 8f;
        height += getNoise(x, z) / 4f;
        return height;
    }

    private float getNoise(int x, int y) {
        random.setSeed(x * 15210 + y * 52150 + seed);
        return random.nextFloat() * 2f - 1f;
    }
}

