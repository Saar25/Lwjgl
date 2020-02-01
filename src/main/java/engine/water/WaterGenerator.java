package engine.water;

import engine.models.Model;
import engine.models.ModelGenerator;
import engine.terrain.Terrain;
import engine.water.generation.WaveWaterGeneratorOld;

public class WaterGenerator {

    private WaterGenerator() {

    }

    public static Model generateModel(int vertexCount) {
        return ModelGenerator.planeGenerator(vertexCount).generateModel();
    }

    public static Model generateModel(Terrain terrain, int vertices) {
        return new WaveWaterGeneratorOld(terrain, vertices).generateModel();
    }

}
