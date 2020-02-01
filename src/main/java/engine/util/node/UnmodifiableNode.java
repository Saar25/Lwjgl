package engine.util.node;

import engine.rendering.Spatial;

import java.util.Collections;
import java.util.List;

public class UnmodifiableNode<T extends Spatial> implements INode<T> {

    private final INode<T> node;

    public UnmodifiableNode(INode<T> node) {
        this.node = node;
    }

    @Override
    public void attachChild(T child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void detachChild(T child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceChild(T child, T newChild) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isChild(T child) {
        return node.isChild(child);
    }

    @Override
    public boolean hasChildren() {
        return node.hasChildren();
    }

    @Override
    public List<? extends Spatial> getChildren() {
        return Collections.unmodifiableList(node.getChildren());
    }
}
