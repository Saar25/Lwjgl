package game;

import maths.utils.Vector3;
import maths.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public final class Configs {

    private Configs() {

    }

    // Terrains
    public static final float TERRAIN_SIZE = 800f;
    public static final int TERRAIN_SEED = (int) (Math.random() * 5000);

    // Sun
    public static final Vector3f SUN_POSITION = Vector3.of(700, 1500, 300);

    // Day night cycle
    public static final float DAY_NIGHT_SPM = 0.1f;
    public static final Vector3f DAY_NIGHT_1100 = Vector3.of(1.0f, 1.0f, 1.0f); // Vector3.of(.71f, .84f, .88f);
    public static final Vector3f DAY_NIGHT_0600 = Vector3.of(1.0f, .54f, .40f); // Vector3.of(.90f, .42f, .24f);
    public static final Vector3f DAY_NIGHT_0000 = Vector3.of(.25f, .23f, .37f); // Vector3.of(.15f, .13f, .31f);

    // Light
    public static final Vector3f LIGHT_POSITION = Vector3.of(50, 5, -20);
    public static final Vector3f LIGHT_COLOUR = Vector3.of(1, 0, 1);
    public static final Vector3f LIGHT_ATTENUATION = Vector3.of(0.3f, .0f, .001f);

    // Input
    public static final int DETACH_MOUSE_KEY = GLFW.GLFW_KEY_ESCAPE;
    public static final int DRAW_FILLED_KEY = 'T';//GLFW.GLFW_KEY_T;
    public static final int DRAW_LINES_KEY = 'R';//GLFW.GLFW_KEY_R;
    public static final float MOUSE_SENSITIVITY = 0.3f;

}
