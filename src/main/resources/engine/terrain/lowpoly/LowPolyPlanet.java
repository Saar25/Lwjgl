package engine.terrain.lowpoly;

import engine.models.Model;
import engine.models.Skin;
import engine.terrain.Terrain;
import engine.util.property.FloatProperty;
import engine.util.property.IntegerProperty;

public class LowPolyPlanet extends Terrain {

    private final IntegerProperty verticesProperty = new IntegerProperty();
    private final FloatProperty roughnessProperty = new FloatProperty(10);

    public LowPolyPlanet(float size) {
        this(64, size);
    }

    public LowPolyPlanet(int vertices, float size) {
        super(Skin.create(), size);
        verticesProperty().set(vertices);
        getProperties().add(roughnessProperty());
        getProperties().add(verticesProperty());
    }

    public IntegerProperty verticesProperty() {
        return verticesProperty;
    }

    public FloatProperty roughnessProperty() {
        return roughnessProperty;
    }

    @Override
    protected Model createModel() {
        return LowPolyPlanetModelGenerator.generateModel(
                verticesProperty().get(), getSize(), roughnessProperty().get());
    }

    @Override
    public float getHeight(float x, float z) {
        return getSize();
    }

    @Override
    public void process() {
        HighPolyTerrainRenderer.getInstance().process(this);
    }
}
