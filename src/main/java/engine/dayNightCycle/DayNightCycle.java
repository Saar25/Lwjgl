package engine.dayNightCycle;

import maths.utils.Maths;
import maths.utils.Vector3;
import maths.joml.Vector3f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DayNightCycle {

    private Hour currentHour;
    private Vector3f currentColour;
    private List<DayNightKeyFrame> keyFrames;

    public DayNightCycle() {
        this.currentHour = Hour.MIDNIGHT;
        this.currentColour = Vector3.create();
        this.keyFrames = new ArrayList<>();
    }

    /**
     * Updates the hour of the day and determine the current colour
     *
     * @param minutes the time past since last call in minutes
     */
    public void update(int minutes) {
        this.currentHour = currentHour.after(0, minutes);

        if (keyFrames.size() == 1) currentColour = keyFrames.get(0).getColour();
        if (keyFrames.size() <= 1) return;

        int index = 0;
        for (DayNightKeyFrame kf : keyFrames) {
            if (currentHour.isAfter(kf.getHour())) index++;
            else break;
        }
        index %= keyFrames.size();

        final int before = index - 1 < 0 ? keyFrames.size() - 1 : index - 1;
        final DayNightKeyFrame keyFrame1 = keyFrames.get(before);
        final DayNightKeyFrame keyFrame2 = keyFrames.get(index);

        float maxTime = (keyFrame2.getHour().compareTo(keyFrame1.getHour()) + Hour.SECONDS_IN_DAY) % Hour.SECONDS_IN_DAY;
        float factor = ((currentHour.compareTo(keyFrame1.getHour()) + Hour.SECONDS_IN_DAY) % Hour.SECONDS_IN_DAY) / maxTime;
        factor = (float) Math.pow(factor, 6);

        final Vector3f colour1 = keyFrame1.getColour();
        final Vector3f colour2 = keyFrame2.getColour();
        this.currentColour = Maths.mix(colour1, colour2, factor);
    }

    /**
     * Adds a {@link DayNightKeyFrame} to the keyFrames and sorts them according to their hour
     *
     * @param keyFrame the keyFrame to be added
     */
    public void addKeyFrame(DayNightKeyFrame keyFrame) {
        keyFrames.add(keyFrame);
        keyFrames.sort(Comparator.comparing(DayNightKeyFrame::getHour));
    }

    public Hour getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(Hour currentHour) {
        this.currentHour = currentHour;
    }

    public Vector3f getCurrentColour() {
        return currentColour;
    }
}
