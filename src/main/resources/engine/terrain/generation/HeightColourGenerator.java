package engine.terrain.generation;

import engine.terrain.TerrainVertex;
import maths.joml.Vector3f;
import maths.utils.Maths;

import java.util.ArrayList;
import java.util.List;

public class HeightColourGenerator implements ColourGenerator {

    private final List<HeightColour> heightColours = new ArrayList<>();

    public HeightColourGenerator withColour(float height, Vector3f colour) {
        addColour(height, colour);
        return this;
    }

    public void addColour(float height, Vector3f colour) {
        heightColours.add(new HeightColour(height, colour));
        heightColours.sort((hc1, hc2) -> (int) (hc1.height - hc2.height));
    }

    @Override
    public Vector3f generateColour(TerrainVertex vertex) {
        if (vertex.getPosition().y <= heightColours.get(0).height) {
            return heightColours.get(0).colour;
        }
        for (int i = 1; i < heightColours.size(); i++) {
            final HeightColour high = heightColours.get(i);
            final HeightColour low = heightColours.get(i - 1);
            if (Maths.isInside(vertex.getPosition().y, low.height, high.height)) {
                final float factor = (high.height - vertex.getPosition().y) / (high.height - low.height);
                return Maths.mix(high.colour, low.colour, factor);
            }
        }
        return heightColours.get(heightColours.size() - 1).colour;
    }

    private static class HeightColour {
        private final float height;
        private final Vector3f colour;

        public HeightColour(float height, Vector3f colour) {
            this.height = height;
            this.colour = colour;
        }
    }
}
