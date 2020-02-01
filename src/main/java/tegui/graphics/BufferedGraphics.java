package tegui.graphics;

import maths.objects.Polygon;
import opengl.textures.Texture2D;
import opengl.utils.MemoryUtils;
import org.lwjgl.system.MemoryUtil;
import tegui.style.property.IColour;

import java.nio.IntBuffer;

public class BufferedGraphics implements Graphics {

    private final Texture2D texture;
    private final IntBuffer buffer;

    private IColour colour;

    public BufferedGraphics(Texture2D texture) {
        this.buffer = MemoryUtils.callocInt(texture.getWidth() * texture.getHeight());
        this.texture = texture;
    }

    private int getColour(int x, int y) {
        int position = x + y * texture.getWidth();
        int colour = buffer.get(position);
        colour = (colour << 4) + buffer.get(position + 1);
        colour = (colour << 4) + buffer.get(position + 2);
        colour = (colour << 4) + buffer.get(position + 3);
        return colour;
    }

    private void setPixel(int x, int y, IColour colour) {
        if (x >= 0 && x < texture.getWidth() && y >= 0 && y < texture.getHeight()) {
            buffer.position(x + y * texture.getWidth());
            buffer.put(colour.asInt());
        }
    }

    @Override
    public void setColour(IColour colour) {
        this.colour = colour;
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        if (x1 > x2) {
            int temp = x2;
            x2 = x1;
            x1 = temp;
        }
        if (y1 > y2) {
            int temp = y2;
            y2 = y1;
            y1 = temp;
        }
        final int xLength = x2 - x1;
        final int yLength = y2 - y1;
        if (xLength > yLength) {
            final float m = yLength == 0 ? 0 : (float) xLength / yLength;
            for (int x = x1; x <= x2; x++) {
                setPixel(x, (int) (y1 + (x - x1) * m), colour);
            }
        } else {
            final float m = xLength == 0 ? 0 : (float) yLength / xLength;
            for (int y = y1; y <= y2; y++) {
                setPixel((int) (x1 + (y - y1) * m), y, colour);
            }
        }
    }

    @Override
    public void drawRectangle(int x, int y, int w, int h) {
        drawLine(x, y, x + w, y);
        drawLine(x, y, x, y + h);
        drawLine(x + w, y, x + w, y + h);
        drawLine(x, y + h, x + w, y + h);
    }

    @Override
    public void fillRectangle(int x, int y, int w, int h) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                setPixel(x + i, y + j, colour);
            }
        }
    }

    @Override
    public void drawOval(int cx, int cy, int a, int b) {
        final float invCx2 = 1f / (a * a);
        final float invCy2 = 1f / (b * b);
        for (int x = cx - a; x < cx + a * 2; x++) {
            for (int y = cy - b; y < cy + b * 2; y++) {
                float dx = x - cx, dy = y - cy;
                if (dx * dx * invCx2 + dy * dy * invCy2 == 1) {
                    setPixel(x, y, colour);
                }
            }
        }
    }

    @Override
    public void fillOval(int cx, int cy, int a, int b) {
        final float invCx2 = 1f / (a * a);
        final float invCy2 = 1f / (b * b);
        for (int x = cx - a; x < cx + a * 2; x++) {
            for (int y = cy - b; y < cy + b * 2; y++) {
                float dx = x - cx, dy = y - cy;
                if (dx * dx * invCx2 + dy * dy * invCy2 <= 1) {
                    setPixel(x, y, colour);
                }
            }
        }
    }

    @Override
    public void fillPolygon(Polygon polygon) {

    }

    @Override
    public void clear(IColour clearColour) {
        buffer.clear();
        final int colour = clearColour.asInt();
        for (int i = 0; i < buffer.limit(); i++) {
            buffer.put(colour);
        }
    }

    @Override
    public void process() {
        buffer.flip().limit(buffer.capacity());
        texture.load(buffer);
    }

    @Override
    public void delete() {
        MemoryUtil.memFree(buffer);
    }
}
