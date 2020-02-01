package opengl.constants;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public enum DataType {

    U_BYTE_3_3_2(GL12.GL_UNSIGNED_BYTE_3_3_2, 4),
    U_BYTE_2_3_3_REV(GL12.GL_UNSIGNED_BYTE_2_3_3_REV, 4),

    U_SHORT_5_6_5(GL12.GL_UNSIGNED_SHORT_5_6_5, 2),
    U_SHORT_4_4_4_4(GL12.GL_UNSIGNED_SHORT_4_4_4_4, 2),
    U_SHORT_5_5_5_1(GL12.GL_UNSIGNED_SHORT_5_5_5_1, 2),
    U_SHORT_5_6_5_REV(GL12.GL_UNSIGNED_SHORT_5_6_5_REV, 2),
    U_SHORT_4_4_4_4_REV(GL12.GL_UNSIGNED_SHORT_4_4_4_4_REV, 2),
    U_SHORT_1_5_5_5_REV(GL12.GL_UNSIGNED_SHORT_1_5_5_5_REV, 2),

    U_INT_8_8_8_8(GL12.GL_UNSIGNED_INT_8_8_8_8, 4),
    U_INT_10_10_10_2(GL12.GL_UNSIGNED_INT_10_10_10_2, 4),
    U_INT_8_8_8_8_REV(GL12.GL_UNSIGNED_INT_8_8_8_8_REV, 4),
    U_INT_2_10_10_10_REV(GL12.GL_UNSIGNED_INT_2_10_10_10_REV, 4),

    DOUBLE(GL11.GL_DOUBLE, 8),
    FLOAT(GL11.GL_FLOAT, 4),
    SHORT(GL11.GL_SHORT, 2),
    BYTE(GL11.GL_BYTE, 1),
    INT(GL11.GL_INT, 4),

    U_SHORT(GL11.GL_UNSIGNED_SHORT, 2),
    U_BYTE(GL11.GL_UNSIGNED_BYTE, 1),
    U_INT(GL11.GL_UNSIGNED_INT, 4),

    BYTES_2(GL11.GL_2_BYTES, 2),
    BYTES_3(GL11.GL_3_BYTES, 3),
    BYTES_4(GL11.GL_4_BYTES, 4),
    ;

    private final int value;
    private final int bytes;

    DataType(int value, int bytes) {
        this.value = value;
        this.bytes = bytes;
    }

    public int get() {
        return value;
    }

    public int getBytes() {
        return bytes;
    }
}
