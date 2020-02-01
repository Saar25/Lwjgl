package engine.water.simple;

import engine.water.Water;
import engine.water.WaterGenerator;
import maths.joml.Vector3f;
import maths.utils.Vector3;

public class SimpleWater extends Water {

    private final Vector3f colour = Vector3.of(0, 1, 1);

    public SimpleWater() {
        super(WaterGenerator.generateModel(2));
    }

    public Vector3f getColour() {
        return colour;
    }

    @Override
    public void process() {
        SimpleWaterRenderer.getInstance().process(this);
    }
}
