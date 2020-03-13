package engine.particles.placers;

import engine.particles.Particle;
import engine.particles.ParticlePlacer;
import maths.joml.Vector3f;
import maths.utils.Vector3;

public class CircleParticlePlacer implements ParticlePlacer {

    private final Vector3f center;
    private final float radius;

    public CircleParticlePlacer(float radius) {
        this(Vector3.zero(), radius);
    }

    public CircleParticlePlacer(Vector3f center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void place(Particle particle) {
        final float x = (float) Math.random() - .5f;
        final float z = (float) Math.random() - .5f;
        final float r = (float) (radius * Math.random());
        particle.getTransform().getPosition().set(x, 0, z);
        particle.getTransform().getPosition().normalize(r);
        particle.getTransform().getPosition().add(center);
    }
}
