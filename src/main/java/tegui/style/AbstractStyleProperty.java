package tegui.style;

public abstract class AbstractStyleProperty implements StyleProperty {

    protected final Style origin;

    private boolean inherited = true;

    public AbstractStyleProperty() {
        this(null);
    }

    public AbstractStyleProperty(Style origin) {
        this.origin = origin;
    }

    /**
     * Sets the inherited state of the property
     *
     * @param inherited the state
     */
    public final void setInherited(boolean inherited) {
        this.inherited = inherited;
    }

    @Override
    public boolean isInherited() {
        return inherited;
    }
}
