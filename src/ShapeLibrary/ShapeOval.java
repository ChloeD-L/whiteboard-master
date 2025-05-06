package ShapeLibrary;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class ShapeOval extends Shape {
    private static final long serialVersionID = 3456786L;

    public ShapeOval(int x1, int y1, int x2, int y2, Color color,
                       int s) {
        super(x1, y1, x2, y2, color, s);

    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(getStroke()));
        g.setColor(getColor());
        int semiWidth = Math.abs(getX1() - getX2());
        int semiHeight = Math.abs(getY2() - getY1());
        int width = 2 * semiWidth;
        int height = 2 * semiHeight;
        g.drawOval(getX1(), getY1(), width, height);

    }
}
