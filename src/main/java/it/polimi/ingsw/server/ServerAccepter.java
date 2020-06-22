package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.BadNameException;
import it.polimi.ingsw.server.serializable.Message;
import it.polimi.ingsw.server.serializable.Serializable;
import it.polimi.ingsw.server.serializable.SerializableConnection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerAccepter extends Thread {
    private ServerSocket serverSocket;
    private List <ServerWaitingList> waitingLists;
    /**
     * This method accepts incoming connections, checking if client datas are correct
     * If datas are correct it adds the player to a waiting list, otherwise it reports the error
     */
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                (new Thread (()->{
                    try {
                        SerializableConnection connection = (SerializableConnection) (new ServerSyncReceiver()).receiveObject(socket);
                        int numOfPlayers = connection.getNumOfPlayers();
                        String name = connection.getName();
                        if (numOfPlayers < 2 || numOfPlayers > waitingLists.size()+1){
                            sendError(socket, "ERROR_NOT_VALID_NUM_OF_PLAYERS");
                            return;
                        }
                        waitingLists.get(numOfPlayers-2).addToPlayersList(socket, name);
                    } catch (BadNameException e){
                        try {
                            sendError(socket, "ERROR_NOT_VALID_NAME");
                        } catch (Exception ex) {}
                    } catch (Exception e) {
                        try { socket.close(); } catch (Exception ex) {}
                    }
                })).start();
            }
        } catch (IOException e) {
            try { (new Server()).startServer(serverSocket.getLocalPort()); } catch (InterruptedException ex) {}
        }
    }
    /**
     * This method sends to a client an error message and then closes the connection
     * @param socket socket
     * @param message error message
     * @throws IOException IOException
     */
    private void sendError (Socket socket, String message) throws IOException {
        ObjectOutputStream fileObjectOut = new ObjectOutputStream(socket.getOutputStream());
        fileObjectOut.writeObject(new Message(message));
        fileObjectOut.flush();
        socket.close();
    }
    public ServerAccepter(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
        this.waitingLists = new ArrayList<>();
        for (int i = 2; i <= 3; i++) waitingLists.add(new ServerWaitingList(i));
    }
}
