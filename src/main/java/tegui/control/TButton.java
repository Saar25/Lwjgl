package tegui.control;

import engine.util.property.BooleanProperty;
import tegui.GuiObject;
import tegui.TControl;
import tegui.event.EventHandler;
import tegui.event.MouseEvent;
import tegui.objects.TImage;
import tegui.objects.TRectangle;

public class TButton extends TControl {

    private static final int g = 5;

    private final BooleanProperty pressedProperty = new BooleanProperty();

    private EventHandler<MouseEvent> onAction = e -> {};

    private GuiObject guiObject = new TRectangle();

    public TButton() {
        getChildren().add(guiObject);

        /*
        getStyle().radiuses.set(15f, 50f);
        getStyle().backgroundColour.set(36, 74, 102);
        getStyle().backgroundColour.set(Colour.CYAN, Colour.BLUE, Orientation.VERTICAL);*/
    }

    public void setImage(TImage image) {
        getChildren().remove(guiObject);
        getChildren().add(image);
        guiObject = image;
    }

    public BooleanProperty pressedProperty() {
        return pressedProperty;
    }

    public void setOnAction(EventHandler<MouseEvent> onAction) {
        this.onAction = onAction;
    }

    @Override
    public void onMousePress(MouseEvent event) {
        if (event.getButton().isPrimary()) {
            pressedProperty().set(true);
            getStyle().colourModifier.set(1.5f, 1.5f, 1.5f, 1f);
            for (GuiObject child : getChildren()) {
                child.getStyle().colourModifier.set(getStyle().colourModifier);
            }
        }
    }

    @Override
    public void onMouseRelease(MouseEvent event) {
        if (event.getButton().isPrimary()) {
            if (pressedProperty().get()) {
                pressedProperty().set(false);
                onAction.handle(event);
            }
            getStyle().colourModifier.set(1);
            for (GuiObject child : getChildren()) {
                child.getStyle().colourModifier.set(getStyle().colourModifier);
            }
        }
    }

    @Override
    public void onMouseEnter(MouseEvent event) {
        //getStyle().position.add(-g, -g);
        //getStyle().dimensions.add(2 * g, 2 * g);
        getTransform().scaleBy(2f);
    }

    @Override
    public void onMouseExit(MouseEvent event) {
        pressedProperty().set(false);
        getTransform().scaleBy(.5f);
        //getStyle().position.add(g, g);
        //getStyle().dimensions.add(-2 * g, -2 * g);
        getStyle().colourModifier.set(1f);
        for (GuiObject child : getChildren()) {
            child.getStyle().colourModifier.set(getStyle().colourModifier);
        }
    }
}
