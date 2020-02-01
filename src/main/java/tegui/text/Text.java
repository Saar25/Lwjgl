package tegui.text;

import engine.rendering.Spatial;
import engine.util.node.Node;
import maths.joml.Vector2f;
import maths.utils.Vector2;
import opengl.constants.DataType;
import opengl.constants.VboUsage;
import opengl.objects.Attribute;
import opengl.objects.BufferUtils;
import opengl.objects.DataBuffer;
import opengl.objects.Vao;
import opengl.utils.MemoryUtils;

import java.nio.FloatBuffer;

public class Text extends Spatial {

    private final Node letters;

    private final Vao vao;
    private final Vector2f position;

    public Text(Letter... letters) {
        this.letters = new Node();
        for (Letter letter : letters) {
            this.letters.attachChild(letter);
        }
        this.position = Vector2.of(0, 0);
        this.vao = Vao.create();

        final DataBuffer dataBuffer = BufferUtils.loadToDataBuffer(
                VboUsage.STATIC_DRAW, toBuffer(letters));

        vao.loadDataBuffer(dataBuffer,
                Attribute.ofInstance(0, 1, DataType.INT, false),
                Attribute.ofInstance(1, 2, DataType.FLOAT, false));
    }

    public Text(String s) {
        this(toLetters(s));
    }

    private static Letter[] toLetters(String s) {
        Letter[] letters = new Letter[s.length()];
        char[] characters = s.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            char c = characters[i];
            letters[i] = new Letter(c);
        }
        return letters;
    }

    private FloatBuffer toBuffer(Letter... letters) {
        FloatBuffer data = MemoryUtils.allocFloat(letters.length * 3);
        for (Letter letter : letters) {
            data.put(letter.getLetter());
            data.put(position.x);
            data.put(position.y);
        }
        return data;
    }

    @Override
    public void process() {
        letters.process();
    }

    @Override
    public void update() {
        letters.process();
    }

    @Override
    public void delete() {
        letters.delete();
        vao.delete(true);
    }
}
