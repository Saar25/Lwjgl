package tegui;

import engine.util.ObservableList;
import glfw.input.MouseButton;
import glfw.input.event.ClickEvent;
import glfw.input.event.MoveEvent;
import glfw.input.event.ScrollEvent;
import tegui.event.MouseEvent;

public class TContainer extends TParent {

    private final ObservableList<TControl> children = ObservableList.observableArrayList();

    public TContainer() {
        getChildren().addListener(change -> {
            for (TControl component : change.getAdded()) {
                component.setParent(this);
            }
            for (TControl component : change.getRemoved()) {
                component.setParent(null);
            }
        });
    }

    @Override
    public void onMouseMoveEvent(MoveEvent moveEvent) {
        final MouseEvent event = MouseEvent.create(moveEvent);
        invokeMouseEnter(event);
        invokeMouseExit(event);
        if (moveEvent.getMouse().isButtonDown(MouseButton.PRIMARY)) {
            invokeMouseDrag(event);
        } else {
            invokeMouseMove(event);
        }
    }

    @Override
    public void onMouseClickEvent(ClickEvent clickEvent) {
        final MouseEvent event = MouseEvent.create(clickEvent);
        if (clickEvent.isDown()) {
            invokeMousePress(event);
        } else {
            invokeMouseRelease(event);
        }
    }

    @Override
    public void onMouseScrollEvent(ScrollEvent event) {

    }

    @Override
    public void process() {
        TControl selected = null;
        for (TControl child : getChildren()) {
            if (child.isSelected()) {
                selected = child;
            } else {
                child.process();
            }
        }
        if (selected != null) {
            selected.process();
        }
    }

    @Override
    public ObservableList<TControl> getChildren() {
        return children;
    }

    public TControl getSelected() {
        for (TControl child : getChildren()) {
            if (child.isSelected()) {
                return child;
            }
        }
        return null;
    }

    /**
     * Invoked when a mouse button has been pressed on a component
     *
     * @param event the mouse event
     */
    protected final void invokeMousePress(MouseEvent event) {
        boolean found = false;
        for (int i = 0; i < getChildren().size(); i++) {
            final TControl component = getChildren().get(i);
            if (component.isMouseHover()) {
                component.onMousePressImpl(event);
                component.onMousePress(event);
                component.setSelected(!found);
                found |= component.isSelected();
            } else {
                component.setSelected(false);
            }
        }
    }

    /**
     * Invoked when a mouse button has been released on the component
     *
     * @param event the mouse event
     */
    protected final void invokeMouseRelease(MouseEvent event) {
        getChildren().forEach(component -> {
            if (component.isMousePressed()) {
                component.onMouseReleaseImpl(event);
                component.onMouseRelease(event);
            }
        });
    }

    /**
     * Invoked when the mouse enters the component
     *
     * @param event the mouse event
     */
    protected final void invokeMouseEnter(MouseEvent event) {
        for (TControl component : getChildren()) {
            if (!component.isMouseHover() && component.inTouch(event.getX(), event.getY())) {
                component.onMouseEnterImpl(event);
                component.onMouseEnter(event);
            }
        }
    }

    /**
     * Invoked when the mouse exits the component
     *
     * @param event the mouse event
     */
    protected final void invokeMouseExit(MouseEvent event) {
        for (TControl component : getChildren()) {
            if (component.isMouseHover() && !component.inTouch(event.getX(), event.getY())) {
                component.onMouseExitImpl(event);
                component.onMouseExit(event);
            }
        }
    }

    /**
     * Invoked when the mouse move on the component
     *
     * @param event the mouse event
     */
    protected final void invokeMouseMove(MouseEvent event) {
        for (TControl component : getChildren()) {
            if (component.isMouseHover()) {
                component.onMouseMoveImpl(event);
                component.onMouseMove(event);
            }
        }
    }

    /**
     * Invoked when the mouse drags on the component
     *
     * @param event the mouse event
     */
    protected final void invokeMouseDrag(MouseEvent event) {
        for (TControl component : getChildren()) {
            if (component.isMousePressed() && event.getButton() != MouseButton.NONE) {
                component.onMouseDragImpl(event);
                component.onMouseDrag(event);
            }
        }
    }

    public void attachChild(TControl child) {
        getChildren().add(child);
    }
}
