package SharedWhiteBoard;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import RMI.ManagerService;
import ShapeLibrary.Shape;

public class Whiteboard {
    public JFrame frame;
    public JButton btnCurrentColor;
    private Color currentColor = Color.BLACK;
    private JColorChooser colorChooser;
    private String name;
    private Boolean isHost;
    public JButton btnKick;
    public DefaultListModel<String> listModel;
    public JList<String> memberList;
    public JTextArea messageTextArea;
    private JTextField inputTextField;
    private JPanel chatBox;
    public JButton sendButton;
    private JPanel memberBox;
    private JPanel menuPanel;
    private JPanel colorPanel;
    public CanvasBoard canvas;
    public Shape[] shapeArray = new Shape[10000];
    public DrawListener dl;
    private ManagerService managerService;
    public String[] memberNames;

    private static final Color[] DEFAULTCOLORS = { Color.BLACK, Color.BLUE, Color.WHITE, Color.GRAY, Color.RED, Color.GREEN,
            Color.ORANGE, Color.YELLOW, Color.PINK, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.CYAN, Color.MAGENTA};

    public Whiteboard (String name, Boolean isHost) {
        this.name = name;
        this.isHost = isHost;
    }

    public void updateListener(Shape[] shapes, int number) {
        shapeArray = shapes;
        dl.setShapeArray(shapes, number);
    }

