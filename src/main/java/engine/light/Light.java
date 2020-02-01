package engine.light;

import engine.rendering.Spatial;

public abstract class Light extends Spatial implements ILight {

    @Override
    public void process() {
        LightBatch.process(this);
    }

}
