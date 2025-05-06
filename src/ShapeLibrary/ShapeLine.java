package ShapeLibrary;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class ShapeLine extends Shape {
    private static final long serialVersionID = 3456787L;

    public ShapeLine(int x1, int y1, int x2, int y2, Color color,
                     int stroke) {
        super(x1, y1, x2, y2, color, stroke);
    }


    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(getStroke()));
        g.setColor(getColor());
        g.drawLine(getX1(), getY1(), getX2(), getY2());
    }
}
