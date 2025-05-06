package ShapeLibrary;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class ShapeCircle extends Shape {
    private static final long serialVersionID = 3456789L;

    public ShapeCircle(int x1, int y1, int x2, int y2, Color color,
                       int t) {
        super(x1, y1, x2, y2, color, t);

    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(getStroke()));// Painter thickness
        g.setColor(getColor());// Painter color
        int semiDiameter = (int) Math.sqrt(Math.pow(Math.abs(getX1() - getX2()), 2) + Math.pow(Math.abs(getY2() - getY1()), 2));
        int diameter = 2*semiDiameter;
        g.drawOval(getX1(), getY1(), semiDiameter, semiDiameter);// draw circle

    }
}
