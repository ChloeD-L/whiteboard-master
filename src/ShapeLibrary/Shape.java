package ShapeLibrary;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;


public abstract class Shape implements Serializable {

//    private static final long serialVersionID = 34567890L;

    private int x1, y1, x2, y2;
    private Color color;
    private int thickness;
    private String text;

    public Shape(int x1, int y1, int x2, int y2, Color color, int thickness) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.thickness = thickness;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Painting method
     *
     * @param g Brush object
     */
    public abstract void draw(Graphics2D g);

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getStroke() {
        return thickness;
    }

    @Override
    public String toString() {
        return "Shape{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", color=" + color +
                ", thickness=" + thickness +
                ", text='" + text + '\'' +
                '}';
    }
}
