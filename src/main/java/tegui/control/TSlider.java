package tegui.control;

import engine.util.property.FloatProperty;
import maths.utils.Maths;
import tegui.GuiObject;
import tegui.TControl;
import tegui.event.MouseEvent;
import tegui.objects.TRectangle;
import tegui.style.property.Dimensions;
import tegui.style.property.Position;

public class TSlider extends TControl {

    private final FloatProperty value = new FloatProperty(0);
    private final FloatProperty dynamicValue = new FloatProperty(0);

    private final FloatProperty min = new FloatProperty(0);
    private final FloatProperty max = new FloatProperty(100);

    private final GuiObject bar;
    private final GuiObject slider;

    public TSlider(int x, int y) {
        this();
        getStyle().position.set(x, y);
    }

    public TSlider() {
        getChildren().add(this.bar = createBar());
        getChildren().add(this.slider = createSlider());

        valueProperty().addListener((o, old, value) ->
                dynamicValueProperty().setValue(value));
    }

    private GuiObject createBar() {
        return new TRectangle();
    }

    private GuiObject createSlider() {
        final TRectangle rectangle = new TRectangle();
        rectangle.getStyle().backgroundColour.set(1f, 1f, 1f, 1f, true);
        rectangle.getStyle().width.set(length -> Math.min(getBar().getStyle().dimensions.getHeight(),
                (int) (.1f * getBar().getStyle().dimensions.getWidth())));
        return rectangle;
    }

    @Override
    public void onMousePress(MouseEvent event) {
        onMouseDrag(event);
    }

    @Override
    public void onMouseRelease(MouseEvent event) {
        float value = getValue();
        valueProperty().setValue(value);
    }

    @Override
    public void onMouseDrag(MouseEvent event) {
        final Position position = getBar().getStyle().position;
        final Dimensions dimensions = getBar().getStyle().dimensions;
        int w = getSlider().getStyle().dimensions.getWidth();

        int xMax = dimensions.getWidth() - w / 2;
        int x = Maths.clamp(event.getX() - position.getX(), w / 2, xMax);

        getSlider().getStyle().bounds.setMiddleX(x);

        float value = getValue();
        dynamicValueProperty().setValue(value);
    }

    private float getValue() {
        int x1 = getSlider().getStyle().position.getX();
        int w1 = getSlider().getStyle().dimensions.getWidth();
        int x2 = getBar().getStyle().position.getX();
        int w2 = getBar().getStyle().dimensions.getWidth();
        float value = (float) (x1 - x2) / (w2 - w1);
        float range = max.getValue() - min.getValue();
        return value * range + min.getValue();
    }

    public void setValue(float value) {
        valueProperty().setValue(value);
    }

    @Override
    public void update() {
        final float value = dynamicValueProperty().getValue();
        final float range = maxProperty().getValue() - minProperty().getValue();
        final float relative = (value - minProperty().getValue()) / range;
        final float width = (getBar().getStyle().dimensions.getWidth() -
                getSlider().getStyle().dimensions.getWidth());
        final float center = relative * width;

        getSlider().getStyle().bounds.setMiddleX(
                getSlider().getStyle().dimensions.getWidth() / 2f + center);
    }

    public GuiObject getBar() {
        return bar;
    }

    public GuiObject getSlider() {
        return slider;
    }


    public FloatProperty valueProperty() {
        return value;
    }

    public FloatProperty dynamicValueProperty() {
        return dynamicValue;
    }


    public void setBounds(float min, float max) {
        setMin(min);
        setMax(max);
    }


    public FloatProperty maxProperty() {
        return max;
    }

    public float getMax() {
        return maxProperty().getValue();
    }

    public void setMax(float max) {
        maxProperty().setValue(max);
    }


    public FloatProperty minProperty() {
        return min;
    }

    public float getMin() {
        return minProperty().getValue();
    }

    public void setMin(float max) {
        minProperty().setValue(max);
    }
}
