package engine.effects;

import engine.rendering.Camera;
import engine.rendering.RenderManager;
import engine.util.node.GGroup;

/**
 * This class represent an effect in the scene such as water reflection or shadows
 * which would be difficult to achieve with regular rendering
 *
 * @author Saar ----
 * @version 1.2
 * @since 14.2.2018
 */
public abstract class Effect {

    protected final GGroup<?> group;
    private boolean enabled = true;

    protected Effect(GGroup<?> group) {
        this.group = group;
    }

    public abstract void process(RenderManager renderManager, Camera camera);

    public final void processIfEnabled(RenderManager renderManager, Camera camera) {
        if (enabled) process(renderManager, camera);
    }

    public final void delete() {
        group.delete();
        onDelete();
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    /**
     * Invoked when the effect is deleted
     */
    protected void onDelete() {

    }

    /**
     * Invoked when the effect is enabled
     */
    protected void onEnable() {

    }

    /**
     * Invoked when the effect is disabled
     */
    protected void onDisable() {

    }
}
