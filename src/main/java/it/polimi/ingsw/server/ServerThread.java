package it.polimi.ingsw.server;

import it.polimi.ingsw.GameController;
import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;
import it.polimi.ingsw.Game;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
    private int numOfPlayers;
    private List<Socket> playersList;
    private List<String> playersNames;
    private ServerWaitingList waitingList;
    private void returnListToWaitingList(){
        if (numOfPlayers == 2) waitingList.importTwoPlayersList(playersList);
        if (numOfPlayers == 3) waitingList.importThreePlayersList(playersList);
    }
    private List <String> scanList(String message) throws IOException {
        List <String> tempList = new ArrayList();
        for (int i = 0; i < playersList.size(); i++) {
            try{
                tempList.add(sendMessageAndWaitForReply(message, i, 1));
            } catch(ClientStoppedWorkingException e){
                tempList.add(null);
                playersList.remove(i);
                i--;
            }
        }
        return tempList;
    }
    public void sendMessage(String message, int player) throws IOException {
        PrintWriter out = new PrintWriter(playersList.get(player).getOutputStream());
        out.println(message);
        out.flush();
    }
    public String sendMessageAndWaitForReply(String message, int player, int timeLimit) throws IOException, ClientStoppedWorkingException {
        sendMessage(message, player);
        return ServerReciever.receiveMessage(playersList.get(player), timeLimit);
    }
    public void sendObject(Object object, int player) throws IOException {
        ObjectOutputStream fileObjectOut = new ObjectOutputStream(playersList.get(player).getOutputStream());
        fileObjectOut.writeObject(object);
        fileObjectOut.flush();
    }
    public void sendAllObject(Object object) throws IOException {
        for (int i = 0; i < playersList.size(); i++){
            if (playersList.get(i)!=null) sendObject(object, i);
        }
    }
    public Object sendObjectAndWaitForReply(Object object, int player, int timeLimit) throws IOException, ClientStoppedWorkingException {
        sendObject(object, player);
        return ServerReciever.receiveObject(playersList.get(player), timeLimit);
    }
    public void startGame(){
        try {
            for (int i = 0; i < numOfPlayers; i++) sendMessage("Server is ready", i);
            playersNames = scanList("Player's name");
            if (playersList.size() < numOfPlayers){
                scanList ("Close");
                return;
            }
            ServerProxy serverProxy = new ServerProxy(this);
            Game game = new Game(numOfPlayers, playersNames);
            GameController gameController = new GameController(game);
            game.addObserver(serverProxy);
            serverProxy.addObserver(gameController);

            // Here the game begins //

        }catch(IOException | ParseException e){}
    }
    public void run(){
        try{
            scanList("Hello");
        } catch(IOException e){return;}
        if (playersList.size() < numOfPlayers) returnListToWaitingList();
        else startGame();
    }
    public ServerThread(List<Socket> playersList, ServerWaitingList waitingList, int numOfPlayers){
        this.playersList = playersList;
        this.waitingList = waitingList;
        this.numOfPlayers = numOfPlayers;
    }
}
