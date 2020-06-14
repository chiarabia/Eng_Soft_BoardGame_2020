package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.BadNameException;
import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;
import it.polimi.ingsw.server.serializable.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerWaitingList {
    private final int numOfPlayers;
    private List<Socket> playersList;
    private List<String> namesList;
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
    private synchronized List <Socket> exportPlayersList() {
        if (playersList.size()<numOfPlayers) return null;
        List<Socket> list = new ArrayList<>();
        for (int j = 0; j < numOfPlayers; j++) list.add(playersList.remove(0));
        return list;
    }
    private synchronized String sendMessageAndWaitForReply(String message, int position) throws ClientStoppedWorkingException {
        try {
            ObjectOutputStream fileObjectOut = new ObjectOutputStream(playersList.get(position).getOutputStream());
            fileObjectOut.writeObject(new Message(message));
            fileObjectOut.flush();
        } catch (Exception e){}
        return ((Message)(new ServerSyncReceiver()).receiveObject(playersList.get(position))).getMessage();
    }
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
