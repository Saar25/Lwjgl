package engine.terrain;

import engine.models.Model;
import engine.models.Skin;
import engine.rendering.Renderable;
import engine.rendering.Spatial;
import engine.util.property.FloatProperty;
import maths.utils.Maths;

public abstract class Terrain extends Spatial implements Renderable {

    private Model model;
    private final Skin skin;

    private final float size;

    private FloatProperty amplitudeProperty = new FloatProperty(1);

    public Terrain(Model model, Skin skin, float size) {
        this.model = model;
        this.skin = skin;
        this.size = size;
    }

    public Terrain(Skin skin, float size) {
        this.skin = skin;
        this.size = size;
    }

    protected Model createModel() {
        return null;
    }

    @Override
    protected void validate() {
        if (model != null) {
            model.delete();
        }
        this.model = createModel();
    }

    @Override
    public Skin getSkin() {
        return skin;
    }

    @Override
    public Model getModel() {
        if (model == null) {
            model = createModel();
        }
        return model;
    }

    @Override
    public void delete() {
        getModel().delete();
        getSkin().cleanUp();
    }

    public final float getSize() {
        return size;
    }

    /**
     * Returns the amplitude property of the terrain
     *
     * @return the amplitude property of the terrain
     */
    public FloatProperty amplitudeProperty() {
        return amplitudeProperty;
    }

    public float getAmplitude() {
        return amplitudeProperty().get();
    }

    public void setAmplitude(float amplitude) {
        amplitudeProperty().set(amplitude);
    }

    /**
     * Check if x, z coordinates are inside the terrain area
     *
     * @param x the x coordinate
     * @param z the x coordinate
     * @return true if the coordinates are inside the terrain false otherwise
     */
    public boolean isInside(float x, float z) {
        final float tx = getTransform().getPosition().x;
        final float tz = getTransform().getPosition().z;
        return Maths.isInside(x, tx - getSize() / 2f, tx + getSize() / 2f) &&
                Maths.isInside(z, tz - getSize() / 2f, tz + getSize() / 2f);
    }

    /**
     * Return the height of the terrain based on x and z coordinates
     *
     * @param x x coordinate of the position
     * @param z z coordinate of the position
     * @return the y value of the terrain
     */
    public abstract float getHeight(float x, float z);
}
