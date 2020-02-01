package engine.util.node;

public interface INode<T> extends IParent {

    void attachChild(T child);

    void detachChild(T child);

    void replaceChild(T child, T newChild);

    boolean isChild(T child);

    boolean hasChildren();

}
