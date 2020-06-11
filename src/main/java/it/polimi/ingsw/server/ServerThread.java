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
    private void echoRequest() throws IOException {
        for (int i = 0; i < playersList.size(); i++) {
            try{
                String reply = sendMessageAndWaitForReply("Hello", i);
                if (!reply.equals("Hello")) throw new Exception();
            } catch(Exception e){
                playersList.remove(i).close();
                System.out.println(playersNames.remove(i) + " disconnected");
                i--;
            }
        }
    }
    public void sendMessage(String message, int position) {
        sendObject(new Message(message), position);
    }
    public String sendMessageAndWaitForReply(String message, int position) throws ClientStoppedWorkingException {
        sendMessage(message, position);
        return ((Message)(new ServerSyncReceiver()).receiveObject(playersList.get(position))).getMessage();
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
            echoRequest();
            if (playersList.size() < numOfPlayers) {
                waitingList.importPlayersList(playersList, playersNames);
                System.out.println("Game ended");
                return;
            }
            for (int i = 0; i < numOfPlayers; i++) sendMessage("You are player " + (i+1), i);
            ServerView serverView = new ServerView(this); // View
            Game game = new Game(numOfPlayers, playersNames); // Model
            Controller controller = new Controller(game, serverView); // Controller
            game.addObserver(serverView);
            serverView.addObserver(controller);
            serverView.startNewEventGenerators(playersList);
        }catch(Exception e){}
    }
    public ServerThread(List<Socket> playersList, ServerWaitingList waitingList, int numOfPlayers, List <String> names){
        this.playersList = playersList;
        this.waitingList = waitingList;
        this.numOfPlayers = numOfPlayers;
        this.playersNames = names;
    }
}
