package engine.engineObjects;

import opengl.textures.Texture;
import maths.joml.Vector3f;

public class Material {

    private final Vector3f colour;
    private final Texture diffuse;

    public Material(Vector3f colour, Texture diffuse) {
        this.colour = colour;
        this.diffuse = diffuse;
    }

    public Vector3f getColour() {
        return colour;
    }

    public Texture getDiffuse() {
        return diffuse;
    }
}
