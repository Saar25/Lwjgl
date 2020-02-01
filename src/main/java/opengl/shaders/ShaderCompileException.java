package opengl.shaders;

public class ShaderCompileException extends Exception {

    public ShaderCompileException() {
    }

    public ShaderCompileException(String message) {
        super(message);
    }

    public ShaderCompileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShaderCompileException(Throwable cause) {
        super(cause);
    }

    public ShaderCompileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