    public void initialize(ManagerService managerService) {
        this.managerService = managerService;
        colorChooser = new JColorChooser(currentColor);
        // Create the main JFrame
        frame = new JFrame("Shared white board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMemberBox();
        setChatBox();
        setColorPanel();

        dl = new DrawListener(canvas, shapeArray,isHost, name);
        dl.setManagerService(managerService);

        JPanel menuPanel = new JPanel(new FlowLayout());

        String buttonName[] = {"Line", "Circle", "Oval", "Rectangle", "Pencil", "Text", "Eraser"};
        // Create the buttons
        for (int i = 0; i < buttonName.length; i++) {
            JButton button = new JButton(buttonName[i]);
            button.setActionCommand(buttonName[i]);
            button.addActionListener(dl);
            menuPanel.add(button);
        }

        // Create the JComboBox
        if (isHost) {
            String[] comboBoxItems = {"New", "Save", "SaveAs"};
            JComboBox<String> comboBox = new JComboBox<>(comboBoxItems);
            menuPanel.add(comboBox);
            comboBox.addActionListener(e -> {
                JComboBox cb = (JComboBox)e.getSource();
                String selected = (String)cb.getSelectedItem();
                switch (selected) {
                    case "New":
                        Shape[] shapeArray = new Shape[10000];
                        updateListener(shapeArray, 0);
                        canvas.paintAll(shapeArray);
                        try {
                            managerService.broadcastPainting(name, shapeArray, isHost, 0);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case "Save":
                        FileSaveWindow saver = new FileSaveWindow();
                        saver.save(canvas);
                        break;
                    case "SaveAs":
                        FileSaveWindow saveWindow = new FileSaveWindow();
                        saveWindow.saveAs(canvas);
                    default:
                        break;
                }
            });
        }

        menuPanel.setLayout(new GridLayout(2,4));

        JPanel toolPanel = new JPanel(new BorderLayout());
        toolPanel.add(menuPanel, BorderLayout.WEST);
        toolPanel.add(colorPanel, BorderLayout.CENTER);

        // Create canvas panel
        canvas = new CanvasBoard(shapeArray);
        canvas.setBackground(Color.WHITE);
        canvas.setPreferredSize(new Dimension(400, 400));


        // Add the panels to the main frame
        frame.setLayout(new BorderLayout());

        frame.add(toolPanel, BorderLayout.NORTH);
        frame.add(memberBox, BorderLayout.WEST);
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(chatBox, BorderLayout.EAST);

        // Set the frame size and make it visible
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to close this application?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    if (isHost) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Perform the long-running operation here (the RMI call)
                                    managerService.closeBoard();
                                    frame.dispose();
//                                        System.exit(0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    } else {
//                        System.out.println("222222");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Perform the long-running operation here (the RMI call)
//                                    System.out.println("3333");
                                    managerService.unregisterClient(name);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    frame.dispose();
                    }
                }
            }
        });

        //Setup listener
        Graphics gr = canvas.getGraphics();
        dl.setG(gr);
        canvas.addMouseListener(dl);
        canvas.addMouseMotionListener(dl);


    }

    public void addMessage(String message) {
        messageTextArea.append(message + "\n");
    }

    public String getMessage() {
        return inputTextField.getText();
    }

    public void clearInputField() {
        inputTextField.setText("");
    }

    public void updateMemberList(ArrayList<String> users) {
        memberList.setListData(users.toArray(new String[0]));
    }

    private void setChatBox() {
        chatBox = new JPanel(new BorderLayout());

        // Create a JTextArea for displaying messages
        messageTextArea = new JTextArea(27, 20);
        messageTextArea.setEditable(false);
        JScrollPane messageScrollPane = new JScrollPane(messageTextArea);
        chatBox.add(messageScrollPane, BorderLayout.NORTH);

        // Create a JTextField for entering messages
        inputTextField = new JTextField(20);
        inputTextField.setPreferredSize(new Dimension(20, 5));
        chatBox.add(inputTextField, BorderLayout.CENTER);

        // Create a JButton for sending messages
        sendButton = new JButton("Send");
        chatBox.add(sendButton, BorderLayout.SOUTH);
        sendButton.addActionListener(e -> {
            String message = getMessage();
            if (message != null) {
                addMessage("Me: " + message);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Perform the long-running operation here (the RMI call)
                            managerService.broadcastMessage(name, message, isHost);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                clearInputField();
            }
        });
    }

    private void setMemberBox() {
        // Create member box panel
        memberBox = new JPanel(new BorderLayout());

        listModel = new DefaultListModel<>();
        memberList = new JList<>(listModel);
        memberBox.add(memberList, BorderLayout.NORTH);
        ArrayList<String> membersFromManager = null;
        try {
            membersFromManager = managerService.updateUser();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (!isHost) {
            membersFromManager.add(name);
        }
        memberNames = membersFromManager.toArray(new String[0]);
        memberList.setListData(memberNames);

        // Create a scrollable JTextArea
        JScrollPane scrollPane = new JScrollPane(memberList);
        scrollPane.setPreferredSize(new Dimension(100, 200));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        memberBox.add(scrollPane, BorderLayout.CENTER);

        if (isHost) {
            btnKick = new JButton("Kick out");
            btnKick.addActionListener(e -> {
                String selectedClient = memberList.getSelectedValue();
                if (selectedClient != null && !selectedClient.equals(name)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Perform the long-running operation here (the RMI call)
                                managerService.unregisterClient(selectedClient);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
            memberBox.add(btnKick, BorderLayout.SOUTH);
        } else {
            System.out.println("This is a user");
        }
    }

    private void setColorPanel() {
        // Create color panel
        // Color panel layout
        colorPanel = new JPanel(new BorderLayout());
        colorPanel.setBorder(new TitledBorder(null, "Color Bar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        colorPanel.setLayout(new BorderLayout(0,0));

        // Current color show
        JPanel currentColorPanel = new JPanel();
        colorPanel.add(currentColorPanel, BorderLayout.WEST);
        currentColorPanel.setPreferredSize(new Dimension(80,40));
        currentColorPanel.setLayout(new BorderLayout());

        JLabel currentColorLabel = new JLabel("More color: ");
        currentColorPanel.add(currentColorLabel, BorderLayout.NORTH);

        JPanel showCurrentColor = new JPanel();
        showCurrentColor.setPreferredSize(new Dimension(20, 20));
        currentColorPanel.add(showCurrentColor, BorderLayout.CENTER);

        btnCurrentColor = new JButton();
        showCurrentColor.add(btnCurrentColor);
        btnCurrentColor.setBorderPainted(true);
        btnCurrentColor.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCurrentColor.setActionCommand("Current");
        btnCurrentColor.setBackground(currentColor);
        btnCurrentColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentColor = colorChooser.showDialog(frame, "Color Chooser", currentColor);
                dl.setColor(currentColor);

                btnCurrentColor.setBackground(currentColor);
            }
        });

        btnCurrentColor.setOpaque(true);
        btnCurrentColor.setPreferredSize(new Dimension(20,20));
        currentColorPanel.add(showCurrentColor, BorderLayout.CENTER);

        JPanel defaultColorPanel = new JPanel();
        colorPanel.add(defaultColorPanel, BorderLayout.CENTER);
        defaultColorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton colorButton = null;
        for (int i = 0; i < DEFAULTCOLORS.length; i++) {
            colorButton = new JButton();
            colorButton.setBorderPainted(false);
            colorButton.setBackground(DEFAULTCOLORS[i]);
            colorButton.setOpaque(true);
            colorButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            colorButton.setPreferredSize(new Dimension(20, 20));
            colorButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    currentColor = ((JButton) e.getSource()).getBackground();
                    dl.setColor(currentColor);
                    btnCurrentColor.setBackground(currentColor);
                    System.out.println("Operation: Change color.");
                }
            });
            defaultColorPanel.add(colorButton);
        }
        colorPanel.add(defaultColorPanel, BorderLayout.EAST);
    }

    public boolean isPermitted(String name) {
        int result = JOptionPane.showConfirmDialog(null,
                "Someone wants to share your whiteboard.", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }
}
