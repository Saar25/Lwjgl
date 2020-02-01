package engine.entity;

import engine.componentsystem.ComponentBasedBlueprint;
import engine.models.Model;
import engine.models.Skin;

public class EntityBlueprint extends ComponentBasedBlueprint {

    private final Model model;
    private final Skin skin;

    public EntityBlueprint(Model model, Skin skin) {
        this.model = model;
        this.skin = skin;
    }

    @Override
    public Entity createInstance() {
        final Entity entity = new Entity(model, skin);
        getComponents().apply(entity);
        return entity;
    }

}
