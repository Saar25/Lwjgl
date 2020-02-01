package engine.models;

import maths.objects.Box;

public interface Model {

    /**
     * Render the model
     */
    void render();

    /**
     * Returns the level of detail of the model
     *
     * @return the level of detail
     */
    ILod getLod();

    /**
     * Returns the bounds of the model
     *
     * @return the bounds
     */
    Box getBounds();

    /**
     * Delete the model
     */
    void delete();

}
