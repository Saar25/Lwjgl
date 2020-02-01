package engine.util.node;

import engine.rendering.Spatial;

import java.util.ArrayList;
import java.util.List;

public class Node extends Parent implements INode<Spatial> {

    private final List<Spatial> children = new ArrayList<>();

    @Override
    public void attachChild(Spatial child) {
        if (child.getParent() != null) {
            child.getParent().getChildren().remove(child);
        }
        getChildren().add(child);
        child.setParent(this);
    }

    @Override
    public void detachChild(Spatial child) {
        if (getChildren().remove(child)) {
            child.setParent(null);
        }
    }

    @Override
    public void replaceChild(Spatial child, Spatial newChild) {
        if (isChild(child)) {
            detachChild(child);
            attachChild(newChild);
        }
    }

    @Override
    public boolean isChild(Spatial child) {
        return getChildren().contains(child);
    }

    @Override
    public boolean hasChildren() {
        return getChildren().isEmpty();
    }

    @Override
    public List<Spatial> getChildren() {
        return children;
    }
}
