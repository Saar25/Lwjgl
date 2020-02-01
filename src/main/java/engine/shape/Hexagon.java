package engine.shape;

import engine.models.Model;
import engine.models.ModelGenerator;

public class Hexagon extends Shape {

    public Hexagon() {

    }

    @Override
    protected Model createModel() {
        return ModelGenerator.generateCube();
    }
}
