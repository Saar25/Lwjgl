package engine.water.lowpoly;

import engine.terrain.Terrain;
import engine.water.wavy.WavyWater;

public class LowpolyWavyWater extends WavyWater {

    public LowpolyWavyWater() {

    }

    public LowpolyWavyWater(int vertices) {
        super(vertices);
    }

    public LowpolyWavyWater(Terrain terrain) {
        super(terrain);
    }

    public LowpolyWavyWater(Terrain terrain, int vertices) {
        super(terrain, vertices);
    }

    @Override
    public void process() {
        LowpolyWavyWaterRenderer.getInstance().process(this);
    }
}
