package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.exceptions.BadNameException;
import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;
import it.polimi.ingsw.controller.server.serializable.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerWaitingList {
    private final int numOfPlayers;
    private List<Socket> playersList;
    private List<String> namesList;
    /**
     * Checks if the player's name is valid, then adds it to the
     * players list and eventually extracts 2 or 3 players in order to start a match
     * @param socket socket
     * @param name player's name
     * @throws BadNameException BadNameException
     * @throws IOException IOException
     */
    public synchronized void addToPlayersList(Socket socket, String name) throws BadNameException, IOException {
        if (!isNameValid(name)) throw new BadNameException();
        System.out.println(name + " accepted for " + numOfPlayers + " players game");
        playersList.add(socket);
        namesList.add(name);
        List<Socket> exportedList = exportPlayersList();
        if (exportedList!=null) {
            List <String> tempNames = new ArrayList<>();
            for (int i = 0; i < numOfPlayers; i++) tempNames.add(namesList.remove(0));
            (new ServerThread(exportedList, numOfPlayers, tempNames)).start();
        }
    }
    /**
     * Exports 2 or 3 players from the waiting list if available
     * @return <code>List&lt;Socket&gt;</code> or null if there are not enough players
     */
    private synchronized List <Socket> exportPlayersList() {
        if (playersList.size()<numOfPlayers) return null;
        List<Socket> list = new ArrayList<>();
        for (int j = 0; j < numOfPlayers; j++) list.add(playersList.remove(0));
        return list;
    }
    /**
     * Sends a Message object and waits for the answer
     * @param message message
     * @param position position of socket inside the list
     * @throws ClientStoppedWorkingException if client is not connected anymore or if no response is received within a second
     */
    private synchronized String sendMessageAndWaitForReply(String message, int position) throws ClientStoppedWorkingException {
        try {
            ObjectOutputStream fileObjectOut = new ObjectOutputStream(playersList.get(position).getOutputStream());
            fileObjectOut.writeObject(new Message(message));
            fileObjectOut.flush();
        } catch (Exception e){}
        return ((Message)(new ServerSyncReceiver()).receiveObject(playersList.get(position))).getMessage();
    }
    /**
     * Discards the no more valid clients by sending an echo message, then
     * checks if a name is already used by another player.
     * @param name player's name
     * @throws IOException IOException
     */
    private synchronized boolean isNameValid (String name) throws IOException {
        for (int i = 0; i < playersList.size(); i++) {
            try{
                String reply = sendMessageAndWaitForReply("HELLO", i);
                if (!reply.equals("HELLO")) throw new Exception();
            } catch(Exception e){
                playersList.remove(i).close();
                System.out.println(namesList.remove(i) + " disconnected");
                i--;
            }
        }
        for (int i = 0; i < namesList.size(); i++)
            if (namesList.get(i).equals(name)) return false;
        return true;
    }
    public ServerWaitingList(int numOfPlayers){
        playersList = new ArrayList<>();
        namesList = new ArrayList<>();
        this.numOfPlayers = numOfPlayers;
    }
}
