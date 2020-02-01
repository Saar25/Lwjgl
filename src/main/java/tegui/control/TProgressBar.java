package tegui.control;

import engine.util.lengths.Proportion;
import engine.util.property.FloatProperty;
import tegui.GuiObject;
import tegui.TControl;
import tegui.driver.FloatDriver;
import tegui.objects.TRectangle;
import tegui.style.property.Colour;

public class TProgressBar extends TControl {

    private final FloatProperty progressProperty = new FloatProperty();

    private final GuiObject bar = createBar();
    private final GuiObject border = createBorder();

    private FloatDriver progressDriver = null;

    public TProgressBar() {
        getChildren().add(bar);
        getChildren().add(border);

        getStyle().backgroundColour.setNormalized(0, 1, 0);
        getStyle().position.set(100, 200);
        getStyle().dimensions.set(500, 200);

        progressProperty().addListener((o, old, value) ->
                bar.getStyle().width.set(Proportion.of(value / 100f)));
    }

    private static GuiObject createBar() {
        return new TRectangle();
    }

    private static GuiObject createBorder() {
        final TRectangle border = new TRectangle();
        border.getStyle().backgroundColour.set(0, 0, 0, 0);
        border.getStyle().borderColour.set(Colour.BLACK);
        border.getStyle().borders.set(3);
        return border;
    }

    @Override
    public void update() {
        super.update();

        if (progressDriver != null) {
            progressDriver.update();
            progressProperty().set(progressDriver.get());
        }
    }

    public FloatProperty progressProperty() {
        return progressProperty;
    }

    public float getValue() {
        return progressProperty().get();
    }

    public void setValue(float value) {
        progressProperty().set(value);
    }

    public void setProgressDriver(FloatDriver progressDriver) {
        this.progressDriver = progressDriver;
    }
}
