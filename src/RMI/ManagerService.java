package RMI;

import Guest.ClientServiceImp;
import ShapeLibrary.Shape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ManagerService extends Remote {

    public void registerClient(String name, String ip) throws RemoteException;

    public void unregisterClient(String name) throws RemoteException;

    public void broadcastMessage(String name, String message, Boolean isHost) throws RemoteException;

    public void broadcastPainting(String name, Shape[] shapes, Boolean isHost, int num) throws RemoteException;

    public boolean isRegistered(String name) throws RemoteException;

    public Boolean checkPermitted(String name) throws RemoteException;

    public void broadcastUpdate() throws RemoteException;

    public void setService(ManagerService manager) throws RemoteException;

    public void draw(Shape[] shapes, int num) throws RemoteException;

    public ArrayList<String> updateUser() throws RemoteException;

    public void closeBoard() throws RemoteException;


}
