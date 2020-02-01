package engine.water.wavy;

import engine.models.Model;
import engine.terrain.Terrain;
import engine.util.Lazy;
import engine.util.property.FloatProperty;
import engine.water.Water;
import engine.water.WaterGenerator;
import engine.water.generation.WaveWaterGeneratorOld;
import maths.joml.Vector3f;
import maths.utils.Vector3;

public class WavyWater extends Water {

    private static final Lazy<Model> MODEL = new Lazy<>(new WaveWaterGeneratorOld(32)::generateModel);

    private final FloatProperty amplitudeProperty = new FloatProperty(1);
    private final Vector3f colour = Vector3.of(0, 1f, 1f);

    public WavyWater() {
        super(MODEL.get());
    }

    public WavyWater(int vertices) {
        super(WaterGenerator.generateModel(vertices));
    }

    public WavyWater(Terrain terrain) {
        this(terrain, 64);
    }

    public WavyWater(Terrain terrain, int vertices) {
        super(WaterGenerator.generateModel(terrain, vertices));
        getTransform().set(terrain.getTransform());
    }

    public static void deleteModel() {
        MODEL.ifAssigned(Model::delete);
    }

    @Override
    public void process() {
        WavyWaterRenderer.getInstance().process(this);
    }

    /**
     * Returns the amplitude property of the water
     *
     * @return the amplitude property of the water
     */
    public FloatProperty amplitudeProperty() {
        return amplitudeProperty;
    }

    public float getAmplitude() {
        return amplitudeProperty().getValue();
    }

    public void setAmplitude(float amplitude) {
        amplitudeProperty().setValue(amplitude);
    }

    /**
     * Returns the colour of the water
     *
     * @return the colour of the water
     */
    public Vector3f getColour() {
        return colour;
    }
}
