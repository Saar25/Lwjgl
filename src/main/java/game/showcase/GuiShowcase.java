package game.showcase;

import engine.gameengine.GameEngine;
import engine.gameengine.SimpleApplication;
import engine.rendering.RenderManager;
import engine.rendering.background.BackgroundColour;
import engine.util.lengths.Pixels;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;
import tegui.control.TButton;
import tegui.control.TCheckBox;
import tegui.style.Style;
import tegui.style.property.Colour;

public class GuiShowcase extends SimpleApplication {

    public static void main(String[] args) {
        try {
            SimpleApplication game = new GuiShowcase();
            GameEngine gameEngine = new GameEngine("GuiShowcase", 1200, 741, game);
            gameEngine.init();
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(Window window, RenderManager renderer, Keyboard keyboard, Mouse mouse) {
        renderer.setBackground(new BackgroundColour(Colour.CYAN));

        final Style btnStyle = new Style();
        btnStyle.borders.set(2);
        btnStyle.radiuses.set(5);
        btnStyle.borderColour.set(Colour.LIGHT_GREY);
        btnStyle.backgroundColour.set(Colour.DARK_GREY);

        final Style checkBoxStyle = new Style();
        checkBoxStyle.backgroundColour.set(Colour.DARK_GREY);
        checkBoxStyle.borderColour.set(Colour.BLACK);
        checkBoxStyle.borders.set(2);

        for (int i = 0; i < 10; i++) {
            final TButton button = new TButton();
            button.getStyle().setParent(btnStyle);
            button.getStyle().position.set(20, 20 + i * 40);
            button.getStyle().height.set(button.getStyle().width);
            button.getStyle().width.set(new Pixels(30));
            scene.attachChild(button);
        }

        for (int i = 0; i < 10; i++) {
            final TCheckBox checkBox = new TCheckBox();
            checkBox.getStyle().setParent(checkBoxStyle);
            checkBox.getStyle().position.set(60, 20 + i * 40);
            checkBox.getStyle().height.set(checkBox.getStyle().width);
            checkBox.getStyle().width.set(new Pixels(30));
            scene.attachChild(checkBox);
        }
    }

    @Override
    public void onUpdate(Keyboard keyboard, Mouse mouse) {
        scene.update();
    }

    @Override
    public void onRender(RenderManager renderer) {
        renderer.render(scene);
    }

}