package engine.util.node;

import engine.rendering.Spatial;

import java.util.ArrayList;
import java.util.List;

public class GGroup<T extends Spatial> extends Spatial implements INode<T> {

    private final List<T> children;

    public GGroup() {
        this.children = new ArrayList<>();
    }

    @Override
    public void attachChild(T child) {
        getChildren().add(child);
    }

    @Override
    public void detachChild(T child) {
        getChildren().remove(child);
    }

    @Override
    public void replaceChild(T child, T newChild) {
        if (isChild(child)) {
            detachChild(child);
            attachChild(newChild);
        }
    }

    public List<T> getChildren() {
        return children;
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
    public void process() {
        getChildren().forEach(T::process);
    }

    @Override
    public void update() {
        getChildren().forEach(T::update);
    }

    @Override
    public void delete() {
        getChildren().forEach(T::delete);
    }

}
