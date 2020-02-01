package opengl.textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL32;

public enum TextureTarget {

    TEXTURE_2D(GL11.GL_TEXTURE_2D),
    TEXTURE_CUBE_MAP(GL13.GL_TEXTURE_CUBE_MAP),
    TEXTURE_2D_MULTISAMPLE(GL32.GL_TEXTURE_2D_MULTISAMPLE),

    TEXTURE_CUBE_MAP_POSITIVE_X(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X),
    TEXTURE_CUBE_MAP_NEGATIVE_X(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X),
    TEXTURE_CUBE_MAP_POSITIVE_Y(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y),
    TEXTURE_CUBE_MAP_NEGATIVE_Y(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y),
    TEXTURE_CUBE_MAP_POSITIVE_Z(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z),
    TEXTURE_CUBE_MAP_NEGATIVE_Z(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z),
    ;

    private final int value;

    TextureTarget(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    /**
     * Returns the texture texture target of the given face
     *
     * @param face the index of the cube face
     * @return the fitted texture target
     */
    public static TextureTarget ofCubeFace(int face) {
        switch (face) {
            case 0: return TEXTURE_CUBE_MAP_POSITIVE_X;
            case 1: return TEXTURE_CUBE_MAP_NEGATIVE_X;
            case 2: return TEXTURE_CUBE_MAP_POSITIVE_Y;
            case 3: return TEXTURE_CUBE_MAP_NEGATIVE_Y;
            case 4: return TEXTURE_CUBE_MAP_POSITIVE_Z;
            case 5: return TEXTURE_CUBE_MAP_NEGATIVE_Z;
            default: throw new IllegalArgumentException("Cubes do not have " + face + " faces");
        }
    }
}
