package opengl.shaders;

import engine.fileLoaders.TextFileLoader;
import org.lwjgl.opengl.GL20;

public class Shader {

    public final int id;
    private final String code;
    private final ShaderType type;

    private Shader(int id, String code, ShaderType type) {
        this.id = id;
        this.code = code;
        this.type = type;
    }

    /**
     * Creates a shader of the given type, with the given file that contains the code
     *
     * @param fileName the code file
     * @param type     the shader type
     * @return a vertex shader
     * @throws Exception In case could not read the file
     */
    public static Shader of(String fileName, ShaderType type) throws Exception {
        final int id = GL20.glCreateShader(type.get());
        final String code = TextFileLoader.loadResource(fileName);
        return new Shader(id, code, type);
    }

    /**
     * Creates a vertex shader with the given file that contains the code
     *
     * @param fileName the code file
     * @return a vertex shader
     * @throws Exception In case could not read the file
     */
    public static Shader createVertex(String fileName) throws Exception {
        return Shader.of(fileName, ShaderType.VERTEX);
    }

    /**
     * Creates a fragment shader with the given file that contains the code
     *
     * @param fileName the code file
     * @return a fragment shader
     * @throws Exception In case could not read the file
     */
    public static Shader createFragment(String fileName) throws Exception {
        return Shader.of(fileName, ShaderType.FRAGMENT);
    }

    /**
     * Creates a geometry shader with the given file that contains the code
     *
     * @param fileName the code file
     * @return a geometry shader
     * @throws Exception In case could not read the file
     */
    public static Shader createGeometry(String fileName) throws Exception {
        return Shader.of(fileName, ShaderType.GEOMETRY);
    }

    /**
     * Creates a tessellation control shader with the given file that contains the code
     *
     * @param fileName the code file
     * @return a tessellation control shader
     * @throws Exception In case could not read the file
     */
    public static Shader createTessControl(String fileName) throws Exception {
        return Shader.of(fileName, ShaderType.TESS_CONTROL);
    }

    /**
     * Creates a tessellation evaluation shader with the given file that contains the code
     *
     * @param fileName the code file
     * @return a tessellation evaluation shader
     * @throws Exception In case could not read the file
     */
    public static Shader createTessEvaluation(String fileName) throws Exception {
        return Shader.of(fileName, ShaderType.TESS_EVALUATION);
    }

    /**
     * Initializes the shader, compiles, and checking for error in the shader code
     *
     * @throws ShaderCompileException In case could not compile the shader code
     */
    public void init() throws ShaderCompileException {
        GL20.glShaderSource(id, code);
        GL20.glCompileShader(id);
        if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == 0) {
            final String infoLog = GL20.glGetShaderInfoLog(id, 1024);
            throw new ShaderCompileException("Shader type: " + type + ", Error compiling Shader code:\n" + infoLog);
        }
    }

    /**
     * Attaches the shader to the given program
     *
     * @param programId the shader program
     */
    public void attach(int programId) {
        GL20.glAttachShader(programId, id);
    }

    /**
     * Detaches the shader from the given program
     *
     * @param programId the shader program
     */
    public void detach(int programId) {
        GL20.glDetachShader(programId, id);
    }

    /**
     * Deletes the shader
     */
    public void delete() {
        GL20.glDeleteShader(id);
    }
}