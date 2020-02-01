package engine.gameengine;

public enum  GameState {

    GAME,
    GUI;

    private static GameState current = GAME;

    public static GameState getCurrent() {
        return current;
    }

    public static void setCurrent(GameState current) {
        GameState.current = current;
    }
}
