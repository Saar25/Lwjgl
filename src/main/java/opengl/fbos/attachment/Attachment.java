package opengl.fbos.attachment;

import opengl.fbos.Fbo;
import opengl.textures.Texture;

public interface Attachment {

    int getAttachmentPoint();

    AttachmentType getAttachmentType();

    Texture getTexture();

    void init(Fbo fbo);

    void delete();

}
