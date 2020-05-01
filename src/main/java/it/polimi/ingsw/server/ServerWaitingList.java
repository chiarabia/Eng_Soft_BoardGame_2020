package it.polimi.ingsw.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerWaitingList {
    private List<Socket> twoPlayersList;
    private List<Socket> threePlayersList;
    public boolean isTwoPlayersListFull(){
        return (twoPlayersList.size()>=2);
    }
    public boolean isThreePlayersListFull(){
        return (threePlayersList.size()>=3);
    }
    public void addToTwoPlayersList(Socket socket){
        twoPlayersList.add(socket);
    }
    public void addToThreePlayersList(Socket socket){
        threePlayersList.add(socket);
    }
    public List <Socket> exportTwoPlayersList(){
        List<Socket> list = new ArrayList();
        for (int i = 0; i < 2; i++){
            list.add(twoPlayersList.remove(0));
        }
        return list;
    }
    public List <Socket> exportThreePlayersList(){
        List<Socket> list = new ArrayList();
        for (int i = 0; i < 3; i++){
            list.add(threePlayersList.remove(0));
        }
        return list;
    }
    public synchronized void importTwoPlayersList(List <Socket> list){
        List <Socket> tempList = new ArrayList();
        tempList.addAll(list);
        tempList.addAll(twoPlayersList);
        twoPlayersList = tempList;
    }
    public synchronized void importThreePlayersList(List <Socket> list){
        List <Socket> tempList = new ArrayList();
        tempList.addAll(list);
        tempList.addAll(threePlayersList);
        threePlayersList = tempList;
    }
    public ServerWaitingList(){
        twoPlayersList = new ArrayList<>();
        threePlayersList = new ArrayList<>();
    }
}
