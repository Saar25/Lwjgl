package engine.rendering;

import engine.effects.Effect;
import engine.util.node.Node;

import java.util.ArrayList;
import java.util.List;

public class Scene extends Node {

    private final Camera camera;
    private final List<Effect> effects;

    public Scene(Camera camera) {
        this.camera = camera;
        this.effects = new ArrayList<>();
    }

    public Camera getCamera() {
        return camera;
    }

    public void addEffect(Effect effect) {
        this.effects.add(effect);
    }

    public void processEffects(RenderManager renderer) {
        for (Effect effect : effects) {
            effect.processIfEnabled(renderer, camera);
        }
    }

    public <T extends Effect> T getEffect(Class<T> c) {
        return effects.stream().filter(c::isInstance).findFirst().map(c::cast).orElse(null);
    }

    @Override
    public void update() {
        super.update();
        camera.update();
    }

    @Override
    public void delete() {
        super.delete();
        effects.forEach(Effect::delete);
    }
}
