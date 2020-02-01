package engine.skybox;

import engine.gameengine.Time;
import engine.models.Skin;
import engine.rendering.Spatial;
import maths.joml.Vector3fc;
import opengl.textures.CubeMapTexture;
import tegui.style.property.Colour;
import tegui.style.property.IColour;

public class SkyBox extends Spatial {

    private final Skin skin;
    private IColour skyColour = Colour.WHITE;
    private IColour fogColour = new Colour(0, 0, 0, 0);

    public SkyBox(CubeMapTexture texture) {
        this.skin = Skin.of(texture);
    }

    public Skin getSkin() {
        return skin;
    }

    public IColour getSkyColour() {
        return skyColour;
    }

    public IColour getFogColour() {
        return fogColour;
    }

    public void setSkyColour(IColour skyColour) {
        this.skyColour = skyColour;
    }

    public void setSkyColour(Vector3fc skyColour) {
        this.skyColour = new Colour(skyColour.x(), skyColour.y(), skyColour.z());
    }

    public void setFogColour(IColour fogColour) {
        this.fogColour = fogColour;
    }

    public void setFogColour(Vector3fc fogColour) {
        this.fogColour = new Colour(fogColour.x(), fogColour.y(), fogColour.z());
    }

    @Override
    public void process() {
        SkyBoxRenderer.getInstance().process(this);
    }

    @Override
    public void update() {
        float angle = Time.getDelta() * 0.03f;
        getTransform().getRotation().rotateY(angle);
    }

    @Override
    public void delete() {
        skin.cleanUp();
    }
}
