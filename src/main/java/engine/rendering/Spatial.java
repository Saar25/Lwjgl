package engine.rendering;

import engine.util.ObservableList;
import engine.util.node.IParent;
import engine.util.property.ObjectProperty;
import engine.util.property.Property;
import glfw.input.event.ClickEvent;
import glfw.input.event.MoveEvent;
import glfw.input.event.ScrollEvent;
import maths.objects.Transform;
import maths.objects.Transformable;

/**
 * Base class for every object in the scene
 *
 * @author Saar ----
 * @version 1.1
 * @since 28.12.2018
 */
public abstract class Spatial implements Transformable {

    private final ObjectProperty<IParent> parentProperty = new ObjectProperty<>();

    private final ObservableList<Property<?>> properties = ObservableList.observableArrayList();

    private final Transform transform = new Transform();

    public Spatial() {
        getProperties().addListener(change -> {
            for (Property<?> property : change.getAdded()) {
                SpatialValidationBinding.bind(property, this);
            }
            for (Property<?> property : change.getRemoved()) {
                SpatialValidationBinding.unbind(property, this);
            }
        });
    }

    /**
     * Returns the transform of the spatial
     *
     * @return the transform of the spatial
     */
    @Override
    public final Transform getTransform() {
        return transform;
    }

    /**
     * Returns the properties of the object
     *
     * @return the properties of the object
     */
    public final ObservableList<Property<?>> getProperties() {
        return properties;
    }

    /**
     * Returns the parent property of the spatial
     *
     * @return the parent property
     */
    public final ObjectProperty<IParent> parentProperty() {
        return parentProperty;
    }

    public final IParent getParent() {
        return parentProperty().getValue();
    }

    public final void setParent(IParent parent) {
        if (parentProperty().get() != null) {
            if (parentProperty().get() instanceof Spatial) {
                final Transform transform = ((Spatial) parentProperty().get()).getTransform();
                getTransform().removeTransformation(transform);
            }
        }
        parentProperty().set(parent);
        if (parent instanceof Spatial) {
            final Transform transform = ((Spatial) parent).getTransform();
            getTransform().addTransformation(transform);
        }
    }

    /**
     * Validate the spatial
     * Invoked after property change
     */
    protected void validate() {

    }

    /**
     * Process the spatial
     * Invoked before rendering the spatial object
     */
    public void process() {

    }

    /**
     * Update the spatial
     * Invoked once every frame
     */
    public void update() {

    }

    /**
     * Delete the spatial
     * Invoked when the program closes
     * Invoked on the main gl thread
     */
    public void delete() {

    }

    /**
     * Invoked when the mouse move
     *
     * @param event the move event
     */
    public void onMouseMoveEvent(MoveEvent event) {

    }

    /**
     * Invoked when the mouse click (press or release)
     *
     * @param event the click event
     */
    public void onMouseClickEvent(ClickEvent event) {

    }

    /**
     * Invoked when the mouse scroll
     *
     * @param event the scroll event
     */
    public void onMouseScrollEvent(ScrollEvent event) {

    }

}
