package opengl.fbos.attachment;

public abstract class AbstractAttachment implements Attachment {

    private final AttachmentType type;
    private final int attachmentIndex;

    public AbstractAttachment(AttachmentType type, int attachmentIndex) {
        this.type = type;
        this.attachmentIndex = attachmentIndex;
    }

    @Override
    public int getAttachmentPoint() {
        return getAttachmentType().get() + attachmentIndex;
    }

    @Override
    public AttachmentType getAttachmentType() {
        return type;
    }
}
