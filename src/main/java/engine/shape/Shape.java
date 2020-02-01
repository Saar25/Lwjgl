package engine.shape;

import engine.componentsystem.ComponentBased;
import engine.componentsystem.ComponentGroup;
import engine.models.Model;
import engine.models.Skin;
import engine.rendering.Renderable;
import engine.rendering.Spatial;
import engine.shape.lowpoly.LowPolyShapeRenderer;
import engine.shape.smooth.SmoothShapeRenderer;
import tegui.style.property.Colour;

public abstract class Shape extends Spatial implements ComponentBased, Renderable {

    private Model model;
    private final Skin skin;
    private final Colour colour;

    private final ComponentGroup components = new ComponentGroup(this);

    private boolean lowPoly = true;

    public Shape() {
        this.skin = Skin.create();
        this.model = createModel();
        this.colour = new Colour(.1f, .1f, 1f);
    }

    public Colour getColour() {
        return colour;
    }

    public void setLowPoly(boolean lowPoly) {
        this.lowPoly = lowPoly;
    }

    protected abstract Model createModel();

    @Override
    public ComponentGroup getComponents() {
        return components;
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
    public void process() {
        if (lowPoly) {
            LowPolyShapeRenderer.getInstance().process(this);
        } else {
            SmoothShapeRenderer.getInstance().process(this);
        }
    }

    @Override
    public void update() {
        getComponents().update();
    }

    @Override
    public void delete() {
        model.delete();
    }
}
