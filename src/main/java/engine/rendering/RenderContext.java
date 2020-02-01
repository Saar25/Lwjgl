package engine.rendering;

import engine.engineObjects.Fog;
import engine.light.Light;
import engine.shadows.Sun;
import opengl.objects.ClipPlane;

import java.util.ArrayList;
import java.util.List;

public class RenderContext {

    private final List<Light> lights = new ArrayList<>();

    private Camera camera = null;
    private ClipPlane clipPlane = ClipPlane.NONE;
    private float shadowDistance = 0;
    private float shadowBias = .003f;
    private float shadowPower = 0;
    private Fog fog = Fog.NONE;
    private Sun sun = null;

    private RenderOutputData outputData;

    public List<Light> getLights() {
        return lights;
    }

    public void setLights(List<Light> lights) {
        this.lights.clear();
        this.lights.addAll(lights);
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public ClipPlane getClipPlane() {
        return clipPlane;
    }

    public void setClipPlane(ClipPlane clipPlane) {
        this.clipPlane = clipPlane;
    }

    public float getShadowDistance() {
        return shadowDistance;
    }

    public void setShadowDistance(float shadowDistance) {
        this.shadowDistance = shadowDistance;
    }

    public float getShadowBias() {
        return shadowBias;
    }

    public void setShadowBias(float shadowBias) {
        this.shadowBias = shadowBias;
    }

    public float getShadowPower() {
        return shadowPower;
    }

    public void setShadowPower(float shadowPower) {
        this.shadowPower = shadowPower;
    }

    public Fog getFog() {
        return fog;
    }

    public void setFog(Fog fog) {
        this.fog = fog;
    }

    public Sun getSun() {
        return sun;
    }

    public void setSun(Sun sun) {
        this.sun = sun;
    }

    public RenderOutputData getOutputData() {
        return outputData;
    }

    public void setOutputData(RenderOutputData outputData) {
        this.outputData = outputData;
    }
}