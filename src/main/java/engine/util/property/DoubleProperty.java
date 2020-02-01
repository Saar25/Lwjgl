package engine.util.property;

public class DoubleProperty extends AbstractProperty<Double> implements Property<Double> {

    private double value;

    public DoubleProperty() {
        this.value = 0;
    }

    public DoubleProperty(double value) {
        this.value = value;
    }

    public double get() {
        return value;
    }

    public void set(double value) {
        if (this.value != value) {
            changed(this.value, value);
            this.value = value;
        }
    }

    @Override
    public Double getValue() {
        return get();
    }

    @Override
    public void setValue(Double value) {
        set(value);
    }

}
