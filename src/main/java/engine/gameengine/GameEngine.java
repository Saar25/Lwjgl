package engine.gameengine;

import engine.rendering.RenderManager;
import glfw.input.Keyboard;
import glfw.input.Mouse;
import glfw.window.Window;

public class GameEngine implements Runnable {

    private static final boolean vSync = false;
    private static final boolean showFps = true;

    private final Window window;
    private final Thread gameLoopThread;
    private final Thread gameUpdateThread;
    private final Application game;

    private RenderManager renderer;
    private Keyboard keyboard;
    private Mouse mouse;

    public GameEngine(String windowTitle, int windowWidth, int windowHeight, Application game) {
        this(windowTitle, windowWidth, windowHeight, game, vSync);
    }

    public GameEngine(String windowTitle, int windowWidth, int windowHeight, Application game, boolean vSync) {
        this.window = new Window(windowTitle, windowWidth, windowHeight, vSync);
        this.gameLoopThread = new Thread(this, "MAIN GAME LOOP");
        this.gameUpdateThread = new Thread(() -> update(), "GAME UPDATE LOOP");
        this.game = game;
    }

    public void init() throws Exception {
        window.init();
        renderer = new RenderManager();
        keyboard = window.getKeyboard();
        mouse = window.getMouse();
        game.init(window, renderer, keyboard, mouse);
    }

    public void start() {
        this.gameLoopThread.run();
    }

    @Override
    public void run() {
        float fps = 0;
        int times = 0;
        while (window.isOpen() && !game.isClosed()) {
            try {
                Time.update();

                if (showFps) {
                    times++;
                    fps += Time.getDelta();
                    if (times >= 50) {
                        System.out.print("\u001B[32m" + "\r fps: " + (int) (times / fps) + "\u001B[0m");
                        times = 0;
                        fps = 0;
                    }
                }

                game.update(keyboard, mouse);
                game.render(renderer);

                window.update(renderer.isAnyChange());
                window.pollEvents();

                renderer.setAnyChange(false);

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        game.cleanUp();
        renderer.cleanUp();
        Mouse.cleanUp();
    }

    private void update() {
        while (window.isOpen()) {
            Time.update();
            game.update(keyboard, mouse);
        }
    }
}
