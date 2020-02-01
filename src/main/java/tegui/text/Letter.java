package tegui.text;

import engine.rendering.Spatial;

public class Letter extends Spatial {

    private final char letter;

    public Letter(char letter) {
        this.letter = letter;
    }

    public char getLetter() {
        return letter;
    }

    @Override
    public void process() {
        LetterRenderer.getInstance().process(this);
    }
}
