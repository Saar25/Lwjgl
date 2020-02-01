package tegui;

import engine.rendering.Spatial;
import engine.util.node.IParent;

import java.util.List;

public abstract class TParent extends TComponent implements IParent {

    @Override
    public void process() {
        getChildren().forEach(Spatial::process);
    }

    @Override
    public void update() {
        for (int i = 0; i < getChildren().size(); i++) {
            getChildren().get(i).update();
        }
    }

    @Override
    public void delete() {
        getChildren().forEach(Spatial::delete);
    }
}
