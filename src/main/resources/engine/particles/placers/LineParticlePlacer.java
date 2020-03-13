package engine.particles.placers;

import engine.particles.Particle;
import engine.particles.ParticlePlacer;
import maths.joml.Vector3f;

public class LineParticlePlacer implements ParticlePlacer {

    private final Vector3f vector;

    public LineParticlePlacer(Vector3f vector) {
        this.vector = vector;
    }

    @Override
    public void place(Particle particle) {
        final float distance = (float) Math.random() * 2 - 1;
        particle.getTransform().getPosition().set(vector);
        particle.getTransform().getPosition().mul(distance);
    }
}
