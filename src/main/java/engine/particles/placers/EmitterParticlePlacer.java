package engine.particles.placers;

import engine.particles.Particle;
import engine.particles.ParticlePlacer;
import engine.rendering.Spatial;
import maths.joml.Vector3f;

public class EmitterParticlePlacer implements ParticlePlacer {

    private final Vector3f emitter;

    public EmitterParticlePlacer(Spatial emitter) {
        this.emitter = emitter.getTransform().getPosition();
    }

    public EmitterParticlePlacer(Vector3f position) {
        this.emitter = position;
    }

    @Override
    public void place(Particle particle) {
        particle.getTransform().setPosition(emitter);
    }
}
