package engine.rendering;

import engine.models.Model;
import engine.models.Skin;
import maths.objects.Transform;

public interface Renderable {

    Skin getSkin();

    Model getModel();

    Transform getTransform();
}
