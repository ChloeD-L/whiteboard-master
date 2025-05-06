package SharedWhiteBoard;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;


public class FileSaveWindow {

    public String filePath;
    public String type;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void save(JPanel panel) {
        JFrame frame = new JFrame("File Saver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

        JTextField fileNameField = new JTextField(20);
        String[] fileTypes = {".jpeg", ".png"};
        JComboBox<String> fileTypeComboBox = new JComboBox<>(fileTypes);
        JButton saveButton = new JButton("Save File");

        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            String fileName = fileNameField.getText();
            String fileType = (String)fileTypeComboBox.getSelectedItem();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            fileChooser.setSelectedFile(new File(fileName + fileType));
            int returnValue = fileChooser.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fullFilePath = selectedFile.getAbsolutePath();
                System.out.println("Saved file path: " + fullFilePath);
                imageSaver(panel, fullFilePath, fileType);
                frame.dispose();
            }
        });

        frame.add(new JLabel("Enter file name:"));
        frame.add(fileNameField);
        frame.add(new JLabel("Choose file type:"));
        frame.add(fileTypeComboBox);
        frame.add(saveButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void saveAs(JPanel panel) {
        JFrame frame = new JFrame("File Saver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        String[] fileTypes = {".jpeg", ".png"};
        JComboBox<String> fileTypeComboBox = new JComboBox<>(fileTypes);

        JButton saveButton = new JButton("Save File");

        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = fileChooser.showSaveDialog(frame);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String fileType = (String)fileTypeComboBox.getSelectedItem();
                File selectedDirectory = fileChooser.getSelectedFile();
                String fullFilePath = selectedDirectory.getAbsolutePath() + fileType;
                type = fileType;
                System.out.println("Saved file path: " + fullFilePath);
                setFilePath(fullFilePath);
                imageSaver(panel, fullFilePath, fileType);
                frame.dispose();
            }
        });

        frame.add(new JLabel("Choose file type:"));
        frame.add(fileTypeComboBox);
        frame.add(saveButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void imageSaver(JPanel panel, String filename, String fileType) {
        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        panel.paint(g2d);

        g2d.dispose();

        try {
            if (fileType.equals(".jpeg")) {
                ImageIO.write(image, "jpeg", new File(filename));
            } else {
                ImageIO.write(image, "png", new File(filename));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

