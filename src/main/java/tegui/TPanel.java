package tegui;

import engine.util.property.BooleanProperty;
import tegui.event.MouseEvent;
import tegui.style.Style;
import tegui.style.Styleable;
import tegui.style.property.Colour;

/**
 * This class represent a panel that gui components can be placed on
 *
 * @author Saar ----
 * @version 1.2
 * @since 17.2.2019
 */
public class TPanel extends TContainer implements Styleable {

    private static final int HEADER = 20;
    private static final int BORDERS = 3;

    private final PanelBackground panelBackground;

    private final Style childrenStyle = new Style();

    private BooleanProperty draggableProperty = new BooleanProperty(true);

    public TPanel() {
        final PanelHeader panelHeader = new PanelHeader();
        getChildren().add(panelHeader);
        panelHeader.getStyle().width.set(length -> getStyle().width.get() + BORDERS * 2);
        panelHeader.getStyle().height.set(length -> getStyle().height.get() + HEADER + BORDERS);
        panelHeader.getStyle().x.set(length -> getStyle().x.get() - BORDERS);
        panelHeader.getStyle().y.set(length -> getStyle().y.get() - HEADER);
        panelHeader.getStyle().backgroundColour.set(Colour.BLUE);

        this.panelBackground = new PanelBackground();
        getChildren().add(panelBackground);

        getChildrenStyle().setParent(getStyle());

        getChildren().addListener(change -> {
            for (TControl tControl : change.getAdded()) {
                tControl.getStyle().setParent(getChildrenStyle());
            }
        });
    }

    @Override
    public Style getStyle() {
        return panelBackground.getStyle();
    }

    public Style getChildrenStyle() {
        return childrenStyle;
    }

    public BooleanProperty draggableProperty() {
        return draggableProperty;
    }

    private static class PanelBackground extends TControl {
        private PanelBackground() {
            getChildren().add(new GuiObject());
            setSelectable(false);
        }
    }

    private class PanelHeader extends PanelBackground {

        private int xStart;
        private int yStart;

        @Override
        public void onMousePress(MouseEvent event) {
            this.xStart = event.getX() - TPanel.this.getStyle().x.get();
            this.yStart = event.getY() - TPanel.this.getStyle().y.get();
        }

        @Override
        public void onMouseDrag(MouseEvent event) {
            if (!panelBackground.isMousePressed() && draggableProperty().get() && getSelected() == null) {
                TPanel.this.getStyle().x.set(event.getX() - xStart);
                TPanel.this.getStyle().y.set(event.getY() - yStart);
            }
        }
    }
}
