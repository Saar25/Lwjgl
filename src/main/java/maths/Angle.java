package maths;

public class Angle {

    private final float degrees;
    private final float radians;

    private Angle(float degrees, float radians) {
        this.degrees = degrees;
        this.radians = radians;
    }

    public static Angle radians(float radians) {
        return new Angle((float) Math.toDegrees(radians), radians);
    }

    public static Angle degrees(float degrees) {
        return new Angle(degrees, (float) Math.toRadians(degrees));
    }

    public float getDegrees() {
        return degrees;
    }

    public float getRadians() {
        return radians;
    }
}
