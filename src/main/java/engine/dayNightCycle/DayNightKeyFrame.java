package engine.dayNightCycle;

import maths.joml.Vector3f;

public final class DayNightKeyFrame {

    private final Vector3f colour;
    private final Hour hour;

    public DayNightKeyFrame(Vector3f colour, Hour hour) {
        this.colour = colour;
        this.hour = hour;
    }

    public Vector3f getColour() {
        return colour;
    }

    public Hour getHour() {
        return hour;
    }
}
