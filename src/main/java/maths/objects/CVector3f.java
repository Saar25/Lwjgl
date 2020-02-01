package maths.objects;

import maths.joml.*;
import maths.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Constant class for vector3f
 */
public class CVector3f implements Vector3fc {

    private final Vector3fc value;

    public CVector3f(float x, float y, float z) {
        this.value = Vector3.of(x, y, z);
    }

    @Override
    public float x() {
        return value.x();
    }

    @Override
    public float y() {
        return value.y();
    }

    @Override
    public float z() {
        return value.z();
    }

    @Override
    public FloatBuffer get(FloatBuffer buffer) {
        return null;
    }

    @Override
    public FloatBuffer get(int index, FloatBuffer buffer) {
        return null;
    }

    @Override
    public ByteBuffer get(ByteBuffer buffer) {
        return null;
    }

    @Override
    public ByteBuffer get(int index, ByteBuffer buffer) {
        return null;
    }

    @Override
    public Vector3fc getToAddress(long address) {
        return null;
    }

    @Override
    public Vector3f sub(Vector3fc v, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f sub(float x, float y, float z, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f add(Vector3fc v, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f add(float x, float y, float z, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f fma(Vector3fc a, Vector3fc b, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f fma(float a, Vector3fc b, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mul(Vector3fc v, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f div(Vector3fc v, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mulProject(Matrix4fc mat, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mul(Matrix3fc mat, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mul(Matrix3dc mat, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mul(Matrix3x2fc mat, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mulTranspose(Matrix3fc mat, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mulPosition(Matrix4fc mat, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mulPosition(Matrix4x3fc mat, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mulTransposePosition(Matrix4fc mat, Vector3f dest) {
        return null;
    }

    @Override
    public float mulPositionW(Matrix4fc mat, Vector3f dest) {
        return 0;
    }

    @Override
    public Vector3f mulDirection(Matrix4dc mat, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mulDirection(Matrix4fc mat, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mulDirection(Matrix4x3fc mat, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mulTransposeDirection(Matrix4fc mat, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mul(float scalar, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f mul(float x, float y, float z, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f div(float scalar, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f div(float x, float y, float z, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f rotate(Quaternionfc quat, Vector3f dest) {
        return null;
    }

    @Override
    public Quaternionf rotationTo(Vector3fc toDir, Quaternionf dest) {
        return null;
    }

    @Override
    public Quaternionf rotationTo(float toDirX, float toDirY, float toDirZ, Quaternionf dest) {
        return null;
    }

    @Override
    public Vector3f rotateAxis(float angle, float aX, float aY, float aZ, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f rotateX(float angle, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f rotateY(float angle, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f rotateZ(float angle, Vector3f dest) {
        return null;
    }

    @Override
    public float lengthSquared() {
        return 0;
    }

    @Override
    public float length() {
        return 0;
    }

    @Override
    public Vector3f normalize(Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f normalize(float length, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f cross(Vector3fc v, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f cross(float x, float y, float z, Vector3f dest) {
        return null;
    }

    @Override
    public float distance(Vector3fc v) {
        return value.distance(v);
    }

    @Override
    public float distance(float x, float y, float z) {
        return 0;
    }

    @Override
    public float distanceSquared(Vector3fc v) {
        return 0;
    }

    @Override
    public float distanceSquared(float x, float y, float z) {
        return 0;
    }

    @Override
    public float dot(Vector3fc v) {
        return 0;
    }

    @Override
    public float dot(float x, float y, float z) {
        return 0;
    }

    @Override
    public float angleCos(Vector3fc v) {
        return 0;
    }

    @Override
    public float angle(Vector3fc v) {
        return 0;
    }

    @Override
    public Vector3f min(Vector3fc v, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f max(Vector3fc v, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f negate(Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f absolute(Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f reflect(Vector3fc normal, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f reflect(float x, float y, float z, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f half(Vector3fc other, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f half(float x, float y, float z, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f smoothStep(Vector3fc v, float t, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f hermite(Vector3fc t0, Vector3fc v1, Vector3fc t1, float t, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f lerp(Vector3fc other, float t, Vector3f dest) {
        return null;
    }

    @Override
    public float get(int component) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public int maxComponent() {
        return 0;
    }

    @Override
    public int minComponent() {
        return 0;
    }

    @Override
    public Vector3f orthogonalize(Vector3fc v, Vector3f dest) {
        return null;
    }

    @Override
    public Vector3f orthogonalizeUnit(Vector3fc v, Vector3f dest) {
        return null;
    }

    @Override
    public boolean equals(Vector3fc v, float delta) {
        return value.equals(v, delta);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
