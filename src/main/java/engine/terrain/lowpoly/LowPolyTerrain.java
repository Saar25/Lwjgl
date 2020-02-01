package engine.terrain.lowpoly;

import engine.models.Model;
import engine.models.Skin;
import engine.terrain.Terrain;
import engine.terrain.generation.*;
import maths.joml.Vector3f;
import maths.utils.Vector3;

public class LowPolyTerrain extends Terrain {

    private static final HeightGenerator heightGenerator = new SimplexNoise(1, 75, 500);
    private static final ColourGenerator colourGenerator = new HeightColourGenerator()
            .withColour(-5.0f, Vector3.of(.76f, .69f, .50f))
            .withColour(+8.0f, Vector3.of(0.5f, 0.8f, 0.0f))
            .withColour(+35.f, Vector3.of(1.0f, 1.0f, 1.0f));

    private final HeightGenerator heightCache;
    private final LPTerrainConfigs configs;

    public LowPolyTerrain(Vector3f position, float size) {
        super(Skin.create(), size);
        this.heightCache = new HeightCache(128, heightGenerator, position, size);
        getTransform().setPosition(position);

        this.configs = createConfigs();
        configs.setPosition(position);
        configs.setSize(size);
    }

    public LowPolyTerrain(LPTerrainConfigs configs) {
        super(Skin.create(), configs.size);
        this.heightCache = new HeightCache(configs.vertices,
                configs.heightGenerator, configs.position, configs.size);
        getTransform().setPosition(configs.position);
        this.configs = configs;
    }

    private static LPTerrainConfigs createConfigs() {
        final LPTerrainConfigs configs = new LPTerrainConfigs();
        configs.setLevels(0, 1, 2, 3, 5, 8, 13, 21, 34, 55, -1);
        configs.setHeightGenerator(heightGenerator);
        configs.setColourGenerator(colourGenerator);
        configs.setVertices(64);
        return configs;
    }

    @Override
    protected Model createModel() {
        return LPTerrainModelGenerator.generateModel(configs);
    }

    @Override
    public float getHeight(float x, float z) {
        return heightCache.getHeight(x, z);
    }

    @Override
    public void process() {
        LowPolyTerrainRenderer.getInstance().process(this);
    }
}
