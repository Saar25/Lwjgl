package engine.gameengine;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class Timer {

    private static final List<WeakReference<Timer>> timers = new ArrayList<>();

    private boolean running = true;

    static void updateTimers() {
        for (WeakReference<Timer> timerRef : timers) {
            final Timer timer = timerRef.get();
            if (timer != null) {
                timer.update();
            } else {
                timers.remove(timerRef);
            }
        }
    }

    private static Timer create(Timer timer) {
        timers.add(new WeakReference<>(timer));
        return timer;
    }

    /**
     * Creates a new Countdown timer
     *
     * @return a new Countdown timer
     */
    public static Timer createCountdown(float time) {
        return create(new Countdown(time));
    }


    /**
     * Creates a new Looping timer
     *
     * @return a new Looping timer
     */
    public static Timer createLoopingTimer(float delay) {
        return create(new Looping(delay));
    }


    /**
     * Creates a new Random timer
     *
     * @return a new Random timer
     */
    public static Timer createRandomTimer(float chance) {
        return create(new Random(chance));
    }

    /**
     * Update the times
     */
    protected abstract void update();

    /**
     * Check if the timer is ready
     *
     * @return true if ready, false otherwise
     */
    public abstract boolean check();

    /**
     * Stops the timer from updating
     * <p>
     * Call this method before removing the reference to it so it will not update forever
     * and the gc will be able to collect it
     */
    public final void stop() {
        if (running) {
            running = false;
            timers.removeIf(r -> r.get() == this);
        }
    }

    /**
     * Start updating the timer, does nothing if timer is already running
     */
    public final void start() {
        if (!running) {
            running = true;
            timers.add(new WeakReference<>(this));
        }
    }

    private static class Looping extends Timer {

        private final float delay;
        private float currentTime = 0;

        private Looping(float delay) {
            this.delay = delay;
        }

        @Override
        public void update() {
            currentTime %= delay;
            currentTime += Time.getDelta();
        }

        @Override
        public boolean check() {
            return currentTime >= delay;
        }
    }

    private static class Countdown extends Timer {

        private float time;

        private Countdown(float time) {
            this.time = time;
        }

        @Override
        public void update() {
            this.time -= Time.getDelta();
        }

        @Override
        public boolean check() {
            return time <= 0;
        }
    }

    private static class Random extends Timer {

        private final float chance;
        private float random;

        private Random(float chance) {
            this.chance = chance;
        }

        @Override
        public void update() {
            this.random = (float) Math.random();
        }

        @Override
        public boolean check() {
            return random < chance;
        }
    }
}
