package engine.terrain.triangle;

import engine.rendering.Camera;
import engine.terrain.Terrain;
import engine.util.Lazy;
import engine.util.node.Parent;
import glfw.input.event.ClickEvent;
import glfw.input.event.MoveEvent;
import glfw.input.event.ScrollEvent;
import maths.joml.Matrix4f;
import maths.joml.Vector3f;
import maths.joml.Vector4f;
import maths.objects.Triangle;
import maths.utils.Maths;
import maths.utils.Vector3;
import maths.utils.Vector4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TerrainNode extends Parent {

    private static final int minLevel = 0;

    private static final float sin60 = Maths.sinf(Maths.PI / 3);
    private static final float cos60 = Maths.cosf(Maths.PI / 3);

    public final int level;

    private final Lazy<TerrainNode> center;
    private final Lazy<TerrainNode> right;
    private final Lazy<TerrainNode> left;
    private final Lazy<TerrainNode> top;

    private final Lazy<List<TerrainNode>> children;

    private final Terrain terrain;

    private Camera camera;

    public TerrainNode(Vector3f center, float size, int level) {
        this(null, center, size, level);
    }

    private TerrainNode(TerrainNode parent, Vector3f center, float size, int level) {
        this.terrain = new TriangularTerrain(createTriangle(), size);
        getTransform().setPosition(center);
        getTransform().setScale(size);

        if (parent != null) {
            center.y = parent.terrain.getHeight(center.x, center.z);
        }

        this.level = level;

        this.terrain.setParent(this);
        setParent(parent);

        this.center = new Lazy<>(() -> {
            final TerrainNode terrainNode = new TerrainNode(this, Vector3.of(0, 0, 0), .5f, level - 1);
            terrainNode.getTransform().setRotation(0, 180, 0);
            return terrainNode;
        });
        this.center.onAssign(t -> t.setParent(this));

        this.top = new Lazy<>(() ->
                new TerrainNode(this, Vector3.of(0, 0, 1).mul(.5f), .5f, level - 1));
        this.top.onAssign(t -> t.setParent(this));

        this.left = new Lazy<>(() ->
                new TerrainNode(this, Vector3.of(-sin60, 0, -cos60).mul(.5f), .5f, level - 1));
        this.left.onAssign(t -> t.setParent(this));

        this.right = new Lazy<>(() ->
                new TerrainNode(this, Vector3.of(+sin60, 0, -cos60).mul(.5f), .5f, level - 1));
        this.right.onAssign(t -> t.setParent(this));

        this.children = new Lazy<>(() -> Collections.unmodifiableList(
                Arrays.asList(this.center.get(), this.right.get(), this.left.get(), this.top.get())
        ));
    }

    private static Triangle createTriangle() {
        Vector3f p1 = Vector3.of(0, 0, 1);
        Vector3f p2 = Vector3.of(+sin60, 0, -cos60);
        Vector3f p3 = Vector3.of(-sin60, 0, -cos60);
        return new Triangle(p1, p2, p3);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    private Camera getCamera() {
        return camera == null && getParent() instanceof TerrainNode
                ? ((TerrainNode) getParent()).getCamera() : camera;
    }

    @Override
    public void process() {
        Triangle triangle = createTriangle();
        Matrix4f transformation = getTransform().getTransformationMatrix();
        Vector4f p1 = Vector4.of(triangle.getP1(), 1).mul(transformation);
        Vector4f p2 = Vector4.of(triangle.getP2(), 1).mul(transformation);
        Vector4f p3 = Vector4.of(triangle.getP3(), 1).mul(transformation);
        triangle = new Triangle(Vector3.of(p1).div(p1.w), Vector3.of(p2).div(p2.w), Vector3.of(p3).div(p3.w));

        final Camera camera = getCamera();
        if (camera != null && level > minLevel && triangle.contains(
                camera.getTransform().getPosition().x, camera.getTransform().getPosition().z)) {
            super.process();
        } else {
            terrain.process();
        }
        //if (level >= minLevel) {
        //    super.process();
        //} else {
        //    terrain.process();
        //}
    }

    @Override
    public void update() {
        center.ifAssigned(TerrainNode::update);
        right.ifAssigned(TerrainNode::update);
        left.ifAssigned(TerrainNode::update);
        top.ifAssigned(TerrainNode::update);
    }

    @Override
    public void delete() {
        center.ifAssigned(TerrainNode::delete);
        right.ifAssigned(TerrainNode::delete);
        left.ifAssigned(TerrainNode::delete);
        top.ifAssigned(TerrainNode::delete);
    }

    @Override
    public List<TerrainNode> getChildren() {
        return children.get();
    }

    @Override
    public void onMouseMoveEvent(MoveEvent event) {
        center.ifAssigned(child -> child.onMouseMoveEvent(event));
        right.ifAssigned(child -> child.onMouseMoveEvent(event));
        left.ifAssigned(child -> child.onMouseMoveEvent(event));
        top.ifAssigned(child -> child.onMouseMoveEvent(event));
    }

    @Override
    public void onMouseClickEvent(ClickEvent event) {
        center.ifAssigned(child -> child.onMouseClickEvent(event));
        right.ifAssigned(child -> child.onMouseClickEvent(event));
        left.ifAssigned(child -> child.onMouseClickEvent(event));
        top.ifAssigned(child -> child.onMouseClickEvent(event));
    }

    @Override
    public void onMouseScrollEvent(ScrollEvent event) {
        center.ifAssigned(child -> child.onMouseScrollEvent(event));
        right.ifAssigned(child -> child.onMouseScrollEvent(event));
        left.ifAssigned(child -> child.onMouseScrollEvent(event));
        top.ifAssigned(child -> child.onMouseScrollEvent(event));
    }
}
