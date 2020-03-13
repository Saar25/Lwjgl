package engine.terrain;

import engine.models.Model;
import engine.models.Skin;
import engine.terrain.generation.*;
import maths.joml.Vector3f;
import opengl.textures.MultiTexture;

public class SimpleTerrain extends Terrain {

    private static final HeightGenerator heightGenerator = new SimplexNoise(75, 500);
    private static final TerrainModelGenerator modelGenerator =
            new QuadsTerrainModelGenerator(128, heightGenerator, 0, 2, 4, 16, 32);

    private final HeightCache heightCache;

    public SimpleTerrain(Vector3f position, float size, MultiTexture textures) {
        super(Skin.of(textures), size);
        this.heightCache = new HeightCache(128,
                heightGenerator, position, size);
        getTransform().setPosition(position);
    }

    public SimpleTerrain(Vector3f position, float size) {
        super(Skin.create(), size);
        this.heightCache = new HeightCache(128,
                heightGenerator, position, size);
        getTransform().setPosition(position);
    }

    @Override
    protected Model createModel() {
        return modelGenerator.generateModel(getTransform().getPosition(), getSize());
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
