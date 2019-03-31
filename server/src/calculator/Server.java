package calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
	final static Logger LOG = Logger.getLogger(Server.class.getName());

    int port;

	/**
	 * Constructor
	 * @param port the port to listen on
	 */
	public Server(int port) {
		this.port = port;
	}

	public void serveClients() {
		ServerSocket serverSocket;
		Socket clientSocket = null;
		BufferedReader in = null;
		PrintWriter out = null;

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, null, ex);
			return;
		}

		while (true) {
			try {
				LOG.log(Level.INFO, "Waiting (blocking) for a new client on port {0}", port);

				clientSocket = serverSocket.accept();
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				out = new PrintWriter(clientSocket.getOutputStream());
				String line;
				boolean shouldRun = true;

				out.println("Welcome to the Calculator Server.\n" +
                            "Send ADD, SUB, MUL OR DIV followed by operand1 and operand2.\n" +
							"Example: ADD 4 5 -> 9\n" +
							"Conclude with BYE.");
				out.flush();

				LOG.info("Reading until client sends BYE or closes the connection...");
				while ((shouldRun) && (line = in.readLine()) != null) {
					String[] tokens = line.split(" ");

					int rhs = Integer.parseInt(tokens[1]);
					int lhs = Integer.parseInt(tokens[2]);

					switch (tokens[0]) {
						case Protocol.CMD_ADD:
							out.println(rhs + lhs);
							break;
						case Protocol.CMD_SUB:
							out.println(rhs - lhs);
							break;
						case Protocol.CMD_DIV:
							out.println(rhs / lhs);
							break;
						case Protocol.CMD_MUL:
							out.println(rhs * lhs);
							break;
						case Protocol.CMD_QUIT:
							shouldRun = false;
							break;
						default :
							out.println("Incorrect input format");
							break;
					}
					out.flush();
				}

				LOG.info("Cleaning up resources...");
				clientSocket.close();
				in.close();
				out.close();

			} catch (IOException ex) {
				if (in != null) {
					try {
						in.close();
					} catch (IOException ex1) {
						LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
					}
				}
				if (out != null) {
					out.close();
				}
				if (clientSocket != null) {
					try {
						clientSocket.close();
					} catch (IOException ex1) {
						LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
					}
				}
				LOG.log(Level.SEVERE, ex.getMessage(), ex);
			} catch (NumberFormatException e) {
				LOG.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
}
