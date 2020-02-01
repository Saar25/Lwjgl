package maths.objects;

import maths.utils.Maths;
import maths.joml.Vector2fc;

public class Rectangle {

    public float x;
    public float y;
    public float w;
    public float h;

    public Rectangle(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Rectangle(Vector2fc position, Vector2fc dimensions) {
        this.x = position.x();
        this.y = position.y();
        this.w = dimensions.x();
        this.h = dimensions.y();
    }

    public Rectangle(Rectangle rectangle) {
        this.x = rectangle.x;
        this.y = rectangle.y;
        this.w = rectangle.w;
        this.h = rectangle.h;
    }

    public static Rectangle ofPosition(float x, float y) {
        return new Rectangle(x, y, 0, 0);
    }

    public static Rectangle ofDimentions(float w, float h) {
        return new Rectangle(0, 0, w, h);
    }

    public static Rectangle fromPoints(float x1, float y1, float x2, float y2) {
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    public float centerX() {
        return x + w / 2;
    }

    public float centerY() {
        return y + h / 2;
    }

    public boolean contains(float x, float y) {
        return Maths.isBetween(x, this.x, this.x + this.w) && Maths.isBetween(y, this.y, this.y + this.h);
    }

    @Override
    public String toString() {
        return String.format("[Rectangle: x=%f, y=%f, w=%f, h=%f]",x, y, w, h);
    }
}
