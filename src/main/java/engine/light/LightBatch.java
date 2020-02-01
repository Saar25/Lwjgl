package engine.light;

import java.util.ArrayList;
import java.util.List;

public class LightBatch {

    private static final List<Light> lights = new ArrayList<>();

    public static List<Light> getLights() {
        return lights;
    }

    public static void process(Light light) {
        lights.add(light);
    }

    public static void clear() {
        lights.clear();
    }
}
