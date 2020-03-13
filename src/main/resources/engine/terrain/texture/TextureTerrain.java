package engine.terrain.texture;

import engine.models.Skin;
import engine.terrain.Terrain;
import engine.terrain.TerrainRenderer;
import engine.terrain.generation.*;
import maths.joml.Vector3f;

public class TextureTerrain extends Terrain {

    private static final HeightGenerator heightGenerator = new SimplexNoise(75, 500);
    private static final TerrainModelGenerator modelGenerator =
            new QuadsTerrainModelGenerator(128, heightGenerator, 0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);

    private final HeightCache heightCache;

    public TextureTerrain(Vector3f position, float size, Skin skin) {
        super(modelGenerator.generateModel(position, size), skin, size);
        this.heightCache = new HeightCache(128, heightGenerator, position, size);
    }

    @Override
    public float getHeight(float x, float z) {
        return heightCache.getHeight(x, z);
    }

    @Override
    public void process() {
        TerrainRenderer.getInstance().process(this);
    }
}
