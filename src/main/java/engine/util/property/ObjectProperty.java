package engine.util.property;

public class ObjectProperty<T> extends AbstractProperty<T> implements Property<T> {

    private T value;

    public ObjectProperty() {
        this.value = null;
    }

    public ObjectProperty(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        if (this.value != value) {
            changed(this.value, value);
            this.value = value;
        }
    }

    @Override
    public T getValue() {
        return get();
    }

    public void setValue(T value) {
        set(value);
    }
}
