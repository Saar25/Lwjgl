package tegui.driver;

public abstract class FloatDriver implements ValueDriver<Float> {

    protected float value;

    public final float get() {
        return value;
    }

    @Override
    public final Float getValue() {
        return get();
    }
}
