package engine.entity;

import engine.componentsystem.ComponentBased;
import engine.componentsystem.ComponentGroup;
import engine.models.Model;
import engine.models.Skin;
import engine.rendering.Renderable;
import engine.rendering.Spatial;

public class Entity extends Spatial implements ComponentBased, Renderable {

    private final Skin skin;
    private final Model model;

    private final ComponentGroup components = new ComponentGroup(this);

    public Entity(Model model, Skin skin) {
        this.model = model;
        this.skin = skin;
    }

    @Override
    public ComponentGroup getComponents() {
        return components;
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
    public void process() {
        EntityRenderer.getInstance().process(this);
    }

    @Override
    public void update() {
        getComponents().update();
    }

    @Override
    public void delete() {
        skin.cleanUp();
        model.delete();
    }
}
