package Guest;

import RMI.ClientService;
import RMI.ManagerService;
import ShapeLibrary.Shape;
import SharedWhiteBoard.Whiteboard;

import javax.swing.*;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientServiceImp extends UnicastRemoteObject implements ClientService, Serializable {

    private String name;
    private String ip;
    private Whiteboard frame;
    public ManagerService manager;

    private static final long serialVersionUID = 1L;

    protected ClientServiceImp(String name, String ip) throws RemoteException {
        super();
        this.name = name;
        this.ip = ip;
        try {
            manager = (ManagerService) Naming.lookup("rmi://" + ip + "/canvasManager");
        } catch (NotBoundException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isExist(String name) throws RemoteException {
        return manager.isRegistered(name);
    }

    public void userCreate(String name) {
        frame = new Whiteboard(name, false);
        frame.initialize(manager);
        try {
            manager.registerClient(name, ip);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            frame.addMessage(message);
        });
    }

    @Override
    public void draw(Shape[] shapes, int num) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            frame.updateListener(shapes, num);
            frame.canvas.paintAll(shapes);
        });
    }

    @Override
    public void updateUsers(ArrayList<String> users) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            frame.updateMemberList(users);
        });
    }

    @Override
    public void close() throws RemoteException {
//        frame = null;
        if (frame.frame != null) {
            frame.frame.dispose();
//            System.exit(0);
        }
        JOptionPane.showMessageDialog(null, "Host has closed the canvas");

    }
}
