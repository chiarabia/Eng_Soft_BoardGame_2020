package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;


public class Server {
    public static int serverPort;
    public void startServer() throws InterruptedException {
        System.out.println("Server started");
        ServerSocket serverSocket;
        ServerAccepter playersAccepter;
        while (true) {
            try {
                serverSocket = new ServerSocket(serverPort);
                playersAccepter = new ServerAccepter(serverSocket);
                playersAccepter.start();
                break;
            } catch (IOException e) {
                System.out.println("An error occurred, restarting in 10 seconds...");
                sleep(10000);
            }
        }
    }
    public void startServer(int port) throws InterruptedException {
        serverPort = port;
        startServer();
    }
}
