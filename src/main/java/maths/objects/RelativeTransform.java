package maths.objects;

import maths.joml.Matrix4f;
import maths.joml.Quaternionf;
import maths.joml.Vector3f;
import maths.joml.Vector3fc;
import maths.utils.Matrix4;
import maths.utils.Quaternion;
import maths.utils.Vector3;

public final class RelativeTransform {

    private final Matrix4f transformation = Matrix4.create();

    private final Vector3f position = Vector3.create();

    private final Quaternionf rotation = Quaternion.create();

    private final Vector3f scale = Vector3.of(1, 1, 1);

    private final Vector3f eulerAngles = Vector3.of(0, 0, 0);

    private boolean changed;

    RelativeTransform() {

    }

    RelativeTransform(Vector3fc position) {
        this.position.set(position);
    }

    RelativeTransform(Vector3f position, Quaternionf rotation, Vector3f scale) {
        this.position.set(position);
        this.rotation.set(rotation);
        this.scale.set(scale);
    }

    RelativeTransform(RelativeTransform transform) {
        this.position.set(transform.position);
        this.rotation.set(transform.rotation);
        this.scale.set(transform.scale);
    }

    public Matrix4f getTransformationMatrix() {
        if (changed) {
            Matrix4.ofTransformation(position, rotation, scale, transformation);
            changed = false;
        }
        return transformation;
    }

    private void changed() {
        this.changed = true;
    }

    /*
     *  Position handling
     */
    public Vector3f getPosition() {
        this.changed();
        return position;
    }

    public void setPosition(Vector3fc v) {
        getPosition().set(v);
    }

    public void addPosition(Vector3fc v) {
        getPosition().add(v);
    }

    public void setPosition(float x, float y, float z) {
        getPosition().set(x, y, z);
    }

    public void addPosition(float x, float y, float z) {
        getPosition().add(x, y, z);
    }

    /*
     *  Rotation handling
     */
    public Quaternionf getRotation() {
        this.changed();
        return rotation;
    }

    public Vector3f getEulerAngles() {
        return rotation.getEulerAnglesXYZ(eulerAngles);
    }

    public void setRotation(Quaternionf r) {
        getRotation().set(r);
    }

    public void setRotation(float x, float y, float z) {
        getRotation().identity();
        addRotation(x, y, z);
    }

    public void addRotation(float x, float y, float z) {
        getRotation().rotateXYZ(
                (float) Math.toRadians(x),
                (float) Math.toRadians(y),
                (float) Math.toRadians(z));
    }

    /*
     *  Scale handling
     */
    public Vector3f getScale() {
        this.changed();
        return scale;
    }

    public void setScale(Vector3f scale) {
        getScale().set(scale);
    }

    public void setScale(float scale) {
        getScale().set(scale);
    }

    public void addScale(Vector3f scale) {
        getScale().add(scale);
    }

    public void addScale(float scale) {
        getScale().add(scale, scale, scale);
    }

    public void mulScale(float scale) {
        getScale().mul(scale);
    }

    public void set(Transform transform) {
        setPosition(transform.getPosition());
        setRotation(transform.getRotation());
        setScale(transform.getScale());
    }

    public RelativeTransform transform(RelativeTransform transform) {
        final Vector3f position = Vector3.add(getPosition(), transform.getPosition());
        final Quaternionf rotation = Quaternion.mul(getRotation(), transform.getRotation());
        final Vector3f scale = Vector3.mul(getScale(), transform.getScale());
        return new RelativeTransform(position, rotation, scale);
    }

    @Override
    public String toString() {
        return "[Transform: Position= " + getPosition() + ", Rotation= "
                + getRotation() + ", Scale= " + getScale() + "]";

    }

}
