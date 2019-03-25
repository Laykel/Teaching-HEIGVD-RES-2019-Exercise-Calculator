package ch.heigvd.res.calculator.client;

import javax.management.NotificationListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    final static Logger LOG = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        Socket clientSocket;
        BufferedReader in;
        PrintWriter out;
        boolean connected = false;

        try {
            clientSocket = new Socket("10.192.107.31", 9998);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            connected = true;
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unable to connect to server: {0}", e.getMessage());
            return;
        }

        String notification;
        try {
            while ((connected && (notification = in.readLine()) != null)) {
                LOG.log(Level.INFO, "Server notification for {1}: {0}", new Object[]{notification});
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Connection problem in client used by {1}: {0}", new Object[]{e.getMessage()});
            connected = false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }

            if (out != null) {
                out.close();
            }

            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        out.println("HELLO ");
        out.flush();
    }
}
