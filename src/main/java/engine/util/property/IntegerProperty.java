package engine.util.property;

public class IntegerProperty extends AbstractProperty<Integer> implements Property<Integer> {

    private int value;

    public IntegerProperty() {
        this.value = 0;
    }

    public IntegerProperty(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    public void set(int value) {
        if (this.value != value) {
            changed(this.value, value);
            this.value = value;
        }
    }

    @Override
    public Integer getValue() {
        return get();
    }

    @Override
    public void setValue(Integer value) {
        set(value);
    }

}
