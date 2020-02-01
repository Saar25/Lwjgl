package engine.models;

import maths.utils.Maths;
import opengl.objects.IVbo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lod implements ILod {

    private int current = 0;

    private final List<IVbo> indexBuffers = new ArrayList<>();

    public Lod() {

    }

    public Lod(IVbo... indexBuffers) {
        this.indexBuffers.addAll(Arrays.asList(indexBuffers));
    }

    public void addIndexBuffer(IVbo indexBuffer) {
        indexBuffers.add(indexBuffer);
    }

    public void set(int current) {
        this.current = Maths.clamp(current, 0, indexBuffers.size() - 1);
    }

    public void inc() {
        this.set(current + 1);
    }

    public void dec() {
        this.set(current - 1);
    }

    @Override
    public IVbo current() {
        return indexBuffers.get(current);
    }

    @Override
    public boolean available() {
        return !indexBuffers.isEmpty();
    }
}
