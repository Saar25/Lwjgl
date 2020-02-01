package openal.objects;

import openal.constants.BufferFormat;
import org.lwjgl.openal.AL10;

import java.nio.ByteBuffer;

public class SoundBuffer {

    private final int id;

    private SoundBuffer(int id) {
        this.id = id;
    }

    public static SoundBuffer create() {
        int id = AL10.alGenBuffers();
        return new SoundBuffer(id);
    }

    public void loadSound(BufferFormat format, ByteBuffer buffer, int frequency) {
        AL10.alBufferData(id, format.getValue(), buffer, frequency);
    }

    public void playFrom(Source source) {
        source.assignBuffer(id);
        source.play();
    }

    /**
     * Deletes the buffer
     */
    public void delete() {
        AL10.alDeleteBuffers(id);
    }
}
