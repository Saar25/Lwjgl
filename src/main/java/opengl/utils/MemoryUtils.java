package opengl.utils;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public final class MemoryUtils {

    private MemoryUtils() {

    }

    public static FloatBuffer loadToFloatBuffer(List<Float> data) {
        FloatBuffer buffer = allocFloat(data.size());
        data.forEach(buffer::put);
        buffer.flip();
        return buffer;
    }

    public static FloatBuffer loadToFloatBuffer(float[] data) {
        FloatBuffer buffer = allocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static IntBuffer loadToIntBuffer(List<Integer> data) {
        IntBuffer buffer = allocInt(data.size());
        data.forEach(buffer::put);
        buffer.flip();
        return buffer;
    }


    public static IntBuffer loadToIntBuffer(int[] data) {
        IntBuffer buffer = allocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static ByteBuffer loadToByteBuffer(List<Byte> data) {
        ByteBuffer buffer = allocByte(data.size());
        data.forEach(buffer::put);
        buffer.flip();
        return buffer;
    }

    public static ByteBuffer loadToByteBuffer(byte[] data) {
        ByteBuffer buffer = allocByte(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static DoubleBuffer allocDouble(int size) {
        return MemoryUtil.memAllocDouble(size);
    }

    public static FloatBuffer allocFloat(int size) {
        return MemoryUtil.memAllocFloat(size);
    }

    public static IntBuffer allocInt(int size) {
        return MemoryUtil.memAllocInt(size);
    }

    public static IntBuffer callocInt(int size) {
        return MemoryUtil.memCallocInt(size);
    }

    public static ByteBuffer allocByte(int size) {
        return MemoryUtil.memAlloc(size);
    }

}
