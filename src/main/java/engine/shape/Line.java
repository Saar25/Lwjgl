package engine.shape;

import engine.models.Model;
import engine.models.ModelGenerator;

public class Line extends Shape {

    private final float x1;
    private final float y1;
    private final float x2;
    private final float y2;

    public Line(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    protected Model createModel() {
        return ModelGenerator.lineGenerator(x1, y1, x2, y2).generateModel();
    }
}
