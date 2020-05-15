package it.polimi.ingsw.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerWaitingList {
    private final int numOfPlayers;
    private List<Socket> playersList;
    public synchronized void addToPlayersList(Socket socket){
        playersList.add(socket);
    }
    public synchronized void importPlayersList(List <Socket> list){
        List <Socket> tempList = new ArrayList();
        tempList.addAll(list);
        tempList.addAll(playersList);
        playersList = tempList;
    }
    public synchronized List <Socket> exportPlayersList(){
        if (playersList.size()<numOfPlayers) return null;
        List<Socket> list = new ArrayList();
        for (int i = 0; i < numOfPlayers; i++)
            list.add(playersList.remove(0));
        return list;
    }
    public ServerWaitingList(int numOfPlayers){
        playersList = new ArrayList<>();
        this.numOfPlayers = numOfPlayers;
    }
}
