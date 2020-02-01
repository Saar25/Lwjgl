package maths.objects;

import maths.Angle;
import maths.joml.*;
import maths.utils.Matrix4;
import maths.utils.Quaternion;
import maths.utils.Vector3;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public final class Transform implements Transformable {

    private final Matrix4f transformation = Matrix4.create();

    private final Quaternionf staticRotation = Quaternion.create();

    private final RelativeTransform localTransform = new RelativeTransform();
    private final RelativeTransform worldTransform = new RelativeTransform();

    private final List<Transform> transformations = new ArrayList<>();

    public Transform() {

    }

    public Transform(Vector3fc position) {
        getPosition().set(position);
    }

    public Transform(Transform transform) {
        localTransform.getPosition().set(transform.localTransform.getPosition());
        localTransform.getRotation().set(transform.localTransform.getRotation());
        localTransform.getScale().set(transform.localTransform.getScale());
    }

    @Override
    public Transform getTransform() {
        return this;
    }

    public Matrix4f getTransformationMatrix() {
        localTransform.getTransformationMatrix()
                .rotate(staticRotation, transformation);
        for (Transform transform : transformations) {
            transform.getTransformationMatrix()
                    .mul(transformation, transformation);
        }
        return transformation;
    }

    public void addTransformation(Transform transform) {
        this.transformations.add(transform);
    }

    public void removeTransformation(Transform transform) {
        this.transformations.remove(transform);
    }

    /*
     *  Position management
     */
    public Vector3f getPosition() {
        return localTransform.getPosition();
    }

    public Vector3f getWorldPosition() {
        worldTransform.setPosition(localTransform.getPosition());
        for (Transform transform : transformations) {
            worldTransform.addPosition(transform.getWorldPosition());
        }
        return worldTransform.getPosition();
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
     *  Rotation management
     */
    public Quaternionf getStaticRotation() {
        return staticRotation;
    }

    public Quaternionf getRotation() {
        return localTransform.getRotation();
    }

    public void setRotation(Quaternionfc rotation) {
        getRotation().set(rotation);
    }

    public Vector3f getEulerAngles() {
        return localTransform.getEulerAngles();
    }

    public void setRotation(Angle x, Angle y, Angle z) {
        getRotation().identity().rotateXYZ(x.getRadians(),
                y.getRadians(), z.getRadians());
    }

    public void addRotation(Angle x, Angle y, Angle z) {
        getRotation().rotateX(x.getRadians());
        getRotation().rotateLocalY(y.getRadians());
        getRotation().rotateZ(z.getRadians());
    }

    public void setRotation(float x, float y, float z) {
        getRotation().identity().rotateXYZ(
                (float) Math.toRadians(x),
                (float) Math.toRadians(y),
                (float) Math.toRadians(z));
    }

    public void addRotation(float x, float y, float z) {
        getRotation().rotateX((float) Math.toRadians(x));
        getRotation().rotateLocalY((float) Math.toRadians(y));
        getRotation().rotateZ((float) Math.toRadians(z));
    }

    public void lookAlong(Vector3fc direction) {
        direction = Vector3.normalize(direction);
        if (direction.equals(Vector3.DOWN, 0)) {
            getRotation().set(-1, 0, 0, 1).normalize();
        } else if (!direction.equals(Vector3.ZERO, 0)) {
            getRotation().identity().lookAlong(direction, Vector3.UP).conjugate();
        }
    }

    public void lookAt(Vector3fc position) {
        Vector3f direction = Vector3.sub(position, getPosition());
        lookAlong(direction);
    }

    public Vector3f getDirection() {
        return Vector3.forward().rotate(getRotation());
    }

    /*
     *  Scale management
     */
    public Vector3f getScale() {
        worldTransform.getScale().set(localTransform.getScale());
        for (Transform transform : transformations) {
            worldTransform.getScale().mul(transform.getScale());
        }
        return worldTransform.getScale();
    }

    public void setScale(Vector3f scale) {
        localTransform.getScale().set(scale);
    }

    public void setScale(float x, float y, float z) {
        localTransform.getScale().set(x, y, z);
    }

    public void setScale(float scale) {
        localTransform.getScale().set(scale);
    }

    public void addScale(Vector3f scale) {
        localTransform.getScale().add(scale);
    }

    public void addScale(float scale) {
        localTransform.getScale().add(scale, scale, scale);
    }

    public void scaleBy(float scale) {
        localTransform.getScale().mul(scale);
    }

    public void set(Transform transform) {
        setPosition(transform.getPosition());
        setRotation(transform.getRotation());
        setScale(transform.getScale());
    }

    @Override
    public String toString() {
        return localTransform.toString();
    }

}
