package ShapeLibrary;

import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Graphics2D;

public class ShapeString extends Shape{
    private static final long serialVersionID = 345674L;
    private String text;
    public ShapeString(int x1, int y1, int x2, int y2, Color color, int s) {
        super(x1, y1, x2, y2,color,s);

    }

    public void setText(String text) {
        this.text = text;
    }

    public void draw(Graphics2D g){
        g.setStroke(new BasicStroke(getStroke()));
        g.setColor(getColor());
        g.drawString(text, getX1(), getY1());
    }

}
