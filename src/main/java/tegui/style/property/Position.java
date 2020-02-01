package tegui.style.property;

import maths.joml.Vector2f;
import maths.utils.Vector2;
import tegui.style.Style;

public class Position {

    private static final Vector2f vector = Vector2.create();

    private final Coordinate x;
    private final Coordinate y;

    public Position(Style origin) {
        this.x = origin.x;
        this.y = origin.y;
    }

    public void add(float x, float y) {
        this.x.add((int) x);
        this.y.add((int) y);
    }

    public void setX(float x) {
        this.x.set((int) x);
    }

    public void setY(float y) {
        this.y.set((int) y);
    }

    public void set(float x, float y) {
        setX((int) x);
        setY((int) y);
    }

    public void set(Vector2f position) {
        set(position.x, position.y);
    }

    public void set(Position position) {
        set(position.asVector());
    }

    public Vector2f asVector() {
        return vector.set(getX(), getY());
    }

    public int getX() {
        return this.x.get();
    }

    public int getY() {
        return this.y.get();
    }

}
