package engine.terrain.generation;

import maths.noise.Noise3f;

public class WeirdNoise implements Noise3f {

    private static final int[] SOURCE = {
            151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142,
            8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203,
            117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165,
            71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41,
            55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89,
            18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250,
            124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189,
            28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
            129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34,
            242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31,
            181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114,
            67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
    };

    private static final int[][] GRAD_3 = {
            new int[]{+1, +1, 0}, new int[]{-1, +1, 0}, new int[]{+1, -1, 0},
            new int[]{-1, -1, 0}, new int[]{+1, 0, +1}, new int[]{-1, 0, +1},
            new int[]{+1, 0, -1}, new int[]{-1, 0, -1}, new int[]{0, +1, +1},
            new int[]{0, -1, +1}, new int[]{0, +1, -1}, new int[]{0, -1, -1}
    };

    private static final float F3 = 1.0f / 3.0f;
    private static final float G3 = 1.0f / 6.0f;

    private static final int RandomSize = 256;
    private int[] random;

    public WeirdNoise() {
        Randomize(0);
    }

    public WeirdNoise(int seed) {
        Randomize(seed);
    }

    @Override
    public float noise(float x, float y, float z) {
        float n0 = 0, n1 = 0, n2 = 0, n3 = 0;

        // Noise contributions from the four corners
        // Skew the input space to determine which simplex cell we're in
        float s = (x + y + z) * F3;

        // for 3D
        int i = FastFloor(x + s);
        int j = FastFloor(y + s);
        int k = FastFloor(z + s);

        float t = (i + j + k) * G3;

        // The x,y,z distances from the cell origin
        float x0 = x - (i - t);
        float y0 = y - (j - t);
        float z0 = z - (k - t);

        // For the 3D case, the simplex shape is a slightly irregular tetrahedron.
        // Determine which simplex we are in.
        // Offsets for second corner of simplex in (i,j,k)
        int i1, j1, k1;

        // coords
        int i2, j2, k2; // Offsets for third corner of simplex in (i,j,k) coords

        if (x0 >= y0) {
            if (y0 >= z0) {
                // X Y Z order
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 1;
                k2 = 0;
            } else if (x0 >= z0) {
                // X Z Y order
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            } else {
                // Z X Y order
                i1 = 0;
                j1 = 0;
                k1 = 1;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            }
        } else {
            // x0 < y0
            if (y0 < z0) {
                // Z Y X order
                i1 = 0;
                j1 = 0;
                k1 = 1;
                i2 = 0;
                j2 = 1;
                k2 = 1;
            } else if (x0 < z0) {
                // Y Z X order
                i1 = 0;
                j1 = 1;
                k1 = 0;
                i2 = 0;
                j2 = 1;
                k2 = 1;
            } else {
                // Y X Z order
                i1 = 0;
                j1 = 1;
                k1 = 0;
                i2 = 1;
                j2 = 1;
                k2 = 0;
            }
        }

        // A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
        // a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z),
        // and
        // a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z),
        // where c = 1/6.

        // Offsets for second corner in (x,y,z) coords
        float x1 = x0 - i1 + G3;
        float y1 = y0 - j1 + G3;
        float z1 = z0 - k1 + G3;

        // Offsets for third corner in (x,y,z)
        float x2 = x0 - i2 + F3;
        float y2 = y0 - j2 + F3;
        float z2 = z0 - k2 + F3;

        // Offsets for last corner in (x,y,z)
        float x3 = x0 - 0.5f;
        float y3 = y0 - 0.5f;
        float z3 = z0 - 0.5f;

        // Work out the hashed gradient indices of the four simplex corners
        int ii = i & 0xff;
        int jj = j & 0xff;
        int kk = k & 0xff;

        // Calculate the contribution from the four corners
        float t0 = 0.6f - x0 * x0 - y0 * y0 - z0 * z0;
        if (t0 > 0) {
            t0 *= t0;
            int gi0 = random[ii + random[jj + random[kk]]] % 12;
            n0 = t0 * t0 * Dot(GRAD_3[gi0], x0, y0, z0);
        }

        float t1 = 0.6f - x1 * x1 - y1 * y1 - z1 * z1;
        if (t1 > 0) {
            t1 *= t1;
            int gi1 = random[ii + i1 + random[jj + j1 + random[kk + k1]]] % 12;
            n1 = t1 * t1 * Dot(GRAD_3[gi1], x1, y1, z1);
        }

        float t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2;
        if (t2 > 0) {
            t2 *= t2;
            int gi2 = random[ii + i2 + random[jj + j2 + random[kk + k2]]] % 12;
            n2 = t2 * t2 * Dot(GRAD_3[gi2], x2, y2, z2);
        }

        float t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3;
        if (t3 > 0) {
            t3 *= t3;
            int gi3 = random[ii + 1 + random[jj + 1 + random[kk + 1]]] % 12;
            n3 = t3 * t3 * Dot(GRAD_3[gi3], x3, y3, z3);
        }

        // Add contributions from each corner to get the final noise value.
        // The result is scaled to stay just inside [-1,1]
        return (n0 + n1 + n2 + n3) * 32;
    }


    private void Randomize(int seed) {
        random = new int[RandomSize * 2];

        if (seed != 0) {
            byte[] F = UnpackLittleUint32(seed);

            for (int i = 0; i < SOURCE.length; i++) {
                random[i] = SOURCE[i] ^ F[0];
                random[i] ^= F[1];
                random[i] ^= F[2];
                random[i] ^= F[3];

                random[i + RandomSize] = random[i];
            }

        } else {
            for (int i = 0; i < RandomSize; i++)
                random[i + RandomSize] = random[i] = SOURCE[i];
        }
    }

    private static float Dot(int[] g, float x, float y, float z, float t) {
        return g[0] * x + g[1] * y + g[2] * z + g[3] * t;
    }

    private static float Dot(int[] g, float x, float y, float z) {
        return g[0] * x + g[1] * y + g[2] * z;
    }

    private static float Dot(int[] g, float x, float y) {
        return g[0] * x + g[1] * y;
    }

    private static int FastFloor(float x) {
        return x >= 0 ? (int) x : (int) x - 1;
    }

    private static byte[] UnpackLittleUint32(int value) {
        final byte[] buffer = new byte[4];
        buffer[0] = (byte) (value & 0x00ff);
        buffer[1] = (byte) ((value & 0xff00) >> 8);
        buffer[2] = (byte) ((value & 0x00ff0000) >> 16);
        buffer[3] = (byte) ((value & 0xff000000) >> 24);
        return buffer;
    }
}
