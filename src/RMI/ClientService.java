package RMI;

import ShapeLibrary.Shape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientService extends Remote {
    public void receiveMessage(String message) throws RemoteException;

    public void draw(Shape[] shapes, int num) throws RemoteException;

    public void updateUsers(ArrayList<String> users) throws RemoteException;

    public void close()  throws RemoteException;
}
