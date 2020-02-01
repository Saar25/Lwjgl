package maths.noise;

import maths.joml.Vector3f;
import maths.utils.Vector3;

public interface Noise3f {

    Noise3f ONE = (x, y, z) -> 1;

    Noise3f LENGTH = (x, y, z) -> Vector3.length(x, y, z);

    float noise(float x, float y, float z);

    default float noise(Vector3f v) {
        return noise(v.x, v.y, v.z);
    }

}
