package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.BadNameException;
import it.polimi.ingsw.server.serializable.Message;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerWaitingList {
    private final int numOfPlayers;
    private List<Socket> playersList;
    private List<String> namesList;
    private synchronized boolean isNameValid (String name){
        for (int i = 0; i < namesList.size(); i++)
            if (namesList.get(i).equals(name)) return false;
        return true;
    }
    public synchronized void addToPlayersList(Socket socket, String name) throws BadNameException {
        if (!isNameValid(name)) throw new BadNameException();
        System.out.println(name + " accepted for " + numOfPlayers + " players game");
        playersList.add(socket);
        namesList.add(name);
        List<Socket> exportedList = exportPlayersList();
        if (exportedList!=null) {
            List <String> tempNames = new ArrayList<>();
            for (int i = 0; i < numOfPlayers; i++) tempNames.add(namesList.remove(0));
            (new ServerThread(exportedList, this, numOfPlayers, tempNames)).start();
        }
    }
    public synchronized void importPlayersList(List <Socket> list, List <String> names){
        List <Socket> tempList = new ArrayList<>();
        tempList.addAll(list);
        tempList.addAll(playersList);
        playersList = tempList;
        List <String> tempNames = new ArrayList<>();
        tempNames.addAll(names);
        tempNames.addAll(namesList);
        namesList = tempNames;
    }
    private synchronized List <Socket> exportPlayersList(){
        if (playersList.size()<numOfPlayers) return null;
        List<Socket> list = new ArrayList<>();
        for (int i = 0; i < numOfPlayers; i++) list.add(playersList.remove(0));
        return list;
    }
    public ServerWaitingList(int numOfPlayers){
        playersList = new ArrayList<>();
        namesList = new ArrayList<>();
        this.numOfPlayers = numOfPlayers;
    }
}
