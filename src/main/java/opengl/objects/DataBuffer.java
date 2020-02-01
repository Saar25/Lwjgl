package opengl.objects;

import opengl.constants.VboAccess;
import opengl.constants.VboTarget;
import opengl.constants.VboUsage;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class DataBuffer implements IVbo {

    public static final DataBuffer NULL = new DataBuffer(Vbo.NULL_ARRAY);

    private final Vbo vbo;

    private DataBuffer(Vbo vbo) {
        this.vbo = vbo;
    }

    public DataBuffer(VboUsage usage) {
        this.vbo = Vbo.create(VboTarget.ARRAY_BUFFER, usage);
    }

    private Vbo getVbo() {
        return vbo;
    }

    public void allocateFloat(long size) {
        getVbo().allocateFloat(size);
    }

    public void allocateInt(long size) {
        getVbo().allocateInt(size);
    }

    public void allocateData(long size) {
        getVbo().allocateData(size);
    }

    public void storeData(int pointer, FloatBuffer data) {
        getVbo().storeData(pointer, data);
    }

    public void storeData(int pointer, float[] data) {
        getVbo().storeData(pointer, data);
    }

    public void storeData(int pointer, IntBuffer data) {
        getVbo().storeData(pointer, data);
    }

    public void storeData(int pointer, int[] data) {
        getVbo().storeData(pointer, data);
    }

    public void storeData(int pointer, ByteBuffer data) {
        getVbo().storeData(pointer, data);
    }

    public ByteBuffer map(VboAccess access) {
        return getVbo().map(access);
    }

    public void unmap() {
        getVbo().unmap();
    }

    @Override
    public long getSize() {
        return getVbo().getSize();
    }

    @Override
    public void bind() {
        getVbo().bind();
    }

    @Override
    public void bindToVao(Vao vao) {
        getVbo().bindToVao(vao);
    }

    @Override
    public void unbind() {
        getVbo().unbind();
    }

    @Override
    public void delete() {
        getVbo().delete();
    }
}
