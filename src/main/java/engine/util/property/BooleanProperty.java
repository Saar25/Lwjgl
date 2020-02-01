package engine.util.property;

public class BooleanProperty extends AbstractProperty<Boolean> implements Property<Boolean> {

    private boolean value;

    public BooleanProperty() {
        this.value = false;
    }

    public BooleanProperty(boolean value) {
        this.value = value;
    }

    public boolean get() {
        return value;
    }

    public void set(boolean value) {
        if (this.value != value) {
            changed(this.value, value);
            this.value = value;
        }
    }

    public void flip() {
        set(!get());
    }

    @Override
    public Boolean getValue() {
        return get();
    }

    @Override
    public void setValue(Boolean value) {
        set(value);
    }

}
