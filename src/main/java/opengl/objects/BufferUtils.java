package opengl.objects;

import opengl.constants.VboUsage;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class BufferUtils {

    private BufferUtils() {
        throw new AssertionError("Cannot create instance of buffer utils");
    }

    public static IndexBuffer loadToIndexBuffer(VboUsage usage, IntBuffer data) {
        final IndexBuffer indexBuffer = new IndexBuffer(usage);
        indexBuffer.allocateInt(data.limit());
        indexBuffer.storeData(0, data);
        return indexBuffer;
    }

    public static IndexBuffer loadToIndexBuffer(VboUsage usage, int... data) {
        final IndexBuffer indexBuffer = new IndexBuffer(usage);
        indexBuffer.allocateInt(data.length);
        indexBuffer.storeData(0, data);
        return indexBuffer;
    }

    public static DataBuffer loadToDataBuffer(VboUsage usage, FloatBuffer data) {
        final DataBuffer dataBuffer = new DataBuffer(usage);
        dataBuffer.allocateFloat(data.limit());
        dataBuffer.storeData(0, data);
        return dataBuffer;
    }

    public static DataBuffer loadToDataBuffer(VboUsage usage, float... data) {
        final DataBuffer dataBuffer = new DataBuffer(usage);
        dataBuffer.allocateFloat(data.length);
        dataBuffer.storeData(0, data);
        return dataBuffer;
    }

    public static DataBuffer loadToDataBuffer(VboUsage usage, IntBuffer data) {
        final DataBuffer dataBuffer = new DataBuffer(usage);
        dataBuffer.allocateInt(data.limit());
        dataBuffer.storeData(0, data);
        return dataBuffer;
    }

    public static DataBuffer loadToDataBuffer(VboUsage usage, int... data) {
        final DataBuffer dataBuffer = new DataBuffer(usage);
        dataBuffer.allocateInt(data.length);
        dataBuffer.storeData(0, data);
        return dataBuffer;
    }

    public static DataBuffer loadToDataBuffer(VboUsage usage, ByteBuffer data) {
        final DataBuffer dataBuffer = new DataBuffer(usage);
        dataBuffer.allocateData(data.limit());
        dataBuffer.storeData(0, data);
        return dataBuffer;
    }

}
