package engine.water;

import engine.models.Model;
import engine.models.Skin;
import engine.rendering.Renderable;
import engine.rendering.Spatial;

public abstract class Water extends Spatial implements Renderable {

    private final Model model;
    private final Skin skin;

    public Water(Model model) {
        this.model = model;
        this.skin = Skin.create();
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
    public void delete() {
        getModel().delete();
    }
}
