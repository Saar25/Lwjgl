package tegui.control;

import engine.util.property.BooleanProperty;
import tegui.GuiObject;
import tegui.TControl;
import tegui.event.MouseEvent;
import tegui.objects.TImage;
import tegui.objects.TRectangle;

public class TCheckBox extends TControl {

    private final BooleanProperty checkedProperty = new BooleanProperty();

    private final GuiObject box = new TRectangle();
    private GuiObject check = new TRectangle();

    private int space = 5;
    private int borders = 10;

    public TCheckBox() {
        getChildren().add(box);

        check.getStyle().borders.set(0);
        check.getStyle().position.set(space, space);
        check.getStyle().width.set(length -> box.getStyle().width.get() - space * 2);
        check.getStyle().height.set(length -> box.getStyle().height.get() - space * 2);
        check.getStyle().backgroundColour.set(255, 255, 255, 255);

        checkedProperty().addListener((observable, oldValue, currentValue) -> {
            if (currentValue) {
                getChildren().add(check);
            } else {
                getChildren().remove(check);
            }
        });
    }

    public BooleanProperty checkedProperty() {
        return checkedProperty;
    }

    public void setCheckImage(TImage check) {
        if (checkedProperty().get()) {
            getChildren().remove(this.check);
            getChildren().add(check);
        }
        this.check = check;
    }

    @Override
    public void onMouseRelease(MouseEvent event) {
        checkedProperty().flip();
    }
}
