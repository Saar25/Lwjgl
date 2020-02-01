package engine.shape;

import engine.models.Model;
import engine.models.ModelGenerator;

public class SpherifiedCube extends Shape {

    private final int vertices;

    public SpherifiedCube() {
        this.vertices = 16;
    }

    public SpherifiedCube(int vertices) {
        this.vertices = vertices;
    }

    @Override
    protected Model createModel() {
        return ModelGenerator.spherifiedCubeGenerator(vertices).generateModel();
    }
}
