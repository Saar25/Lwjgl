package engine.dayNightCycle;

import engine.gameengine.Time;
import engine.util.property.FloatProperty;
import maths.joml.Vector3f;

public class DayNightManager {

    private final DayNightCycle dayNightCycle;
    private final float secondsPerMinute;
    private float seconds = 0;

    private final FloatProperty normalizedTimeProperty = new FloatProperty();

    public DayNightManager(float secondsPerMinute) {
        this.dayNightCycle = new DayNightCycle();
        this.secondsPerMinute = secondsPerMinute;
        dayNightCycle.update(0);
    }

    public void addKeyFrame(DayNightKeyFrame keyFrame) {
        dayNightCycle.addKeyFrame(keyFrame);
    }

    public void addKeyFrame(Vector3f colour, Hour hour) {
        dayNightCycle.addKeyFrame(new DayNightKeyFrame(colour, hour));
    }

    public void update() {
        seconds += Time.getDelta();
        if (seconds >= secondsPerMinute) {
            int ds = (int) (seconds / secondsPerMinute);
            seconds -= secondsPerMinute * ds;
            dayNightCycle.update(ds);
        }
        normalizedTimeProperty.set(getCurrentHour().getNormalizedTime());
    }

    public FloatProperty normalizedTimeProperty() {
        return normalizedTimeProperty;
    }

    public Vector3f getCurrentColour() {
        return dayNightCycle.getCurrentColour();
    }

    public Hour getCurrentHour() {
        return dayNightCycle.getCurrentHour();
    }

    public void setCurrentHour(Hour hour) {
        dayNightCycle.setCurrentHour(hour);
    }
}
