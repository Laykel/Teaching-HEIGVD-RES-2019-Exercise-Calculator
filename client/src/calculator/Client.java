package calculator;

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
        BufferedReader br;
        PrintWriter out;
        boolean connected = false;

        if (args.length != 2) {
            System.err.println("Usage : <program> <ip-address> <port>");
        }

        try {
            clientSocket = new Socket(args[0], Integer.parseInt(args[1]));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            connected = true;
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unable to connect to server: {0}", e.getMessage());
            return;
        }

        String line = "";

        try {
            while (in.ready()) {
                System.out.println("> " + in.readLine());
            }

            br = new BufferedReader(new InputStreamReader(System.in));

            // Read user input
            while (!(line = br.readLine()).equals("BYE")) {
                // Transmit it to server
                out.println(line);
                out.flush();

                // Print server response
                System.out.println("> " + in.readLine());

                System.out.println("Enter operation or BYE.");
            }
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE, "Connection problem in client used by {1}: {0}", new Object[]{e.getMessage()});
            connected = false;
        }
        finally {
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
    }
}
