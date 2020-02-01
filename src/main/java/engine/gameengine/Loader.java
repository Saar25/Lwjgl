package engine.gameengine;

import engine.fileLoaders.ModelLoader;
import engine.fileLoaders.TextFileLoader;
import engine.models.Model;
import engine.models.Skin;
import opengl.textures.Texture2D;

public class Loader {

    public String loadTextFile(String file) throws Exception {
        return TextFileLoader.loadResource(file);
    }

    public Texture2D loadTexture(String file) throws Exception {
        return Texture2D.of(file);
    }

    public Model loadModel(String file) throws Exception {
        return ModelLoader.load(file);
    }

    public Skin loadSkin(String file) throws Exception {
        return Skin.of(file);
    }

}
