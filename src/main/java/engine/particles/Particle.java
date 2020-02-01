package engine.particles;

import engine.componentsystem.ComponentBased;
import engine.componentsystem.ComponentGroup;
import engine.gameengine.Time;
import engine.rendering.Spatial;

public class Particle extends Spatial implements ComponentBased {

    private ComponentGroup components = new ComponentGroup(this);

    private float maxAge;
    private float age = 0;

    public Particle(float maxAge) {
        this.maxAge = maxAge;
    }

    /**
     * Returns the normalized age, the particle's age starts
     * at 0 linearly increases to 1 when it reaches max age
     *
     * @return the normalized age
     */
    public float getNormalizedAge() {
        return Math.min(age / maxAge, 1);
    }

    /**
     * Returns whether this particle is alive or not
     *
     * @return true if the particle is alive, false if dead
     */
    public boolean isAlive() {
        return age < maxAge;
    }

    /**
     * Recreates the particle
     *
     * @param maxAge the maximum age
     */
    public void recreate(float maxAge) {
        this.components = new ComponentGroup(this);
        this.age = 0;
    }

    @Override
    public ComponentGroup getComponents() {
        return components;
    }

    @Override
    public void update() {
        getComponents().update();
        age += Time.getDelta();
    }
}
