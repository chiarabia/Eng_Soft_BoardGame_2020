package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;


public class Server {
    public static int serverPort;
    public static void startServer() throws InterruptedException {
        ServerSocket serverSocket;
        ServerAccepter playersAccepter;
        while (true) {
            try {
                serverSocket = new ServerSocket(serverPort);
                playersAccepter = new ServerAccepter(serverSocket);
                playersAccepter.start();
                break;
            } catch (IOException e) { sleep(10000); }
        }
    }
    public static void startServer(int port) throws InterruptedException {
        serverPort = port;
        startServer();
    }
}
