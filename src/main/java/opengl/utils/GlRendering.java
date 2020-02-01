package opengl.utils;

import opengl.constants.DataType;
import opengl.constants.RenderMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;

public final class GlRendering {

    private GlRendering() {

    }

    /**
     * Draws the bound vao using only his array buffers
     *
     * @param mode  the render mode
     * @param first the first vertex
     * @param count the number of vertices
     */
    public static void drawArrays(RenderMode mode, int first, int count) {
        GL11.glDrawArrays(mode.get(), first, count);
    }

    /**
     * Draws the bound vao using his array buffers and element buffer (indices)
     *
     * @param mode    the render mode
     * @param count   the number of vertices
     * @param type    the type of the indices
     * @param indices the number of the indices
     */
    public static void drawElements(RenderMode mode, int count, DataType type, long indices) {
        GL11.glDrawElements(mode.get(), count, type.get(), indices);
    }

    /**
     * Draws the bound vao multiple times using only his array buffers
     *
     * @param mode      the render mode
     * @param first     the first vertex
     * @param count     the number of vertices
     * @param instances the number of instances
     */
    public static void drawArraysInstanced(RenderMode mode, int first, int count, int instances) {
        GL31.glDrawArraysInstanced(mode.get(), first, count, instances);
    }

    /**
     * Draws the bound vao multiple times using his array buffers and element buffer (indices)
     *
     * @param mode      the render mode
     * @param count     the number of vertices
     * @param type      the type of the indices
     * @param indices   the number of the indices
     * @param instances the number of instances
     */
    public static void drawElementsInstanced(RenderMode mode, int count, DataType type, long indices, int instances) {
        GL31.glDrawElementsInstanced(mode.get(), count, type.get(), indices, instances);
    }

}
