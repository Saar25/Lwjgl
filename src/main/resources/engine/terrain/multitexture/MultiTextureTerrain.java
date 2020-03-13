package engine.terrain.multitexture;

import engine.models.Model;
import engine.models.Skin;
import engine.terrain.Terrain;
import engine.terrain.generation.*;
import engine.util.property.IntegerProperty;
import maths.joml.Vector3f;
import opengl.textures.MultiTexture;

public class MultiTextureTerrain extends Terrain {

    private static final HeightGenerator heightGenerator = new SimplexNoise(75, 500);
    private static final TerrainModelGenerator modelGenerator =
            new QuadsTerrainModelGenerator(128, heightGenerator, 0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);

    private final HeightCache heightCache;

    private final IntegerProperty tilingProperty = new IntegerProperty(40);

    public MultiTextureTerrain(Vector3f position, float size, MultiTexture textures) {
        super(Skin.of(textures), size);
        this.heightCache = new HeightCache(128,
                heightGenerator, position, size);
        getTransform().setPosition(position);
    }

    @Override
    protected Model createModel() {
        return modelGenerator.generateModel(getTransform().getPosition(), getSize());
    }

    @Override
    public void process() {
        MultiTextureTerrainRenderer.getInstance().process(this);
    }

    @Override
    public float getHeight(float x, float z) {
        return heightCache.getHeight(x, z);
    }

    public IntegerProperty tilingProperty() {
        return tilingProperty;
    }

    public int getTiling() {
        return tilingProperty().get();
    }

    public void setTiling(int tiling) {
        tilingProperty().set(tiling);
    }
}
