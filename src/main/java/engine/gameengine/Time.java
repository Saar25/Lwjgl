package engine.gameengine;

public class Time {

    private static long current = System.currentTimeMillis();
    private static float delta = 0;

    static void update() {
        delta = (-current + (current = System.currentTimeMillis())) / 1000f;
        Timer.updateTimers();
    }

    /**
     * Returns the delta time since last update, an update occurs once for each frame
     *
     * @return the delta time
     */
    public static float getDelta() {
        return delta;
    }

    /**
     * Returns the interval from last update, an update occurs once for each frame
     *
     * @return the interval
     */
    public static float getInterval() {
        return delta;
    }

    /**
     * Returns the time where the last update occurred
     *
     * @return the last update time
     */
    public static float getUpdateTime() {
        return current;
    }

    /**
     * Returns the current time
     *
     * @return the current time
     */
    public static float getCurrent() {
        return System.currentTimeMillis() / 1000f;
    }
}
