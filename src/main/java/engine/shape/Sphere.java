package engine.shape;

import engine.models.Model;
import engine.models.ModelGenerator;
import engine.shape.smooth.SmoothShapeRenderer;

public class Sphere extends Shape {

    public Sphere() {

    }

    @Override
    protected Model createModel() {
        return ModelGenerator.generateSphere();
    }
}
