package game;

import engine.gameengine.SimpleApplication;
import engine.gameengine.GameEngine;
import engine.rendering.RenderManager;
import engine.rendering.background.BackgroundColour;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import tegui.TContainer;
import tegui.control.TButton;
import tegui.event.MouseEvent;
import tegui.style.property.Colour;
import tegui.style.property.Orientation;
import tegui.style.Style;

public class NewGame extends SimpleApplication {

    private boolean a = false;
    private boolean b = false;

    public static void main(String[] args) {
        try {
            SimpleApplication game = new NewGame();
            GameEngine gameEngine = new GameEngine("NewGame", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) {
        final Style style = new Style();
        style.borders.set(5);
        style.dimensions.set(800, 300);
        style.backgroundColour.set(Colour.ORANGE,
                new Colour(128, 0, 128), Orientation.HORIZONTAL);

        final TButton button = new DraggableButton();
        button.getStyle().setParent(style);
        button.getStyle().position.set(100, 100);
        button.getStyle().dimensions.set(800, 300);
        button.setOnAction(e -> {
            if (a) {
                button.getStyle().backgroundColour.set(Colour.ORANGE,
                        new Colour(128, 0, 128), Orientation.HORIZONTAL);
            } else {
                button.getStyle().backgroundColour.set(Colour.GREEN,
                        Colour.DARK_GREY, Orientation.HORIZONTAL);
            }
            a = !a;
        });

        final TButton button1 = new DraggableButton();
        button1.getStyle().setParent(style);
        button1.getStyle().position.set(500, 500);
        button1.getStyle().dimensions.set(800, 300);
        button1.setOnAction(e -> {
            if (b) {
                button1.getStyle().backgroundColour.set(Colour.ORANGE,
                        new Colour(128, 0, 128), Orientation.HORIZONTAL);
            } else {
                button1.getStyle().backgroundColour.set(Colour.GREEN,
                        Colour.DARK_GREY, Orientation.HORIZONTAL);
            }
            b = !b;
        });

        final TContainer tPanel = new TContainer();
        tPanel.attachChild(button);
        tPanel.attachChild(button1);
        scene.attachChild(tPanel);

        renderer.setBackground(new BackgroundColour(.5f, 0, 1));
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        // Update game game, get the needed input as well
        scene.update();
    }

    @Override
    public void onRender(RenderManager renderer) {
        // Render the scene

        renderer.render(scene);
    }

    private static class DraggableButton extends TButton {

        private int x;
        private int y;

        @Override
        public void onMousePress(MouseEvent event) {
            super.onMousePress(event);
            this.x = event.getX() - getStyle().position.getX();
            this.y = event.getY() - getStyle().position.getY();
        }

        @Override
        public void onMouseDrag(MouseEvent event) {
            super.onMouseDrag(event);
            getStyle().bounds.setPosition(event.getX() - x, event.getY() - y);
        }
    }

}