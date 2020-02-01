package tegui.style.property;

import engine.util.lengths.ILength;
import maths.joml.Vector2f;
import maths.joml.Vector4f;
import maths.utils.Maths;
import maths.utils.Vector2;
import maths.utils.Vector4;
import tegui.style.AbstractStyleProperty;
import tegui.style.Style;

public class Bounds extends AbstractStyleProperty {

    private final Vector2f center = Vector2.create();
    private final Vector4f vector = Vector4.create();

    private final Position position;
    private final Dimensions dimensions;

    public Bounds(Style origin) {
        super(origin);
        this.position = origin.position;
        this.dimensions = origin.dimensions;
    }

    public int getX() {
        return this.position.getX();
    }

    public int getY() {
        return this.position.getY();
    }

    public int getW() {
        return this.dimensions.getWidth();
    }

    public int getH() {
        return this.dimensions.getHeight();
    }

    public void add(float x, float y, int w, int h) {
        this.position.add(x, y);
        this.dimensions.add(w, h);
    }

    public void set(float x, float y, int w, int h) {
        this.position.set(x, y);
        this.dimensions.set(w, h);
    }

    public void set(Bounds bounds) {
        this.position.set(bounds.position);
        this.dimensions.set(bounds.dimensions);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public void setMiddlePosition(float x, float y) {
        this.position.set(x - dimensions.getWidth() / 2f, y - dimensions.getHeight() / 2f);
    }

    public void setMiddleX(float x) {
        this.position.setX(x - dimensions.getWidth() / 2f);
    }

    public void setMiddleX(ILength x) {
        this.origin.x.set(length -> x.proportionTo(length) - getW() / 2);
    }

    public void setMiddleY(float y) {
        this.position.setY(y - dimensions.getHeight() / 2f);
    }

    public void setMiddleY(ILength y) {
        this.origin.y.set(length -> y.proportionTo(length) - getH() / 2);
    }

    public void setDimensions(int w, int h) {
        this.dimensions.set(w, h);
    }

    public void setX(int x) {
        this.position.setX(x);
    }

    public void setY(int y) {
        this.position.setY(y);
    }

    public void setW(int w) {
        this.dimensions.setWidth(w);
    }

    public void setH(int h) {
        this.dimensions.setHeight(h);
    }

    public int xMax() {
        return getX() + getW();
    }

    public int yMax() {
        return getY() + getH();
    }

    public float xCenter() {
        return getX() + getW() / 2f;
    }

    public float yCenter() {
        return getY() + getH() / 2f;
    }

    public Vector2f getPosition() {
        return position.asVector();
    }

    public Vector2f getDimensions() {
        return dimensions.asVector();
    }

    public Vector2f getCenter() {
        return center.set(xCenter(), yCenter());
    }

    public boolean contains(float x, float y) {
        return Maths.isBetween(x, getX(), getX() + getW())
                && Maths.isBetween(y, getY(), getY() + getH());
    }

    public Vector4f asVector() {
        return vector.set(getX(), getY(), getW(), getH());
    }

    @Override
    public boolean isInherited() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("[Rectangle: x=%d, y=%d, w=%d, h=%d]",
                getX(), getY(), getW(), getH());
    }

}
