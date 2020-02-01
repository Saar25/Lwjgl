package engine.util.lengths;

public class Percent implements ILength {

    public static final Percent ZERO = new Percent(0);

    private final float value;

    protected Percent(float value) {
        this.value = value;
    }

    @Override
    public int proportionTo(int length) {
        return (int) (value * .01f * length);
    }

    public float getValue() {
        return value;
    }
}
