package openal.constants;

import org.lwjgl.openal.AL11;

public enum BufferFormat {

    FORMAT_MONO8(AL11.AL_FORMAT_MONO8),
    FORMAT_MONO16(AL11.AL_FORMAT_MONO16),
    FORMAT_STEREO8(AL11.AL_FORMAT_STEREO8),
    FORMAT_STEREO16(AL11.AL_FORMAT_STEREO16),
    ;

    private final int value;

    BufferFormat(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
