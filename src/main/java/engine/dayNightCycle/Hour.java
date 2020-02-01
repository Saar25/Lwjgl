package engine.dayNightCycle;

public final class Hour implements Comparable<Hour> {

    public static final int HOURS_IN_DAY = 24;
    public static final int MINUTES_IN_DAY = 60 * 24;
    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final Hour MIDNIGHT = Hour.of(0, 0);

    private final int hours;
    private final int minutes;
    private final int seconds;

    private Hour(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public static Hour of(int hour, int minutes, int seconds) {
        return new Hour(hour % 24, minutes % 60, seconds);
    }

    public static Hour of(int hour, int minutes) {
        return Hour.of(hour, minutes, 0);
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public boolean isAfter(Hour hour) {
        return compareTo(hour) > 0;
    }

    public Hour after(int hours, int minutes) {
        minutes += hours * 60;
        int h = this.hours + (this.minutes + minutes) / 60;
        int m = this.minutes + minutes;
        return Hour.of(h, m);
    }

    public float getNormalizedTime() {
        float time = 0;
        time += (float) hours / HOURS_IN_DAY;
        time += (float) minutes / MINUTES_IN_DAY;
        time += (float) seconds / SECONDS_IN_DAY;
        return time;
    }

    public int getHourInSeconds() {
        return hours * 60 * 60 + minutes * 60 + seconds;
    }

    @Override
    public int compareTo(Hour h) {
        return getHourInSeconds() - h.getHourInSeconds();
    }

    @Override
    public String toString() {
        return "Hour: " +
                ((this.hours < 10 ? "0" + this.hours : this.hours) + ":") +
                ((this.minutes < 10 ? "0" + this.minutes : this.minutes) + ":") +
                ((this.seconds < 10 ? "0" + this.seconds : this.seconds) + ":");
    }
}
