package engine.shape;

import engine.models.Model;
import engine.models.ModelGenerator;

public class Plane extends Shape {

    public Plane() {

    }

    @Override
    protected Model createModel() {
        return ModelGenerator.generatePlane();
    }

}
