package engine.terrain.generation;

import maths.joml.Vector3f;
import maths.utils.Vector3;
import opengl.textures.TextureInfo;
import opengl.textures.TextureLoader;

public class HeightMapGenerator implements HeightGenerator {

    private final float amplitude;
    private final TextureInfo heightMapInfo;

    private final Vector3f position;
    private float size;

    public HeightMapGenerator(Vector3f position, float size, float amplitude, String file) throws Exception {
        this.heightMapInfo = TextureLoader.load(file);

        this.position = Vector3.of(position);
        this.size = size;
        this.amplitude = amplitude;
    }

    @Override
    public float getHeight(float x, float z) {
        final int col = (int) ((position.x + size - x) / size * heightMapInfo.getWidth());
        final int row = (int) ((position.z + size - z) / size * heightMapInfo.getHeight());
        final int colour = heightMapInfo.getData().getInt(col + row * heightMapInfo.getWidth());
        return ((colour & 255) * 2f - 1f) * amplitude; //Gray scale
    }
}
