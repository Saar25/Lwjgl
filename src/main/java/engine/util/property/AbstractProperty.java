package engine.util.property;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractProperty<T> implements Property<T> {

    private final List<ChangeListener<T>> listeners = new LinkedList<>();
    private final ChangeListener<T> bindingListener = (o, old, value) -> setValue(value);

    protected void changed(T oldValue, T currentValue) {
        listeners.forEach(l -> l.changed(this, oldValue, currentValue));
    }

    @Override
    public void addListener(ChangeListener<T> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<T> listener) {
        listeners.remove(listener);
    }

    @Override
    public void bind(Observable<T> property) {
        property.addListener(bindingListener);
    }

    @Override
    public void unbind(Observable<T> property) {
        property.removeListener(bindingListener);
    }

    @Override
    public String toString() {
        return "[" + getClass().getSimpleName() + ": " + getValue() + "]";
    }

}
