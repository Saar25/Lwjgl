package engine.rendering;

import engine.util.property.Property;

public final class SpatialValidationBinding {

    private SpatialValidationBinding() {

    }

    public static void bind(Property<?> property, Spatial spatial) {
        property.addListener((observable, oldValue, currentValue) -> spatial.validate());
    }

    public static void unbind(Property<?> property, Spatial spatial) {
        property.removeListener((observable, oldValue, currentValue) -> spatial.validate());
    }
}
