package tegui;

import engine.gameengine.GameState;
import engine.util.ObservableList;
import glfw.input.MouseButton;
import glfw.input.event.ClickEvent;
import glfw.input.event.MoveEvent;
import glfw.input.event.ScrollEvent;
import tegui.event.MouseEvent;
import tegui.style.Style;
import tegui.style.Styleable;

import java.util.List;

public abstract class TControl extends TComponent implements Styleable {

    private final Style style = new Style();
    private final ObservableList<GuiObject> children;

    private boolean selectable = true;
    private boolean selected;
    private boolean mouseHover;
    private boolean mousePressed;

    protected TControl() {
        this.children = ObservableList.observableArrayList();
        children.addListener(change -> {
            for (GuiObject child : change.getAdded()) {
                child.getStyle().setParent(style);
            }
            for (GuiObject child : change.getRemoved()) {
                child.getStyle().setParent(null);
            }
        });
    }

    @Override
    public final void onMouseMoveEvent(MoveEvent event) {
        final MouseEvent e = MouseEvent.create(event);
        final boolean inTouch = inTouch(event.getMouse().getXPos(), event.getMouse().getYPos());

        if (inTouch  && !isMouseHover()) {
            onMouseEnterImpl(e);
            onMouseEnter(e);
        } else if (!inTouch && isMouseHover()) {
            onMouseExitImpl(e);
            onMouseExit(e);
        }

        if (inTouch && e.getButton() != MouseButton.PRIMARY) {
            onMouseMoveImpl(e);
            onMouseMove(e);
        } else if (e.getButton() == MouseButton.PRIMARY && isMousePressed()) {
            onMouseDragImpl(e);
            onMouseDrag(e);
        }

    }

    @Override
    public final void onMouseClickEvent(ClickEvent event) {
        final MouseEvent e = MouseEvent.create(event);
        if (event.isDown() && inTouch(event.getMouse().getXPos(), event.getMouse().getYPos())) {
            onMousePressImpl(e);
            onMousePress(e);
        } else if (isMousePressed()) {
            onMouseReleaseImpl(e);
            onMouseRelease(e);
        }
    }

    @Override
    public final void onMouseScrollEvent(ScrollEvent event) {
        /*if (inTouch(event.getMouse().getXPos(), event.getMouse().getYPos())) {
            final MouseEvent e = MouseEvent.create(event);
            onMouseScroll(e);
        }*/
    }

    /**
     * Returns the style of the component, this style is the parent
     * of all the  styles of the component's children
     *
     * @return the style of the component
     */
    @Override
    public Style getStyle() {
        return style;
    }

    /**
     * Returns whether the  the component contains the given point
     *
     * @param x the x value
     * @param y the y value
     * @return true if the point is in touch, false otherwise
     */
    public boolean inTouch(int x, int y) {
        return getChildren().stream().anyMatch(o -> o.inTouch(x, y));
    }

    @Override
    public void process() {
        getChildren().forEach(GuiObject::process);
    }

    @Override
    public void update() {
        getChildren().forEach(GuiObject::update);
    }

    @Override
    public void delete() {
        getChildren().forEach(GuiObject::delete);
    }

    protected List<GuiObject> getChildren() {
        return children;
    }

    /**
     * Returns whether the gui component is currently pressed by the mouse
     *
     * @return true if the object is pressed, false otherwise
     */
    public final boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Returns whether the mouse hovers the gui component
     *
     * @return true if the mouse is hover, false otherwise
     */
    public final boolean isMouseHover() {
        return mouseHover;
    }

    /**
     * Returns whether the gui component is currently selected
     *
     * @return true if selected, false otherwise
     */
    public final boolean isSelected() {
        return selected && selectable;
    }

    public final void setSelected(boolean selected) {
        this.selected = selected;
    }

    protected final void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    /**
     * Invoked when a mouse button has been pressed on a component
     *
     * @param event the mouse event
     */
    public void onMousePress(MouseEvent event) {

    }

    final void onMousePressImpl(MouseEvent event) {
        mousePressed = true;
        GameState.setCurrent(GameState.GUI);
    }

    /**
     * Invoked when a mouse button has been released on the component
     *
     * @param event the mouse event
     */
    public void onMouseRelease(MouseEvent event) {

    }

    final void onMouseReleaseImpl(MouseEvent event) {
        mousePressed = false;
        GameState.setCurrent(GameState.GAME);
    }

    /**
     * Invoked when the mouse enters the component
     *
     * @param event the mouse event
     */
    public void onMouseEnter(MouseEvent event) {

    }

    final void onMouseEnterImpl(MouseEvent event) {
        mouseHover = true;
    }

    /**
     * Invoked when the mouse exits the component
     *
     * @param event the mouse event
     */
    public void onMouseExit(MouseEvent event) {

    }

    final void onMouseExitImpl(MouseEvent event) {
        mouseHover = false;
    }

    /**
     * Invoked when the mouse move on the component
     *
     * @param event the mouse event
     */
    public void onMouseMove(MouseEvent event) {

    }

    final void onMouseMoveImpl(MouseEvent event) {

    }

    /**
     * Invoked when the mouse drags on the component
     *
     * @param event the mouse event
     */
    public void onMouseDrag(MouseEvent event) {

    }

    final void onMouseDragImpl(MouseEvent event) {

    }
}
