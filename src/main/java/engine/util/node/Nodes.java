package engine.util.node;

import engine.rendering.Spatial;

import java.util.LinkedList;
import java.util.List;

public final class Nodes {

    private Nodes() {

    }

    public static <T extends Spatial> INode<T> unmodifiableNode(GNode<T> node) {
        return new UnmodifiableNode<>(node);
    }

    public static List<Spatial> spread(IParent node) {
        List<Spatial> list = new LinkedList<>();
        for (Spatial child : node.getChildren()) {
            if (child instanceof IParent) {
                list.addAll(spread((IParent) child));
            } else {
                list.add(child);
            }
        }
        return list;
    }

    public static int size(Spatial node) {
        if (node instanceof IParent) {
            return size((IParent) node);
        }
        return 1;
    }

    public static int size(IParent node) {
        int count = 1;
        for (Spatial child : node.getChildren()) {
            count += size(child);
        }
        return count;
    }

}
