package opengl.objects;

import opengl.constants.DataType;
import opengl.utils.GlConfigs;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL33;

public class Attribute {

    private final int attributeIndex;
    private final int componentCount;
    private final int componentsType;
    private final int bytesPerVertex;
    private final boolean normalized;
    private final int instances;

    private boolean enabled = false;

    private Attribute(int attributeIndex, int componentCount, DataType dataType, boolean normalized) {
        this(attributeIndex, componentCount, dataType, normalized, 0);
    }

    private Attribute(int attributeIndex, int componentCount, DataType dataType, boolean normalized, int instances) {
        this.attributeIndex = attributeIndex;
        this.componentCount = componentCount;
        this.componentsType = dataType.get();
        this.normalized = normalized;
        this.instances = instances;
        this.bytesPerVertex = dataType.getBytes() * componentCount;
    }

    public static Attribute of(int attributeIndex, int componentCount, DataType dataType, boolean normalized) {
        return new Attribute(attributeIndex, componentCount, dataType, normalized);
    }

    public static Attribute ofInstance(int attributeIndex, int componentCount,
                                       DataType dataType, boolean normalized) {
        return new Attribute(attributeIndex, componentCount, dataType, normalized, 1);
    }

    public static Attribute ofInstances(int attributeIndex, int componentCount,
                                        DataType dataType, boolean normalized, int instances) {
        return new Attribute(attributeIndex, componentCount, dataType, normalized, instances);
    }

    public static Attribute ofPositions() {
        return new Attribute(0, 3, DataType.FLOAT, false);
    }

    public static Attribute ofTexCoords() {
        return new Attribute(1, 2, DataType.FLOAT, false);
    }

    public static Attribute ofNormals() {
        return new Attribute(2, 3, DataType.FLOAT, false);
    }

    public static void enable(int index) {
        GL20.glEnableVertexAttribArray(index);
    }

    public static void disable(int index) {
        GL20.glDisableVertexAttribArray(index);
    }

    public void enable() {
        if (GlConfigs.CACHE_STATE || !enabled) {
            GL20.glEnableVertexAttribArray(attributeIndex);
            enabled = true;
        }
    }

    public void disable() {
        if (GlConfigs.CACHE_STATE || enabled) {
            GL20.glDisableVertexAttribArray(attributeIndex);
            enabled = false;
        }
    }

    public void link(int stride, int offset) {
        GL20.glVertexAttribPointer(attributeIndex, componentCount, componentsType, normalized, stride, offset);
        if (instances > 0) {
            GL33.glVertexAttribDivisor(attributeIndex, instances);
        }
    }

    public int getAttributeIndex() {
        return attributeIndex;
    }

    public int getComponentCount() {
        return componentCount;
    }

    public int getComponentsType() {
        return componentsType;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public int getBytesPerVertex() {
        return bytesPerVertex;
    }

    public int getInstances() {
        return instances;
    }
}
