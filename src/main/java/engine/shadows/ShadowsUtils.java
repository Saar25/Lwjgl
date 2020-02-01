package engine.shadows;

import maths.joml.Matrix4f;
import maths.joml.Matrix4fc;
import maths.utils.Matrix4;

public final class ShadowsUtils {

    private static final Matrix4fc OFFSET = Matrix4.createIdentity().translate(.5f, .5f, .5f).scale(.5f);

    private ShadowsUtils() {

    }

    public static Matrix4f createSpaceMatrix(Matrix4fc projectionViewMatrix, Matrix4f dest) {
        return dest.set(OFFSET).mul(projectionViewMatrix);
    }

    public static Matrix4f createMVPMatrix(Matrix4fc projectionViewMatrix, Matrix4fc transformationMatrix, Matrix4f dest) {
        return dest.set(OFFSET).mul(projectionViewMatrix).mul(transformationMatrix);
    }

    public static Matrix4f createMVPMatrix(Matrix4fc projectionMatrix, Matrix4fc viewMatrix, Matrix4fc transformationMatrix, Matrix4f dest) {
        return dest.set(OFFSET).mul(projectionMatrix).mul(viewMatrix).mul(transformationMatrix);
    }
}
