package it.polimi.ingsw;

import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.server.GameObserver;
import it.polimi.ingsw.server.serializable.*;

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
    public Worker getPlayerWorker (int playerId, int workerId) {
        return getBoard().getWorkerCell(getPlayer(playerId), workerId).getWorker();
    }
    public Position getWorkerPosition (int playerId, int workerId) {
        return getBoard().getWorkerCell(getPlayer(playerId), workerId).getPosition();
    }

    //This Method reflects the private implementation of players and godPowers list.
    private <T> T searchMethod (int playerId, List<T> list) {
       return list.get(correctIndex(playerId));
    }

    //Per convenzione interna i player ed i loro poteri si trovano in posizione 0,1,2 quindi per
    //trovare il relativo player conoscendo l'id basta fare -1. Quando un player viene eliminato, il suo riferimento viene posto a null
    //la dimensione delle liste non cambia.
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
