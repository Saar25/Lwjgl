package engine.terrain.generation;

import engine.terrain.TerrainVertex;
import maths.joml.Vector3f;
import maths.utils.Maths;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NormalColourGenerator implements ColourGenerator {

    private final List<NormalColour> normalColours = new ArrayList<>();
    private final Vector3f dotProductVector;

    public NormalColourGenerator(Vector3f dotProductVector) {
        this.dotProductVector = dotProductVector;
    }

    public NormalColourGenerator withColour(float dotProduct, Vector3f colour) {
        addColour(dotProduct, colour);
        return this;
    }

    public void addColour(float dotProduct, Vector3f colour) {
        this.normalColours.add(new NormalColour(dotProduct, colour));
        this.normalColours.sort(Comparator.comparingDouble(value -> value.dotProduct));
    }

    @Override
    public Vector3f generateColour(TerrainVertex vertex) {
        final float product = dotProductVector.dot(vertex.getNormal());
        if (product <= normalColours.get(0).dotProduct) {
            return normalColours.get(0).colour;
        }
        for (int i = 1; i < normalColours.size(); i++) {
            final NormalColour high = normalColours.get(i);
            final NormalColour low = normalColours.get(i - 1);
            if (Maths.isInside(product, low.dotProduct, high.dotProduct)) {
                final float factor = (high.dotProduct - product) / (high.dotProduct - low.dotProduct);
                return Maths.mix(high.colour, low.colour, factor);
            }
        }
        return normalColours.get(normalColours.size() - 1).colour;
    }

    private static class NormalColour {
        private final float dotProduct;
        private final Vector3f colour;

        public NormalColour(float dotProduct, Vector3f colour) {
            this.dotProduct = dotProduct;
            this.colour = colour;
        }
    }
}
