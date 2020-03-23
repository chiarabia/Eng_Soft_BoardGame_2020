package it.polimi.ingsw.Server;

import java.io.IOException;
import java.net.ServerSocket;


public class Server {
    public final static int twoPlayersPort = 555;
    public final static int threePlayersPort = 556;
    public static void startServer(){
        try {
            ServerWaitingList waitingList = new ServerWaitingList();
            ServerSocket twoPlayersServerSocket = new ServerSocket(twoPlayersPort);
            ServerSocket threePlayersServerSocket = new ServerSocket(threePlayersPort);
            (new ServerAccepter(twoPlayersServerSocket, waitingList, 2)).start();
            (new ServerAccepter(threePlayersServerSocket, waitingList, 3)).start();
        } catch (IOException e){}
    }
}
