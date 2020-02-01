package engine.shape.generators;

import engine.models.Model;
import opengl.objects.IVbo;
import opengl.objects.Vao;

public interface IModelGenerator {

    Vao createVao();

    IVbo createDataVbo();

    IVbo createIndexVbo();

    Model generateModel();

    default IVbo createIndexVbo(int lod) {
        throw new UnsupportedOperationException("Level of detail is not supported for " + getClass().getSimpleName());
    }

}
