package tegui.objects;

import tegui.graphics.Graphics;
import tegui.style.property.Colour;

public class TSquare extends TGraphical {

    public TSquare(int width, int height) {
        super(width, height);
    }

    @Override
    public void paint(Graphics g) {
        g.setColour(new Colour(.1f, .1f, .1f, .5f));
        g.fillRectangle(100, 100, 1000, 600);
        g.setColour(Colour.PURPLE);
        g.drawRectangle(10, 10, 10, 10);
        g.setColour(Colour.BLUE);
        g.fillOval(50, 50, 20, 30);
        g.setColour(Colour.RED);
        g.fillRectangle(50, 200, 100, 100);
    }
}
