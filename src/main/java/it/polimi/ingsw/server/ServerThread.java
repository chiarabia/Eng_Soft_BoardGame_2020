package it.polimi.ingsw.server;

import it.polimi.ingsw.Controller;
import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;
import it.polimi.ingsw.Game;
import it.polimi.ingsw.server.serializable.Message;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
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
    public void sendMessage(String message, int position) throws IOException {
        sendObject(new Message(message), position);
    }
    public String sendMessageAndWaitForReply(String message, int position, int timeLimit) throws IOException, ClientStoppedWorkingException {
        sendMessage(message, position);
        return ((Message)ServerReciever.receiveObject(playersList.get(position), timeLimit)).getMessage();
    }
    public void sendObject(Object object, int position) {
        try {
            ObjectOutputStream fileObjectOut = new ObjectOutputStream(playersList.get(position).getOutputStream());
            fileObjectOut.writeObject(object);
            fileObjectOut.flush();
        } catch (Exception e){}
    }
    public void sendAllObject(Object object) throws IOException {
        for (int i = 0; i < playersList.size(); i++){
            if (playersList.get(i)!=null) sendObject(object, i);
        }
    }
    public Object sendObjectAndWaitForReply(Object object, int position, int timeLimit) throws ClientStoppedWorkingException {
        sendObject(object, position);
        return ServerReciever.receiveObject(playersList.get(position), timeLimit);
    }
    public void run(){
        try {
            playersNames = scanList("Player's name");
            if (playersList.size() < numOfPlayers) {
                waitingList.importPlayersList(playersList);
                return;
            }
            for (int i = 0; i < numOfPlayers; i++) sendMessage("You are player " + (i+1), i);
            ServerProxy serverProxy = new ServerProxy(this);
            Game game = new Game(numOfPlayers, playersNames);
            Controller gameController = new Controller(game);
            game.addObserver(serverProxy);
            serverProxy.addObserver(gameController);
            gameController.onInitialization();
        }catch(Exception e){}
    }
    public ServerThread(List<Socket> playersList, ServerWaitingList waitingList, int numOfPlayers){
        this.playersList = playersList;
        this.waitingList = waitingList;
        this.numOfPlayers = numOfPlayers;
    }
}
