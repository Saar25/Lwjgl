package engine.light;

import maths.joml.Vector3fc;

public interface ILight {

    Vector3fc getPosition();

    Vector3fc getColour();

    Vector3fc getAttenuation();

    float getIntensity();

    boolean isDirectional();

}
