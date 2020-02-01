package engine.util.property;

public class FloatProperty extends AbstractProperty<Float> implements Property<Float> {

    private float value;

    public FloatProperty() {
        this.value = 0;
    }

    public FloatProperty(float value) {
        this.value = value;
    }

    public float get() {
        return value;
    }

    public void set(float value) {
        if (this.value != value) {
            changed(this.value, value);
            this.value = value;
        }
    }

    @Override
    public Float getValue() {
        return get();
    }

    @Override
    public void setValue(Float value) {
        set(value);
    }
}
