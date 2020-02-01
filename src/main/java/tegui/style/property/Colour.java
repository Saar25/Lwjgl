package tegui.style.property;

import maths.joml.Vector3f;
import maths.joml.Vector4f;
import maths.utils.Vector3;
import maths.utils.Vector4;
import tegui.style.Style;
import tegui.style.AbstractStyleProperty;

public class Colour extends AbstractStyleProperty implements IColour {

    public static final IColour TRANSPARENT = new ImmutableColour(0, 0, 0, 0);

    public static final IColour WHITE = new ImmutableColour(1, 1, 1, 1);

    public static final IColour LIGHT_GREY = new ImmutableColour(.7f, .7f, .7f, 1);

    public static final IColour GREY = new ImmutableColour(.5f, .5f, .5f, 1);

    public static final IColour DARK_GREY = new ImmutableColour(.3f, .3f, .3f, 1);

    public static final IColour BLACK = new ImmutableColour(0, 0, 0, 1);

    public static final IColour RED = new ImmutableColour(1, 0, 0, 1);

    public static final IColour GREEN = new ImmutableColour(0, 1, 0, 1);

    public static final IColour BLUE = new ImmutableColour(0, 0, 1, 1);

    public static final IColour PURPLE = new ImmutableColour(1, 0, 1, 1);

    public static final IColour CYAN = new ImmutableColour(0, 1, 1, 1);

    public static final IColour YELLOW = new ImmutableColour(1, 1, 0, 1);

    public static final IColour ORANGE = new ImmutableColour(1, .5f, 0, 1);

    public static final IColour BROWN = new ImmutableColour(0.39f, 0.26f, 0.13f, 1);

    private final Vector4f rgbaVector = Vector4.create();
    private final Vector3f rgbVector = Vector3.create();

    public float r = 0;
    public float g = 0;
    public float b = 0;
    public float a = 1;

    public Colour() {

    }

    public Colour(int r, int g, int b) {
        this.set(r, g, b);
    }

    public Colour(int r, int g, int b, int a) {
        this.set(r, g, b, a);
    }

    public Colour(float r, float g, float b) {
        this.setNormalized(r, g, b, 1);
    }

    public Colour(float r, float g, float b, float a) {
        this.setNormalized(r, g, b, a);
    }

    public Colour(Style origin) {
        super(origin);
    }

    public Colour(Style origin, float r, float g, float b, float a) {
        super(origin);
        setNormalized(r, g, b, a);
    }

    public void set(String value) {
        if (value.startsWith("#")) {
            if (value.length() == 7) {
                this.setRGB(Integer.parseInt(value.substring(1), 16));
            } else if (value.length() == 9) {
                this.setRGBA(Integer.parseInt(value.substring(1), 16));
            } else {
                throw new IllegalArgumentException("Cannot parse colour " + value);
            }
        } else {
            String[] split = value.split(" ");
            if (split.length != 4 && split.length != 3) {
                throw new IllegalArgumentException("Cannot parse colour " + value);
            }
            if (split[0].endsWith("%")) {
                float r = Integer.parseInt(split[0].substring(0, split[0].length() - 1)) / 100f;
                float g = Integer.parseInt(split[1].substring(0, split[1].length() - 1)) / 100f;
                float b = Integer.parseInt(split[2].substring(0, split[2].length() - 1)) / 100f;
                float a = split.length == 3 ? 1 : Integer.parseInt(split[3].substring(0, split[3].length() - 1)) / 100f;
                this.setNormalized(r, g, b, a);
            } else {
                float r = Integer.parseInt(split[0]) / 255f;
                float g = Integer.parseInt(split[1]) / 255f;
                float b = Integer.parseInt(split[2]) / 255f;
                float a = split.length == 3 ? 1 : Integer.parseInt(split[3]) / 100f;
                this.setNormalized(r, g, b, a);
            }
        }
    }

    public void setRGB(int rgb) {
        this.setNormalized(((rgb >> 16) & 255) / 255f,
                ((rgb >> 8) & 255) / 255f,
                ((rgb) & 255) / 255f, 1);
    }

    public void setRGBA(int rgba) {
        this.setNormalized(
                ((rgba >> 24) & 255) / 255f,
                ((rgba >> 16) & 255) / 255f,
                ((rgba >> 8) & 255) / 255f,
                ((rgba) & 255) / 255f);
    }

    public void set(float r, float g, float b, float a, boolean normalized) {
        if (normalized) {
            this.setNormalized(r, g, b, a);
        } else {
            this.set((int) r, (int) g, (int) b, (int) a);
        }
    }

    public void set(float r, float g, float b, boolean normalized) {
        if (normalized) {
            this.setNormalized(r, g, b);
        } else {
            this.set((int) r, (int) g, (int) b);
        }
    }

    public void set(int r, int g, int b, int a) {
        this.setNormalized(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public void set(int r, int g, int b) {
        this.set(r, g, b, 255);
    }

    public void setNormalized(float r, float g, float b, float a) {
        super.setInherited(false);
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setNormalized(float r, float g, float b) {
        this.setNormalized(r, g, b, 1);
    }

    public void set(IColour colour) {
        this.setNormalized(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha());
    }

    @Override
    public Vector3f rgbVector() {
        return rgbVector.set(r, g, b);
    }

    @Override
    public Vector4f rgbaVector() {
        return rgbaVector.set(r, g, b, a);
    }

    @Override
    public float getRed() {
        return r;
    }

    @Override
    public float getGreen() {
        return g;
    }

    @Override
    public float getBlue() {
        return b;
    }

    @Override
    public float getAlpha() {
        return a;
    }

    @Override
    public String toString() {
        return string();
    }
}
