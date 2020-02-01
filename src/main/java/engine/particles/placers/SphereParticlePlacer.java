package engine.particles.placers;

import engine.particles.Particle;
import engine.particles.ParticlePlacer;
import maths.joml.Vector3f;
import maths.utils.Vector3;

public class SphereParticlePlacer implements ParticlePlacer {

    private final Vector3f center;
    private final float radius;

    public SphereParticlePlacer(float radius) {
        this(Vector3.zero(), radius);
    }

    public SphereParticlePlacer(Vector3f center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void place(Particle particle) {
        final float x = (float) (Math.random() - 0.5);
        final float y = (float) (Math.random() - 0.5);
        final float z = (float) (Math.random() - 0.5);
        particle.getTransform().getPosition().set(x, y, z);
        particle.getTransform().getPosition().normalize(radius);
        particle.getTransform().getPosition().add(center);
    }
}
