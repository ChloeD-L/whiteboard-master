package ShapeLibrary;

import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Graphics2D;

public class ShapeEraser extends Shape{

    private static final long serialVersionID = 3456788L;
    public ShapeEraser(int x1, int y1, int x2, int y2, Color color, int s) {
        super(x1, y1, x2, y2,color,s);

    }
    public void draw(Graphics2D g){
        g.setStroke(new BasicStroke(20));
        g.setColor(Color.WHITE);
        g.drawLine(getX1(),getY1(),getX2(),getY2());

    }

}
