package engine.shadows;

import engine.light.DirectionalLight;
import engine.rendering.Camera;
import engine.rendering.camera.OrthographicProjection;
import maths.joml.Matrix4f;
import maths.joml.Matrix4fc;
import maths.joml.Vector3f;
import maths.utils.Vector3;
import opengl.textures.Texture;

public class Sun {

    private final Camera viewCamera;
    private final Texture shadowMap;
    private final DirectionalLight sunLight;

    private boolean enabled = true;

    public Sun(Texture shadowMap, DirectionalLight sunLight) {
        this.viewCamera = new Camera();
        this.shadowMap = shadowMap;
        this.sunLight = sunLight;
    }

    /**
     * Updates the sun to be related to the center
     *
     * @param camera the camera
     */
    public void update(Camera camera) {
        final Vector3f corner = Vector3.create();
        final Vector3f centroid = Vector3.create();
        for (int i = 0; i < 8; i++) {
            centroid.add(camera.getProjectionViewMatrix().frustumCorner(i, corner));
        }
        centroid.div(8);

        getViewCamera().update();
        getViewCamera().getTransform().setPosition(sunLight.getDirection());
        getViewCamera().getTransform().getPosition().normalize();
        getViewCamera().getTransform().lookAt(Vector3.ZERO);
        getViewCamera().getTransform().addPosition(camera.getTransform().getPosition());
        //getViewCamera().getTransform().addPosition(centroid);

        getViewCamera().updateViewMatrix();
        getViewCamera().updateProjectionViewMatrix();
    }

    /**
     * Sets the mvpMatrix to sun space of the transformation
     *
     * @param tMatrix   the transformation matrix
     * @param mvpMatrix the result destination
     * @return the modified mvpMatrix
     */
    public Matrix4f toViewSpace(Matrix4fc tMatrix, Matrix4f mvpMatrix) {
        final Matrix4fc projectionView = getViewCamera().getProjectionViewMatrix();
        return ShadowsUtils.createMVPMatrix(projectionView, tMatrix, mvpMatrix);
    }

    public Matrix4f getSpaceMatrix(Matrix4f spaceMatrix) {
        final Matrix4fc projectionView = getViewCamera().getProjectionViewMatrix();
        return ShadowsUtils.createSpaceMatrix(projectionView, spaceMatrix);
    }

    /**
     * Sets the maximum distance the shadows will appear
     *
     * @param s the shadow distance
     */
    public void setShadowDistance(float s) {
        getViewCamera().setProjection(new OrthographicProjection(-s, s, -s, s, -s, s));
    }

    /**
     * Returns the camera that represents the sun's view
     *
     * @return the view camera of the sun
     */
    public Camera getViewCamera() {
        return viewCamera;
    }

    /**
     * Returns the texture that contains the z buffer of the shadow space render
     *
     * @return the depth texture
     */
    public Texture getShadowMap() {
        return shadowMap;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return false;
        //return enabled;
    }
}
