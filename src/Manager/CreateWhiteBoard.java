package Manager;

import RMI.ManagerService;
import SharedWhiteBoard.CanvasBoard;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateWhiteBoard {
    public int portNum;
    public String managerName;
    public String IPAddress;

    public CreateWhiteBoard(int portNum, String managerName, String IPAddress) {
        this.portNum = portNum;
        this.managerName = managerName;
        this.IPAddress = IPAddress;
    }

    public static void main (String[] args) {
        try {
            if (args.length != 3) {
                JOptionPane.showMessageDialog(null, "Please enter correct inputs.","Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } else {
                int port = Integer.parseInt(args[1]);
                String ip = args[0] + ":" + args[1];
                CreateWhiteBoard server = new CreateWhiteBoard(port, args[2], args[0]);
                JOptionPane.showMessageDialog(null, "You are the manager now.");
                ManagerService manager = new ManagerServiceImp(server.managerName);
                Registry registry = LocateRegistry.createRegistry(server.portNum);
                Naming.rebind("rmi://" + ip + "/canvasManager", manager);
//                registry.bind("rmi://" + ip + "/canvasManager", manager);

                ManagerService managerService = (ManagerService) Naming.lookup("rmi://" + ip + "/canvasManager");
                manager.setService(managerService);
            }

        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }

    }
}
