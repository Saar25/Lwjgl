package engine.componentsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ComponentGroupBuilder {

    private final List<Supplier<GameComponent>> components = new ArrayList<>();

    public void add(Supplier<GameComponent> component) {
        this.components.add(component);
    }

    public ComponentGroup apply(ComponentBased componentBased) {
        for (Supplier<GameComponent> component : components) {
            componentBased.getComponents().add(component.get());
        }
        return componentBased.getComponents();
    }

}
