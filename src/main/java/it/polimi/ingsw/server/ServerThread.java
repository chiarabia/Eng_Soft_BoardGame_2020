package it.polimi.ingsw.server;

import it.polimi.ingsw.Controller;
import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;
import it.polimi.ingsw.Game;
import it.polimi.ingsw.server.serializable.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
    private int numOfPlayers;
    private List<Socket> playersList;
    private List<String> playersNames;
    private ServerWaitingList waitingList;
    private List <String> scanList(String message) throws IOException {
        List <String> tempList = new ArrayList();
        for (int i = 0; i < playersList.size(); i++) {
            try{
                tempList.add(sendMessageAndWaitForReply(message, i, 1));
            } catch(Exception e){
                tempList.add(null);
                playersList.remove(i);
                i--;
            }
        }
        return tempList;
    }
    public void sendMessage(String message, int position) {
        sendObject(new Message(message), position);
    }
    public String sendMessageAndWaitForReply(String message, int position, int timeLimit) throws ClientStoppedWorkingException {
        sendMessage(message, position);
        return ((Message)(new ServerSyncReceiver()).receiveObject(playersList.get(position), timeLimit)).getMessage();
    }
    public void sendObject(Object object, int position) {
        try {
            ObjectOutputStream fileObjectOut = new ObjectOutputStream(playersList.get(position).getOutputStream());
            fileObjectOut.writeObject(object);
            fileObjectOut.flush();
        } catch (Exception e){}
    }
    public void sendAllObject(Object object) {
        for (int i = 0; i < playersList.size(); i++){
            if (playersList.get(i)!=null) sendObject(object, i);
        }
    }
    public void run(){
        try {
            playersNames = scanList("Player's name");
            if (playersList.size() < numOfPlayers) {
                waitingList.importPlayersList(playersList);
                return;
            }
            for (int i = 0; i < numOfPlayers; i++) sendMessage("You are player " + (i+1), i);
            ServerView serverView = new ServerView(this, playersList); // View
            Game game = new Game(numOfPlayers, playersNames); // Model
            Controller gameController = new Controller(game, serverView); // Controller
            game.addObserver(serverView);
            serverView.addObserver(gameController);
            serverView.start();
        }catch(Exception e){}
    }
    public ServerThread(List<Socket> playersList, ServerWaitingList waitingList, int numOfPlayers){
        this.playersList = playersList;
        this.waitingList = waitingList;
        this.numOfPlayers = numOfPlayers;
    }
}
