package it.polimi.ingsw.server;

import it.polimi.ingsw.server.serializable.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerAccepter extends Thread {
    private ServerSocket serverSocket;
    public void run() {
        ServerWaitingList twoPlayersWaitingList = new ServerWaitingList(2);
        ServerWaitingList threePlayersWaitingList = new ServerWaitingList(3);
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                (new Thread (()->{
                    try {
                        ServerWaitingList waitingList;
                        Message message = (Message) ServerReciever.receiveObject(socket, 1);
                        int numOfPlayers = Character.getNumericValue(message.getMessage().charAt(0));
                        if (numOfPlayers == 2) waitingList = twoPlayersWaitingList;
                        else waitingList = threePlayersWaitingList;
                        waitingList.addToPlayersList(socket);
                        List<Socket> exportedList = waitingList.exportPlayersList();
                        if (exportedList!=null) (new ServerThread(exportedList, waitingList, numOfPlayers)).start();
                    } catch (Exception e) {}
                })).start();
            }
        } catch (IOException e) {
            try { Server.startServer(); } catch (InterruptedException ex) {}
        }
    }
    public ServerAccepter(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
}
