package engine.util.node;

import engine.rendering.Spatial;
import glfw.input.event.ClickEvent;
import glfw.input.event.MoveEvent;
import glfw.input.event.ScrollEvent;

import java.util.ArrayList;

public abstract class Parent extends Spatial implements IParent {

    @Override
    public void process() {
        getChildren().forEach(Spatial::process);
    }

    @Override
    public void update() {
        getChildren().forEach(Spatial::update);
    }

    @Override
    public void delete() {
        getChildren().forEach(Spatial::delete);
    }

    @Override
    public void onMouseMoveEvent(MoveEvent event) {
        new ArrayList<>(getChildren()).forEach(child -> child.onMouseMoveEvent(event));
    }

    @Override
    public void onMouseClickEvent(ClickEvent event) {
        new ArrayList<>(getChildren()).forEach(child -> child.onMouseClickEvent(event));
    }

    @Override
    public void onMouseScrollEvent(ScrollEvent event) {
        new ArrayList<>(getChildren()).forEach(child -> child.onMouseScrollEvent(event));
    }
}
