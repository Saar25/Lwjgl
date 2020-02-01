package engine.fileLoaders;

import engine.models.Model;
import engine.models.SimpleModel;

public final class ModelLoader {

    private ModelLoader() {

    }

    /**
     * Loads a file to a model (only vertex data, no textures or whatever)
     *
     * @param fileName the vertex data file
     * @return a new model
     */
    public static Model load(String fileName) throws Exception {
        String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (fileFormat) {
            case "obj":
                return loadOBJ(fileName);
            default:
                throw new Exception("File format " + fileFormat + " is not supported");
        }
    }

    private static Model loadOBJ(String objFile) throws Exception {
        return OBJLoader.loadOBJ(objFile);
    }
}
