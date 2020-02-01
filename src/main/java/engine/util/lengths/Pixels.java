package engine.util.lengths;

public class Pixels implements ILength{

    private final int value;

    public Pixels(int value) {
        this.value = value;
    }

    @Override
    public int proportionTo(int length) {
        return value;
    }

    public int getValue() {
        return value;
    }
}
