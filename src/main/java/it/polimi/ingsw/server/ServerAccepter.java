package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerAccepter extends Thread {
    private ServerSocket serverSocket;
    private ServerWaitingList waitingList;
    private int numOfPlayers;
    public void run() {
        try {
            Socket socket;
            while (true) {
                socket = serverSocket.accept();
                if (numOfPlayers == 2) waitingList.addToTwoPlayersList(socket);
                if (numOfPlayers == 3) waitingList.addToThreePlayersList(socket);
                if (numOfPlayers == 2 && waitingList.isTwoPlayersListFull())
                    (new ServerThread(waitingList.exportTwoPlayersList(), waitingList, 2)).start();
                if (numOfPlayers == 3 && waitingList.isThreePlayersListFull())
                    (new ServerThread(waitingList.exportThreePlayersList(), waitingList, 3)).start();
            }
        }catch(IOException e){}
    }
    public ServerAccepter(ServerSocket serverSocket, ServerWaitingList waitingList, int numOfPlayers){
        this.numOfPlayers = numOfPlayers;
        this.serverSocket = serverSocket;
        this.waitingList = waitingList;
    }
}
