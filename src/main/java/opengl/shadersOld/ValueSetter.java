package opengl.shadersOld;

@FunctionalInterface
public interface ValueSetter<T> {

    /**
     * Set the first value to the seconds value
     *
     * @param a the first value
     * @param b the second value
     * @return the new value for the first value
     */
    T set(T a, T b);

}
