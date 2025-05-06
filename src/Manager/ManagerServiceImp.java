package Manager;

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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManagerServiceImp extends UnicastRemoteObject implements ManagerService, Serializable {

    private static final long serialVersionID = 1234567890L;

    private String name;
    private Whiteboard frame;
    private ConcurrentHashMap<String, ClientService> userList;

    public ManagerServiceImp(String memberName) throws RemoteException {
        super();
        this.name = memberName;
        this.userList = new ConcurrentHashMap<>();
    }

    @Override
    public void setService(ManagerService manager) {
        this.frame = new Whiteboard(name, true);
        this.frame.initialize(manager);
    }

    @Override
    public void draw(Shape[] shapes, int num) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            frame.canvas.paintAll(shapes);
            frame.updateListener(shapes, num);
        });
    }

    @Override
    public ArrayList<String> updateUser() throws RemoteException {
        ArrayList<String> list = new ArrayList<>();
        list.add(name);
        list.addAll(userList.keySet());
        return list;
    }

    @Override
    public void closeBoard() throws RemoteException {
        if (userList.size() != 0) {
            for (Map.Entry<String, ClientService> entry : userList.entrySet()) {
                entry.getValue().close();
            }
        }
    }

    @Override
    public Boolean checkPermitted(String name) {
        return frame.isPermitted(name);
    }

    @Override
    public synchronized void broadcastUpdate() throws RemoteException {
        if (userList != null) {
            for (ClientService client : userList.values()) {
                client.updateUsers(updateUser());
            }
        }

    }

    @Override
    public synchronized void registerClient(String name, String ip) throws RemoteException {
        ClientService client = null;
        try {
            client = (ClientService) Naming.lookup("rmi://" + ip + "/" + name);
            userList.put(name, client);
            frame.updateMemberList(updateUser());
            broadcastUpdate();
            frame.addMessage(name + " joined the chat.");
            broadcastMessage("Server", name + " joined the chat.", true);
            client.draw(frame.shapeArray, frame.dl.number);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void unregisterClient(String name) throws RemoteException {
        ClientService removeUser = userList.get(name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    userList.remove(name);
                    removeUser.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        frame.updateMemberList(updateUser());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    broadcastUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    frame.addMessage(name + " has left the chat.");
                    broadcastMessage("Server", name + " has left the chat.", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public synchronized void broadcastMessage(String name, String message, Boolean isHost) throws RemoteException {
        String fullMessage = name + ": " + message;
        if (!isHost) {
            frame.addMessage(fullMessage);
        }
        if (userList.size() != 0) {
            for (Map.Entry<String, ClientService> entry : userList.entrySet()) {
                if (!entry.getKey().equals(name)) {
                    entry.getValue().receiveMessage(fullMessage);
                }
            }
        }
    }

    @Override
    public synchronized void broadcastPainting(String name, Shape[] shapes, Boolean isHost, int num) throws RemoteException {
        if (!isHost) {
            draw(shapes, num);
        }
        if (userList.size() != 0) {
            for (Map.Entry<String, ClientService> entry : userList.entrySet()) {
            if (!entry.getKey().equals(name)) {
                entry.getValue().draw(shapes, num);
            }
            }
        }
    }

    @Override
    public boolean isRegistered(String name) throws RemoteException {
        return userList.containsKey(name);
    }
}
