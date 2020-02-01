package engine.rendering;

import maths.joml.Matrix4f;
import maths.joml.Vector3fc;
import maths.joml.Vector4f;
import maths.utils.Maths;
import maths.utils.Vector4;

public abstract class Renderer3D<T> extends Renderer<T> {

    /**
     * Check is the given position is beyond the clipping plane
     *
     * @param position the position to check
     * @return true if the box is beyond the clipping plane false otherwise
     */
    protected boolean checkClippingCulling(Vector3fc position) {
        return Renderer.getContext().getClipPlane().getValue()
                .dot(position.x(), position.y(), position.z(), 1) < -5;
    }

    /**
     * Check is the given box is inside the frustum
     *
     * @param mvpMatrix the model-view-projection matrix the current box
     * @return true if the box is inside the frustum false otherwise
     */
    protected boolean checkFrustumCulling(Matrix4f mvpMatrix) {
        Vector4f ndc = Vector4.pool.poolAndGive();
        ndc.set(0, 0, 0, 1).mul(mvpMatrix);
        return checkFrustumCulling(ndc);
    }

    private boolean checkFrustumCulling(Vector4f ndc) {
        float f = 1.5f;
        return Maths.isBetween(ndc.x / ndc.w, -f, f) &&
                Maths.isBetween(ndc.y / ndc.w, -f, f) &&
                Maths.isBetween(ndc.z / ndc.w, -f + 1, 1);
    }

}
