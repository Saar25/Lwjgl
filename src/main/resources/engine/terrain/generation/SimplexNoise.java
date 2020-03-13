package engine.terrain.generation;

import maths.noise.Noise2f;
import maths.noise.Noise3f;

public class SimplexNoise implements HeightGenerator, Noise2f, Noise3f {

    private final float seed;
    private final float amplitude;
    private final float shear;
    private final int octaves;

    public SimplexNoise(float seed, float amplitude, float shear, int octaves) {
        this.seed = seed;
        this.amplitude = amplitude;
        this.shear = shear;
        this.octaves = octaves;
    }

    public SimplexNoise(float seed, float amplitude, float shear) {
        this(seed, amplitude, shear, 1);
    }


    public SimplexNoise(float amplitude, float shear) {
        this((float) Math.random() * 1000, amplitude, shear);
    }

    public SimplexNoise(float amplitude) {
        this(amplitude, 1);
    }

    public SimplexNoise() {
        this(1, 1);
    }

    @Override
    public float getHeight(float x, float z) {
        float height = 0;
        double d = Math.pow(2, octaves - 1);
        for (int i = 0; i < octaves; i++) {
            float i2 = (float) Math.pow(2, i - 1);
            float freq = (float) (i2 / d);
            float cx = x / shear * freq + seed / 50f;
            float cz = z / shear * freq + seed / 50f;
            height += amplitude * maths.joml.SimplexNoise.noise(cx, cz) / i2;
        }
        return height;
    }

    @Override
    public float noise(float x, float y) {
        return getHeight(x, y);
    }

    @Override
    public float noise(float x, float y, float z) {
        float height = 0;
        double d = Math.pow(2, octaves - 1);
        for (int i = 0; i < octaves; i++) {
            float i2 = (float) Math.pow(2, i - 1);
            float freq = (float) (i2 / d);
            float cx = x / shear * freq + seed;
            float cy = y / shear * freq + seed;
            float cz = z / shear * freq + seed;
            height += amplitude * maths.joml.SimplexNoise.noise(cx, cy, cz) / i2;
        }
        return height;
    }
}
