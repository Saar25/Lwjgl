package engine.particles;

import engine.models.InstancedModel;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.Attribute;
import opengl.objects.DataBuffer;
import opengl.objects.Vao;
import opengl.utils.DynamicFloatBuffer;

public class ParticlesMemoryManager {

    private final ParticleSystem particleSystem;
    private DynamicFloatBuffer floatBuffer;

    private final Vao vao;
    private final DataBuffer dataBuffer;

    public ParticlesMemoryManager(ParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
        this.floatBuffer = new DynamicFloatBuffer(1000);

        this.vao = Vao.create();
        this.dataBuffer = new DataBuffer(VboUsage.DYNAMIC_DRAW);
        this.dataBuffer.allocateFloat(floatBuffer.get().capacity());
        this.vao.loadDataBuffer(dataBuffer,
                Attribute.ofInstance(0, 3, DataType.FLOAT, false),  // Position
                Attribute.ofInstance(1, 2, DataType.FLOAT, false)); // Scale, Age
    }

    public InstancedModel createModel() {
        return new InstancedModel(vao, RenderMode.TRIANGLE_STRIP, 4);
    }

    public void update() {
        floatBuffer.get().clear();
        if (floatBuffer.ensureCapacity(particleSystem.getChildren().size() * 5) != null) {
            dataBuffer.allocateFloat(floatBuffer.get().capacity());
        }
        for (Particle particle : particleSystem.getChildren()) {
            particle.getTransform().getWorldPosition().get(floatBuffer.get());
            floatBuffer.get().position(floatBuffer.get().position() + 3);
            floatBuffer.get().put(particle.getTransform().getScale().x);
            floatBuffer.get().put(particle.getNormalizedAge());
        }
        floatBuffer.get().flip();
        dataBuffer.storeData(0, floatBuffer.get());
    }

    public void bind() {
        this.vao.bind();
        this.vao.enableAttributes();
    }

    public void delete() {
        this.vao.delete(true);
    }


}
