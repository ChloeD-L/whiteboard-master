package ShapeLibrary;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class ShapeRect extends Shape {

    private static final long serialVersionID = 3456785L;

    public ShapeRect(int x1, int y1, int x2, int y2, Color color,
                         int s) {
        super(x1, y1, x2, y2, color, s);
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(getStroke()));
        g.setColor(getColor());
        g.drawRect(getX1(), getY1(), Math.abs(getX1() - getX2()), Math.abs(getY1() - getY2()));
    }
}
