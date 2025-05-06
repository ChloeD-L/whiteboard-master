package SharedWhiteBoard;

import RMI.ManagerService;
import ShapeLibrary.*;
import ShapeLibrary.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Random;

public class DrawListener implements ActionListener, MouseListener, MouseMotionListener {

    private String type = "Pencil";// 声明图形属性，用来存储用户选择的图形
    private Color color = Color.black;// 声明颜色属性，用来存储用户选择的颜色
    private Graphics g;// 声明Graphics画笔类的对象名属性
    private int x1, y1, x2, y2;// 坐标
    private Shape shape;// 声明图形对象名
    private Shape[] shapeArray;// 声明存储图形对象的数组对象名属性
    public int number = 0;// 记录数器，用来记录已经存储的图形个数。
    private JPanel panel4;
    public ManagerService managerService;
    public String kickedMember;
    private int thickness = 1;
    private Boolean isHost;
    private String username;

    public DrawListener(JPanel panel4, Shape[] shapeArray, Boolean isHost, String name) {

        this.panel4 = panel4;
        this.shapeArray = shapeArray;
        this.isHost = isHost;
        this.username = name;
    }

    public void setColor(Color color) {
        this.color = color;
        System.out.println("color = " + color);
    }

    public void setManagerService(ManagerService managerService) {
        this.managerService = managerService;
    }

    public Shape[] getShapeArray() {
        return shapeArray;
    }

    public void setShapeArray(Shape[] shapeArray, int number) {
        this.shapeArray = shapeArray;
        this.number = number;
    }

    public void setG(Graphics g) {
        this.g = g;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();// Get source object
        type = button.getText();// Get text from button
        System.out.println("Type = " + type);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x1 = e.getX();
        y1 = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();
        // Draw the shape according to the coordinate values pressed and released.
        if (type.equals("Line")) {
            // Instantiate graphical objects based on data
            shape = new ShapeLine(x1, y1, x2, y2, color, thickness);
            // Call the drawing method of the graph
            shape.draw((Graphics2D) g);

            if (number < shapeArray.length) {
                // Put shape object into the array
                shapeArray[number++] = shape;
            }
            try {
                managerService.broadcastPainting(username, shapeArray, isHost, number);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }else if (type.equals("Rectangle")) {
            shape = new ShapeRect(x1, y1, x2, y2, color, thickness);
            shape.draw((Graphics2D) g);
            if (number < shapeArray.length) {
                shapeArray[number++] = shape;
            }
            try {
                managerService.broadcastPainting(username, shapeArray, isHost, number);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        } else if (type.equals("Oval")) {
            shape = new ShapeOval(x1, y1, x2, y2, color, thickness);
            shape.draw((Graphics2D) g);
            if (number < shapeArray.length) {
                shapeArray[number++] = shape;
            }
            try {
                managerService.broadcastPainting(username, shapeArray, isHost, number);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        } else if (type.equals("Circle")){
            shape = new ShapeCircle(x1, y1, x2, y2, color, thickness);
            shape.draw((Graphics2D) g);
            if (number < shapeArray.length) {
                shapeArray[number++] = shape;
            }
            try {
                managerService.broadcastPainting(username, shapeArray, isHost, number);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        } else if (type.equals("Text")) {
            String text = JOptionPane.showInputDialog("Enter your text");
            if (!text.equals("")) {
                shape = new ShapeString(x1, y1, x2, y2, color, thickness);
                shape.setText(text);
                shape.draw((Graphics2D) g);
                if (number < shapeArray.length) {
                    shapeArray[number++] = shape;
                }
            }
            try {
                managerService.broadcastPainting(username, shapeArray, isHost, number);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        x2 = e.getX();
        y2 = e.getY();
        Graphics2D g2d = (Graphics2D) g;
        if (type.equals("Pencil")) {
            shape = new ShapeLine(x1, y1, x2, y2, color, thickness);
            shape.draw(g2d);
            if (number < shapeArray.length) {
                shapeArray[number++] = shape;
            }
            try {
                managerService.broadcastPainting(username, shapeArray, isHost, number);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            x1 = x2;
            y1 = y2;
        } else if (type.equals("Eraser")) {
            shape = new ShapeEraser(x1, y1, x2, y2, color, thickness);
            shape.draw((Graphics2D) g);

            if (number < shapeArray.length) {
                // 将图形对象存入到数组中
                shapeArray[number++] = shape;
            }

            try {
                managerService.broadcastPainting(username, shapeArray, isHost, number);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }

            x1 = x2;
            y1 = y2;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void broadcastDraw() {

    }
}
