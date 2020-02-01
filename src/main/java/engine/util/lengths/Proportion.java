package engine.util.lengths;

public class Proportion implements ILength {

    public static Proportion ZERO = new Proportion(0f);
    public static Proportion ONE = new Proportion(1f);
    public static Proportion HALF = new Proportion(.5f);

    private final float value;

    private Proportion(float value) {
        this.value = value;
    }

    public static Proportion of(float value) {
        if (value == 0f) {
            return Proportion.ZERO;
        } else if (value == 1f) {
            return Proportion.ONE;
        } else if (value == .5f) {
            return Proportion.HALF;
        }
        return new Proportion(value);
    }

    @Override
    public int proportionTo(int length) {
        return (int) (value * length);
    }

    public float getValue() {
        return value;
    }
}
