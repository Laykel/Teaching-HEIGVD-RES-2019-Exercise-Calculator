package ch.heigvd.res.calculator.server;

public class Server {
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

				out.println("Welcome to the Single-Threaded Server.\nSend me text lines and conclude with the BYE command.");
				out.flush();
				LOG.info("Reading until client sends BYE or closes the connection...");
				while ( (shouldRun) && (line = in.readLine()) != null ) {
					if (line.equalsIgnoreCase("bye")) {
						shouldRun = false;
					}
					out.println("> " + line.toUpperCase());
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
			}
		}
	}
}
