package tegui.control;

import engine.gameengine.Time;
import engine.util.Smooth;
import engine.util.lengths.Proportion;
import engine.util.property.BooleanProperty;
import tegui.TControl;
import tegui.GuiObject;
import tegui.event.MouseEvent;
import tegui.objects.TRectangle;

public class TToggleButton extends TControl {

    private final BooleanProperty valueProperty = new BooleanProperty();
    private final Smooth<Float> togglePosition = Smooth.ofFloat(10);

    private final GuiObject bar;
    private final GuiObject toggle;

    public TToggleButton(int x, int y) {
        this();
        getStyle().position.set(x, y);
        getStyle().dimensions.set(300, 100);
        getStyle().radiuses.set(15f);
    }

    public TToggleButton() {
        this.bar = createBar();
        this.toggle = createToggle();

        getChildren().add(bar);
        getChildren().add(toggle);

        togglePosition.set(0f, true);

        valueProperty().addListener((o, old, value) -> {
            if (value) {
                togglePosition.setTarget(1f);
                bar.getStyle().backgroundColour.set(0, 255, 0, 255);
            } else {
                togglePosition.setTarget(0f);
                bar.getStyle().backgroundColour.set(255, 0, 0, 255);
            }
        });
        valueProperty().setValue(false);
    }

    private static GuiObject createBar() {
        final GuiObject rectangle = new TRectangle();
        rectangle.getStyle().backgroundColour.set(255, 0, 0, 255);
        return rectangle;
    }

    private static GuiObject createToggle() {
        final GuiObject rectangle = new TRectangle();
        rectangle.getStyle().backgroundColour.set(255, 255, 255, 255);
        rectangle.getStyle().width.set(Proportion.of(.5f));
        return rectangle;
    }

    public BooleanProperty valueProperty() {
        return valueProperty;
    }

    @Override
    public void onMousePress(MouseEvent event) {
        final boolean value = valueProperty().getValue();
        valueProperty().setValue(!value);
    }

    @Override
    public void update() {
        super.update();

        togglePosition.interpolate(Time.getDelta());
        final float w1 = getStyle().dimensions.getWidth();
        final float w2 = toggle.getStyle().dimensions.getWidth();
        toggle.getStyle().position.setX(togglePosition.get() * (w1 - w2));
    }
}
