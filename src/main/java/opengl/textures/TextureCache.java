package opengl.textures;

import java.util.HashMap;
import java.util.Map;

public final class TextureCache {

    private static final Map<String, ITexture> CACHE = new HashMap<>();

    private TextureCache() {

    }

    public static void addToCache(String file, ITexture texture) {
        CACHE.put(file, texture);
    }

    public static ITexture getTexture(String file) {
        return CACHE.get(file);
    }
}
