package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.BadNameException;
import it.polimi.ingsw.server.serializable.Message;
import it.polimi.ingsw.server.serializable.Serializable;
import it.polimi.ingsw.server.serializable.SerializableConnection;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
                        SerializableConnection connection = (SerializableConnection) (new ServerSyncReceiver()).receiveObject(socket);
                        int numOfPlayers = connection.getNumOfPlayers();
                        String name = connection.getName();
                        if (numOfPlayers == 2) waitingList = twoPlayersWaitingList;
                        else if (numOfPlayers == 3) waitingList = threePlayersWaitingList;
                        else {
                            socket.close();
                            return;
                        }
                        waitingList.addToPlayersList(socket, name);
                    } catch (BadNameException e){
                        try {
                            ObjectOutputStream fileObjectOut = new ObjectOutputStream(socket.getOutputStream());
                            fileObjectOut.writeObject(new Message("ERROR_NOT_VALID_NAME"));
                            fileObjectOut.flush();
                            socket.close();
                        } catch (Exception ex) {}
                    } catch (Exception e) {}
                })).start();
            }
        } catch (IOException e) {
            try { (new Server()).startServer(serverSocket.getLocalPort()); } catch (InterruptedException ex) {}
        }
    }
    public ServerAccepter(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
}
