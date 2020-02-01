package opengl.utils;

import java.nio.FloatBuffer;

public class DynamicFloatBuffer {

    private FloatBuffer buffer;

    public DynamicFloatBuffer(int initialCapacity) {
        this.buffer = MemoryUtils.allocFloat(initialCapacity);
    }

    /**
     * Ensures the capacity of the float buffer.
     * If the capacity of the buffer is too small,
     * a new float buffer will be allocated with a
     * 1.5 times bigger capacity.
     *
     * @param needed the memory needed in the buffer
     * @return the old buffer, if the buffer was not reallocated, null will be returned
     */
    public FloatBuffer ensureCapacity(int needed) {
        if (needed >= buffer.capacity()) {
            final FloatBuffer temp = buffer;
            final int newCapacity = (int) (buffer.capacity() * 1.5);
            this.buffer = MemoryUtils.allocFloat(Math.max(newCapacity, needed));
            return temp;
        }
        return null;
    }

    /**
     * Returns the float buffer
     *
     * @return the float buffer
     */
    public FloatBuffer get() {
        return buffer;
    }
}
