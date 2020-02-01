package engine.terrain.lowpoly;

import engine.models.Lod;
import engine.models.Model;
import engine.models.SimpleModel;
import maths.objects.Box;
import opengl.objects.IVbo;
import opengl.objects.Vao;

public class LPTerrainModel implements Model {

    private final SimpleModel model;

    private final Vao vao;
    private final IVbo dataVbo;

    public LPTerrainModel(SimpleModel model, Vao vao, IVbo dataVbo) {
        this.model = model;
        this.vao = vao;
        this.dataVbo = dataVbo;
    }

    public Vao getVao() {
        return vao;
    }

    public IVbo getDataVbo() {
        return dataVbo;
    }

    public IVbo getIndexVbo() {
        return getLod().current();
    }

    @Override
    public void render() {
        model.render();
    }

    @Override
    public Lod getLod() {
        return model.getLod();
    }

    @Override
    public Box getBounds() {
        return model.getBounds();
    }

    @Override
    public void delete() {
        model.delete();
    }
}
