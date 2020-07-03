package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.server.serializable.SerializableRequest;
import it.polimi.ingsw.controller.server.serializable.SerializableUpdate;
import it.polimi.ingsw.model.effects.GodPower;
import it.polimi.ingsw.controller.server.GameObserver;

import java.util.ArrayList;
import java.util.List;


public class Game {
    private final int numOfPlayers;
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
    public Player getPlayer(int playerId) {
        return searchMethod(playerId, getPlayers());
    }
    public GodPower getPlayerGodPower(int playerId) {
        return searchMethod(playerId, getGodPowers());
    }
    public Worker getPlayerWorker (int playerId, int workerId) { return getBoard().getWorkerCell(getPlayer(playerId), workerId).getWorker(); }
    public Position getWorkerPosition (int playerId, int workerId) { return getBoard().getWorkerCell(getPlayer(playerId), workerId).getPosition(); }

    /**
     * Reflects the private implementation of players and godPowers lists.
     * @param playerId playerId
     * @param list list
     * @return T
     */
    private <T> T searchMethod (int playerId, List<T> list) {
       return list.get(correctIndex(playerId));
    }

    /**
     * Converts a playerId into a list index
     * @param playerId playerId
     * @return int
     */
    private int correctIndex (int playerId) {
        return playerId-1;
    }

    public void setBoard(Board board) {this.board = board; }
    public void setTurn(Turn turn) {this.turn = turn;}
    public void setPlayers(List<Player> players) {this.players = players;}
    public void setGodPowers(List<GodPower> godPowers) {this.godPowers = godPowers;}
    public void removePlayer (int playerId) {
        getPlayers().set(correctIndex(playerId), null);
    }
    public void removeGodPower (int playerId) {
        getGodPowers().set(correctIndex(playerId), null);
    }
    public void addObserver(GameObserver observer){observerList.add(observer);}

    public void notifyJustUpdateAll(SerializableUpdate update) {
        for (GameObserver gameObserver : observerList) gameObserver.justUpdateAll(update);
    }

    public void notifyJustUpdateAll(List<SerializableUpdate> updates) {
        for (GameObserver gameObserver : observerList) gameObserver.justUpdateAll(updates);
    }

    public void notifyAnswerOnePlayer(SerializableRequest request) {
        for (GameObserver gameObserver : observerList) gameObserver.answerOnePlayer(request);
    }

    public void notifyUpdateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableRequest request){
        for (GameObserver gameObserver : observerList) gameObserver.updateAllAndAnswerOnePlayer(update, request);
    }


    /**
     * Creates a match
     * @param numOfPlayers number of players of the match
     * @param playersNames List of players' names
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
