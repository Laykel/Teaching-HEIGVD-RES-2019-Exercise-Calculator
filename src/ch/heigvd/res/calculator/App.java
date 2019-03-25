package ch.heigvd.res.calculator;

import ch.heigvd.res.calculator.server.Server;

public class App {
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");

        Server server = new Server(9998);
        server.serveClients();
    }
}
