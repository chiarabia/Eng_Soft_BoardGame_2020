package it.polimi.ingsw.Server;

import it.polimi.ingsw.Exceptions.ClientStoppedWorkingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ServerThread extends Thread {
    private int numOfPlayers;
    private List<Socket> playersList;
    private ServerWaitingList waitingList;
    private void returnListToWaitingList(){
        if (numOfPlayers == 2) waitingList.importTwoPlayersList(playersList);
        if (numOfPlayers == 3) waitingList.importThreePlayersList(playersList);
    }
    private void scanList(String message) throws IOException {
        for (int i = 0; i < playersList.size(); i++) {
            try{
                sendAndWaitForReply(message, i, 1);
            } catch(ClientStoppedWorkingException e){
                playersList.remove(i);
                i--;
            }
        }
    }
    public void sendMessage(String message, int player) throws IOException {
        PrintWriter out = new PrintWriter(playersList.get(player).getOutputStream());
        out.println(message);
        out.flush();
    }
    public String sendAndWaitForReply(String message, int player, int timeLimit) throws IOException, ClientStoppedWorkingException {
        sendMessage(message, player);
        return ServerReciever.recieve(playersList.get(player), timeLimit);
    }
    public void initializeGame(){
        try {
            for (int i = 0; i < numOfPlayers; i++) sendMessage("Server is ready", i);
        }catch(IOException e){return;}

        // Here the game begins //

    }
    public void run(){
        try{
            scanList("Hello");
        } catch(IOException e){return;}
        if (playersList.size() < numOfPlayers) returnListToWaitingList();
        else initializeGame();
    }
    public ServerThread(List<Socket> playersList, ServerWaitingList waitingList, int numOfPlayers){
        this.playersList = playersList;
        this.waitingList = waitingList;
        this.numOfPlayers = numOfPlayers;
    }
}
