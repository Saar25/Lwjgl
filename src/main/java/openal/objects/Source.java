package openal.objects;

import maths.joml.Vector3fc;
import org.lwjgl.openal.AL10;

public class Source {

    private final int id;

    private Source(int id) {
        this.id = id;
    }

    public static Source create() {
        int id = AL10.alGenSources();
        return new Source(id);
    }

    public void setPosition(float x, float y, float z) {
        AL10.alSource3f(id, AL10.AL_POSITION, x, y, z);
    }

    public void setPosition(Vector3fc p) {
        setPosition(p.x(), p.y(), p.z());
    }

    public void setVelocity(float x, float y, float z) {
        AL10.alSource3f(id, AL10.AL_VELOCITY, x, y, z);
    }

    public void setVelocity(Vector3fc p) {
        setVelocity(p.x(), p.y(), p.z());
    }

    public void setPitch(float pitch) {
        AL10.alSourcef(id, AL10.AL_PITCH, pitch);
    }

    public void setGain(float gain) {
        AL10.alSourcef(id, AL10.AL_GAIN, gain);
    }

    public void setLoop(boolean loop) {
        AL10.alSourcei(id, AL10.AL_LOOPING, loop ? 1 : 0);
    }

    public void assignBuffer(int buffer) {
        AL10.alSourcei(id, AL10.AL_BUFFER, buffer);
    }

    /**
     * Plays the source
     */
    public void play() {
        AL10.alSourcePlay(id);
    }

    /**
     * Pauses the source
     */
    public void pause() {
        AL10.alSourcePause(id);
    }

    /**
     * Stops playing the source
     */
    public void stop() {
        AL10.alSourceStop(id);
    }

    /**
     * Deletes the source
     */
    public void delete() {
        AL10.alDeleteSources(id);
    }
}
