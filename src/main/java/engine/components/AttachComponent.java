package engine.components;

import engine.componentsystem.GameComponent;
import maths.objects.Transform;
import maths.objects.Transformable;

public class AttachComponent extends GameComponent {

    private final Transform attach;

    public AttachComponent(Transformable attach) {
        this.attach = attach.getTransform();
    }

    @Override
    public void update() {
        getTransform().set(attach);
    }
}
