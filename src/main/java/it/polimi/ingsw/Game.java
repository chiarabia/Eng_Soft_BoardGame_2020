package it.polimi.ingsw;

import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.GodPowerManager;
import it.polimi.ingsw.exceptions.ClientStoppedWorkingException;
import it.polimi.ingsw.server.GameObserver;
import it.polimi.ingsw.server.serializable.*;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Game {
    private int numOfPlayers;
    private Board board;
    private Turn turn;
    private List<Player> players;
    private List<GodPower> godPowers;
    private List<GameObserver> observerList;

    public int getNumOfPlayers() {return numOfPlayers;}
    public Board getBoard() { return board; }
    public Turn getTurn() { return turn; }
    public List<Player> getPlayers() { return players; }
    public List<GodPower> getGodPowers() { return godPowers; }

    public void setNumOfPlayers(int numOfPlayers) {this.numOfPlayers = numOfPlayers;}
    public void setBoard(Board board) {this.board = board; }
    public void setTurn(Turn turn) {this.turn = turn;}
    public void setPlayers(List<Player> players) {this.players = players;}
    public void setGodPowers(List<GodPower> godPowers) {this.godPowers = godPowers;}

    public void addObserver(GameObserver observer){observerList.add(observer);}


    // In questo metodo il ciclo MVC viene rotto, quindi notifyJustUpdateAll
    // deve servire SOLO per comunicare il termine della partita (vittoria o disconnessione player)
    public void notifyJustUpdateAll(SerializableUpdate update) throws IOException {
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).justUpdateAll(update);
    }

    public void notifyJustUpdateAll(List<SerializableUpdate> updates) throws IOException {
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).justUpdateAll(updates);
    }

    public void notifyAnswerOnePlayer(SerializableRequest request) throws IOException {
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).answerOnePlayer(request);
    }

    public void notifyUpdateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableRequest request) throws IOException{
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).updateAllAndAnswerOnePlayer(update, request);
    }

    public void notifyUpdateAllAndAnswerOnePlayer(List <SerializableUpdate> updates, SerializableRequest request) throws IOException{
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).updateAllAndAnswerOnePlayer(updates, request);
    }


    /**
     * This class creates a match
     * @param numOfPlayers
     * @param playersNames
     * @throws IOException
     * @throws ParseException
     */
    public Game (int numOfPlayers, List<String> playersNames) throws IOException, ParseException {
        this.numOfPlayers = numOfPlayers;
        this.godPowers = new ArrayList<>();
        this.board = new Board();
        this.players = new ArrayList<>();
        this.observerList = new ArrayList<>();
        for (int i = 0; i < playersNames.size(); i++)
            this.players.add(new Player (playersNames.get(i), i+1));
    }
}
