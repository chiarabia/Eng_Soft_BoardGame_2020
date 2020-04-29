package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;


public class Server {
    public final static int twoPlayersPort = 555;
    public final static int threePlayersPort = 556;
    public static void startServer() throws InterruptedException {
        ServerWaitingList waitingList = new ServerWaitingList();
        ServerSocket twoPlayersServerSocket;
        ServerSocket threePlayersServerSocket;
        ServerAccepter twoPlayersAccepter;
        ServerAccepter threePlayersAccepter;
        while (true) {
            try {
                twoPlayersServerSocket = new ServerSocket(twoPlayersPort);
                threePlayersServerSocket = new ServerSocket(threePlayersPort);
                twoPlayersAccepter = new ServerAccepter(twoPlayersServerSocket, waitingList, 2, true);
                threePlayersAccepter = new ServerAccepter(threePlayersServerSocket, waitingList, 3);
                twoPlayersAccepter.start();
                threePlayersAccepter.start();
                break;
            } catch (IOException e) { TimeUnit.SECONDS.sleep(10); }
        }
    }
}
