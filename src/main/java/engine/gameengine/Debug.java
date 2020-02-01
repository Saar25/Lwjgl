package engine.gameengine;

public final class Debug {

    private Debug() {

    }

    public static void print(String string) {
        System.out.println(string);
    }

    public static void print(Object object) {
        System.out.println(object);
    }

    public static void printError(String s) {
        System.err.println(s);
    }

}
