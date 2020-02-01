package engine.shape;

import engine.models.Model;
import engine.models.ModelGenerator;

public class Cube extends Shape {

    public Cube() {
        setLowPoly(true);
    }

    @Override
    protected Model createModel() {
        return ModelGenerator.generateCube();
    }
}
