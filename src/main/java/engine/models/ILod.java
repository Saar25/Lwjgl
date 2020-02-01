package engine.models;

import opengl.objects.IVbo;

public interface ILod {

    IVbo current();

    boolean available();

}
