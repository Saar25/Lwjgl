package engine.shape;

import engine.models.Model;
import engine.models.ModelGenerator;
import maths.noise.Noise3f;

public class NoisedSphere extends Shape {

    private final Noise3f noise;

    public NoisedSphere(Noise3f noise) {
        this.noise = noise;
    }

    @Override
    protected Model createModel() {
        return ModelGenerator.sphereGenerator(100, 100, noise).generateModel();
    }
}
