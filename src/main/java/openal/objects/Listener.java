package openal.objects;

import maths.joml.Vector3fc;
import org.lwjgl.openal.AL11;

public class Listener {

    private static final Listener listener = new Listener();

    private Listener() {

    }

    public static Listener getInstance() {
        return listener;
    }

    /**
     * Sets the position of the listener in the 3d world
     *
     * @param x the x component
     * @param y the y component
     * @param z the z component
     */
    public void setPosition(float x, float y, float z) {
        AL11.alListener3f(AL11.AL_POSITION, x, y, z);
    }

    public void setPosition(Vector3fc p) {
        setPosition(p.x(), p.y(), p.z());
    }

    /**
     * Sets the velocity of the listener in the 3d world
     *
     * @param x the x component
     * @param y the y component
     * @param z the z component
     */
    public void setVelocity(float x, float y, float z) {
        AL11.alListener3f(AL11.AL_VELOCITY, x, y, z);
    }

    public void setVelocity(Vector3fc p) {
        setVelocity(p.x(), p.y(), p.z());
    }

    /**
     * Sets the gain of the listener
     *
     * @param gain the gain
     */
    public void setGain(float gain) {
        AL11.alListenerf(AL11.AL_GAIN, gain);
    }

}
