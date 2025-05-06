package Guest;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class JoinWhiteBoard {
    public static void main(String[] args) {
        if (args.length != 3) {
            JOptionPane.showMessageDialog(null, "Please enter correct inputs.","Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } else {
            try {
                // Handle input arguments
                int portNum = Integer.parseInt(args[1]);
                String username = args[2];
                String ipAddress = args[0] + ":" + portNum;
                String bindName = "rmi://" + ipAddress + "/" + username;
                // Create a new user
                ClientServiceImp client = new ClientServiceImp(username, ipAddress);
                Boolean isExist = client.isExist(username);
                if (isExist) {
                    JOptionPane.showMessageDialog(null, "User name has been used.","Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                } else {
                    if (client.manager.checkPermitted(username)) {
                        Naming.rebind(bindName, client);
                        client.userCreate(username);
//                        client.manager.unregisterClient(username);
                    } else {
                        System.exit(0);
                    }
                }
            } catch (RemoteException | MalformedURLException e) {
                JOptionPane.showMessageDialog(null, "Error happened when connecting.","Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

        }
    }
}
