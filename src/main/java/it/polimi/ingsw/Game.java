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

    public void notifyJustUpdateAll(SerializableUpdate update) {
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).justUpdateAll(update);
    }

    public void notifyJustUpdateAll(List<SerializableUpdate> updates) {
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).justUpdateAll(updates);
    }

    public void notifyAnswerOnePlayer(SerializableRequest request) {
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).answerOnePlayer(request);
    }

    public void notifyUpdateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableRequest request){
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).updateAllAndAnswerOnePlayer(update, request);
    }

    public void notifyUpdateAllAndAnswerOnePlayer(List <SerializableUpdate> updates, SerializableRequest request) {
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).updateAllAndAnswerOnePlayer(updates, request);
    }


    /**
     * This class creates a match
     * @param numOfPlayers
     * @param playersNames
     */
    public Game (int numOfPlayers, List<String> playersNames) {
        this.numOfPlayers = numOfPlayers;
        this.godPowers = new ArrayList<>();
        this.board = new Board();
        this.players = new ArrayList<>();
        this.observerList = new ArrayList<>();
        for (int i = 0; i < playersNames.size(); i++)
            this.players.add(new Player (playersNames.get(i), i+1));
    }
}
