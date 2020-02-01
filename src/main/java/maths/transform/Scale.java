package maths.transform;

public class Scale {

    private float value;

    private Scale(float value) {
        this.value = value;
    }

    public static Scale of(float value) {
        return new Scale(value);
    }

    public static Scale create() {
        return new Scale(1);
    }

    public Scale scale(float scale) {
        this.value *= scale;
        return this;
    }

    public float getValue() {
        return value;
    }
}
