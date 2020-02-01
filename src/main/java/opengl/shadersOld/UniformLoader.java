package opengl.shadersOld;

@FunctionalInterface
public interface UniformLoader<T> {

    /**
     * Loads a value to the uniform given the location of the uniform and the new value
     *
     * @param location the uniform's location
     * @param value    the new value for the uniform
     */
    void load(int location, T value);

}
