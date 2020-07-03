package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.controller.server.serializable.Message;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ServerThread extends Thread {
    private int numOfPlayers;
    private List<Socket> playersList;
    private List<String> playersNames;
    /**
     * Sends a Message object to one player.
     * @param message message
     * @param position list index of the player
     */
    public void sendMessage(String message, int position) {
        sendObject(new Message(message), position);
    }
    /**
     * Sends an object to one player.
     * @param object object
     * @param position list index of the player
     */
    public void sendObject(Object object, int position) {
        try {
            ObjectOutputStream fileObjectOut = new ObjectOutputStream(playersList.get(position).getOutputStream());
            fileObjectOut.writeObject(object);
            fileObjectOut.flush();
        } catch (Exception e){}
    }
    /**
     * Sends an object to all players.
     * @param object object
     */
    public void sendAllObject(Object object) {
        for (int i = 0; i < playersList.size(); i++){
            if (playersList.get(i)!=null) sendObject(object, i);
        }
    }
    /**
     * Communicates the ID number to each player, then creates
     * the MVC structure and starts clients listeners.
     */
    public void run(){
            for (int i = 0; i < numOfPlayers; i++) sendMessage("PLAYER_" + (i+1), i);
            ServerView serverView = new ServerView(this); // View
            Game game = new Game(numOfPlayers, playersNames); // Model
            Controller controller = new Controller(game, serverView); // Controller
            game.addObserver(serverView);
            serverView.addObserver(controller);
            serverView.startNewEventGenerators(playersList);
    }
    public ServerThread(List<Socket> playersList, int numOfPlayers, List <String> names){
        this.playersList = playersList;
        this.numOfPlayers = numOfPlayers;
        this.playersNames = names;
    }
}
