package engine.particles;

import engine.componentsystem.ComponentGroupBuilder;
import engine.models.InstancedModel;
import engine.models.Model;
import engine.models.Skin;
import engine.rendering.Renderable;
import engine.util.node.Parent;
import engine.util.property.FloatProperty;
import engine.util.property.IntegerProperty;
import glfw.input.event.ClickEvent;
import glfw.input.event.MoveEvent;
import glfw.input.event.ScrollEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ParticleSystem extends Parent implements Renderable {

    private final List<Particle> particles = new LinkedList<>();
    private final ComponentGroupBuilder components = new ComponentGroupBuilder();

    private final ParticlesMemoryManager memoryManager = new ParticlesMemoryManager(this);
    private final InstancedModel model = memoryManager.createModel();

    private final Skin skin;
    private final ParticlePlacer placer;

    private final IntegerProperty maxParticlesProperty = new IntegerProperty(1000);
    private final IntegerProperty maxPerUpdateProperty = new IntegerProperty(1);
    private final FloatProperty maxAgeProperty = new FloatProperty(5f);

    public ParticleSystem(ParticlePlacer placer, Skin skin) {
        this.placer = placer;
        this.skin = skin;
    }

    /**
     * Returns the max amount of particles property
     */
    public IntegerProperty maxParticlesProperty() {
        return maxParticlesProperty;
    }

    public int getMaxParticles() {
        return maxParticlesProperty().get();
    }

    public void setMaxParticles(int maxParticles) {
        maxParticlesProperty().set(maxParticles);
    }

    /**
     * Returns the max amount of particles can be added in one update property
     */
    public IntegerProperty maxPerUpdateProperty() {
        return maxPerUpdateProperty;
    }

    public int getMaxPerUpdate() {
        return maxPerUpdateProperty().get();
    }

    public void setMaxPerUpdate(int maxParticlesPerUpdate) {
        this.maxPerUpdateProperty().set(maxParticlesPerUpdate);
    }

    /**
     * Returns the max age of the particles property
     */
    public FloatProperty maxAgeProperty() {
        return maxAgeProperty;
    }

    public float getMaxAge() {
        return maxAgeProperty().get();
    }

    public void setMaxAge(float maxParticlesAge) {
        maxAgeProperty().set(maxParticlesAge);
    }

    @Override
    public Skin getSkin() {
        return skin;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void update() {
        super.update();

        final List<Particle> dead = new ArrayList<>();
        for (Particle child : getChildren()) {
            if (!child.isAlive()) dead.add(child);
        }
        getChildren().removeIf(p -> !p.isAlive());

        final int recreate = Math.min(maxPerUpdateProperty().get(),
                maxParticlesProperty().get() - getChildren().size());
        final List<Particle> recreated = new ArrayList<>();

        for (int i = 0; i < Math.min(dead.size(), recreate); i++) {
            final Particle particle = dead.get(i);
            particle.recreate(maxAgeProperty().get());
            recreated.add(particle);
        }
        for (int i = 0; i < recreate - dead.size(); i++) {
            recreated.add(new Particle(maxAgeProperty().get()));
        }

        for (Particle particle : recreated) {
            getComponents().apply(particle);
            placer.place(particle);

            getChildren().add(particle);
            particle.setParent(this);
        }

        memoryManager.update();
        model.setInstances(getChildren().size());
    }

    public ComponentGroupBuilder getComponents() {
        return components;
    }

    @Override
    public void process() {
        ParticleRenderer.getInstance().process(this);
    }

    @Override
    public void delete() {
        memoryManager.delete();
    }

    @Override
    public void onMouseMoveEvent(MoveEvent event) {

    }

    @Override
    public void onMouseClickEvent(ClickEvent event) {

    }

    @Override
    public void onMouseScrollEvent(ScrollEvent event) {

    }

    @Override
    public List<Particle> getChildren() {
        return particles;
    }
}
