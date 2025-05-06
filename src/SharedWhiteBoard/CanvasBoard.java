package SharedWhiteBoard;

import RMI.ManagerService;
import ShapeLibrary.Shape;

import javax.swing.*;
import java.awt.*;

public class CanvasBoard extends JPanel {

    private Shape[] shapeArray;
    private Image image;

    public void setImage(Image image) {
        this.image = image;
    }

    public CanvasBoard(Shape[] shapeArray) {
        this.shapeArray = shapeArray;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
        for (Shape shape : shapeArray) {
            if (shape != null) {
                shape.draw((Graphics2D) g);
            }
        }
    }

    public void paintAll(Shape[] shapeArray) {
        setShapeArray(shapeArray);
        repaint();
    }

    public void setShapeArray(Shape[] shapeArray) {
        this.shapeArray = shapeArray;
    }

}
