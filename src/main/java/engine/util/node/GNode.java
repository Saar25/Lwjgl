package engine.util.node;

import engine.rendering.Spatial;

import java.util.ArrayList;
import java.util.List;

public class GNode<T extends Spatial> extends Parent implements INode<T> {

    private final List<T> children = new ArrayList<>();

    @Override
    public void attachChild(T child) {
        final IParent parent;
        if ((parent = child.getParent()) != null) {
            parent.getChildren().remove(child);
        }
        getChildren().add(child);
        child.setParent(this);
    }

    @Override
    public void detachChild(T child) {
        if (getChildren().remove(child)) {
            child.setParent(null);
        }
    }

    @Override
    public void replaceChild(T child, T newChild) {
        if (isChild(child)) {
            detachChild(child);
            attachChild(newChild);
        }
    }

    @Override
    public boolean isChild(T child) {
        return getChildren().contains(child);
    }

    @Override
    public boolean hasChildren() {
        return getChildren().isEmpty();
    }

    @Override
    public List<T> getChildren() {
        return children;
    }

}
