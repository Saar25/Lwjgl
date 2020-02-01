package tegui.style;

import maths.joml.Vector4f;
import maths.utils.Vector4;
import tegui.style.property.*;

public class Style {

    public final Vector4f colourModifier = Vector4.of(1);


    public final Coordinate x = new Coordinate.Horizontal(this);

    public final Coordinate y = new Coordinate.Vertical(this);

    public final Position position = new Position(this);


    public final Length width = new Length.Horizontal(this);

    public final Length height = new Length.Vertical(this);

    public final Dimensions dimensions = new Dimensions(this);


    public final Bounds bounds = new Bounds(this);


    public final Borders borders = new Borders(this);

    public final Colour borderColour = new Colour(this);

    public final Radiuses radiuses = new Radiuses(this);

    public final CornersColours backgroundColour = new CornersColours(this);

    /**
     * The parent style used to inherit the properties from
     */
    private Style parent = DefaultStyle.getInstance();

    /**
     * Borders property of the style
     */
    public final Attribute<Borders> bordersAttribute = new Attribute<Borders>(borders) {
        @Override
        public Attribute<Borders> parentAttribute() {
            return parent.bordersAttribute;
        }
    };

    /**
     * Border colour property of the style
     */
    public final Attribute<Colour> borderColourAttribute = new Attribute<Colour>(borderColour) {
        @Override
        public Attribute<Colour> parentAttribute() {
            return parent.borderColourAttribute;
        }
    };

    /**
     * Border radius property of the style
     */
    public final Attribute<Radiuses> borderRadiusAttribute = new Attribute<Radiuses>(radiuses) {
        @Override
        public Attribute<Radiuses> parentAttribute() {
            return parent.borderRadiusAttribute;
        }
    };

    /**
     * Background colour property of the style
     */
    public final Attribute<CornersColours> backgroundColourAttribute = new Attribute<CornersColours>(backgroundColour) {
        @Override
        public Attribute<CornersColours> parentAttribute() {
            return parent.backgroundColourAttribute;
        }
    };

    public Style getParent() {
        return parent;
    }

    public void setParent(Style parent) {
        this.parent = parent == null ? DefaultStyle.getInstance() : parent;
    }

    public abstract class Attribute<T extends StyleProperty> {

        private final T property;

        public Attribute(T property) {
            this.property = property;
        }

        public T get() {
            return parent != null && property.isInherited()
                    ? parentAttribute().get() : property;
        }

        public abstract Attribute<T> parentAttribute();

    }

}
