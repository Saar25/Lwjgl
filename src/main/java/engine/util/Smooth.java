package engine.util;

import maths.utils.Quaternion;
import maths.utils.Vector3;
import maths.joml.Quaternionf;
import maths.joml.Vector3f;

public class Smooth<T> implements Reference<T> {

    private final Interpolator<T> interpolator;

    private T target;
    private T value;

    private Smooth(Interpolator<T> interpolator, T value, T target) {
        this.interpolator = interpolator;
        this.target = target;
        this.value = value;
    }

    /**
     * Creates a Smooth
     *
     * @param inter  the function used to interpolate between the values
     * @param value  the starting value
     * @param target the target value
     * @param <T>    the type of the smooth
     * @return a Smooth
     */
    public static <T> Smooth<T> of(Interpolator<T> inter, T value, T target) {
        return new Smooth<>(inter, value, target);
    }

    /**
     * Creates a Smooth Integer
     *
     * @param agility the agility of the interpolation
     * @return a Smooth Integer
     */
    public static Smooth<Integer> ofInteger(float agility) {
        Interpolator<Integer> interpolator = (v, t, s) -> v + (int) ((t - v) * agility * s);
        return new Smooth<>(interpolator, 0, 0);
    }

    /**
     * Creates a Smooth FLOAT
     *
     * @param agility the agility of the interpolation
     * @return a Smooth FLOAT
     */
    public static Smooth<Float> ofFloat(float agility) {
        Interpolator<Float> interpolator = (v, t, s) -> v + (t - v) * agility * s;
        return new Smooth<>(interpolator, 0f, 0f);
    }

    /**
     * Creates a Smooth Vector3f
     *
     * @param agility the agility of the interpolation
     * @return a Smooth Vector3f
     */
    public static Smooth<Vector3f> ofVector3(float agility) {
        Interpolator<Vector3f> interpolator = (v, t, s) -> v.lerp(t, agility * s);
        return new Smooth<>(interpolator, Vector3.create(), Vector3.create());
    }

    /**
     * Creates a Smooth Quaternionf
     *
     * @param agility the agility of the interpolation
     * @return a Smooth Quaternionf
     */
    public static Smooth<Quaternionf> ofQuaternion(float agility) {
        Interpolator<Quaternionf> interpolator = (v, t, s) -> v.slerp(t, agility * s);
        return new Smooth<>(interpolator, Quaternion.create(), Quaternion.create());
    }

    /**
     * Interpolates between the current value and the target value.
     * Based on the agility and the scalar, higher scalar would cause
     * fast interpolation while low scalar would cause slower interpolation
     *
     * @param scalar the scalar
     * @return the interpolated value
     */
    public T interpolate(float scalar) {
        this.value = interpolator.interpolate(value, target, scalar);
        return value;
    }

    /**
     * Returns the current value
     *
     * @return the current value
     */
    @Override
    public T get() {
        return value;
    }

    /**
     * Sets the current value
     *
     * @param value the new value
     */
    public void set(T value) {
        this.value = value;
    }

    /**
     * Sets the current value
     *
     * @param value the new value
     */
    public void set(T value, boolean target) {
        this.value = value;
        if (target) {
            this.target = value;
        }
    }

    /**
     * Returns the target value
     *
     * @return the target value
     */
    public T getTarget() {
        return target;
    }

    /**
     * Sets the target value
     *
     * @param target the target value
     */
    public void setTarget(T target) {
        this.target = target;
    }

    /**
     * Inner functional class to determine how values are interpolated
     *
     * @param <T> the type of the values
     */
    public interface Interpolator<T> {

        T interpolate(T value, T target, float scalar);

    }
}
