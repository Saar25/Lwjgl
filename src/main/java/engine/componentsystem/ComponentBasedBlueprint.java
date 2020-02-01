package engine.componentsystem;

public abstract class ComponentBasedBlueprint {

    private final ComponentGroupBuilder components = new ComponentGroupBuilder();

    public ComponentGroupBuilder getComponents() {
        return components;
    }

    public abstract ComponentBased createInstance();

}
