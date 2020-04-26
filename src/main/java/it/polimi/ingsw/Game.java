package it.polimi.ingsw;

import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.GodPowerManager;
import it.polimi.ingsw.server.GameObserver;
import it.polimi.ingsw.server.serializable.*;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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


    // In questo metodo il ciclo MVC viene rotto, quindi notifyUpdateAll
    // deve servire SOLO per comunicare il termine della partita (vittoria o disconnessione player)
    public void notifyUpdateAll(SerializableUpdate update) throws IOException {
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).updateAll(update);
    }

    public void notifyAnswerOnePlayer(SerializableAnswer answer) throws IOException {
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).answerOnePlayer(answer);
    }

    public void notifyUpdateAllAndAnswerOnePlayer(SerializableUpdate update, SerializableAnswer answer) throws IOException {
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).updateAllAndAnswerOnePlayer(update, answer);
    }

    public void notifyUpdateAllTwiceAndAnswerOnePlayer(SerializableUpdate update1, SerializableUpdate update2, SerializableAnswer answer) throws IOException {
        for (int i = 0; i < observerList.size(); i++) observerList.get(i).updateAllTwiceAndAnswerOnePlayer(update1, update2, answer);
    }

    public void initializeInfos() throws IOException{
        List<String> playersNames = new ArrayList<>();
        List<String> godPowersNames = new ArrayList<>();
        for (Player player: players) playersNames.add(player.getName());
        for (GodPower godPower: godPowers) godPowersNames.add(godPower.getGodName());
        SerializableUpdateInitializeInfos update = new SerializableUpdateInitializeInfos(playersNames, godPowersNames);
        SerializableRequest request = new SerializableRequestInitializeWorkers(1);
        notifyUpdateAllAndAnswerOnePlayer(update, request);
    }

    public void initializeWorkers(int playerId, List<Position> workerPositions) throws IOException {
        Worker worker1 = new Worker(players.get(playerId-1), 1);
        Worker worker2 = new Worker(players.get(playerId-1), 2);
        players.get(playerId-1).addWorker(worker1);
        players.get(playerId-1).addWorker(worker2);
        Cell worker1Cell = board.getCell(workerPositions.get(0));
        Cell worker2Cell = board.getCell(workerPositions.get(1));
        worker1Cell.setWorker(worker1);
        worker2Cell.setWorker(worker2);
        SerializableUpdateInitializeWorkers update = new SerializableUpdateInitializeWorkers(workerPositions, playerId);
        if (playerId == players.size()){
            // tutti i worker sono pronti, il primo turno ha inizio
            SerializableUpdateTurn updateTurn = new SerializableUpdateTurn(1);
            notifyUpdateAllTwiceAndAnswerOnePlayer(update, updateTurn, new SerializableRequestReady(1));
            // il segnale di Ready serve solo a portare il controller allo stato di default onReady()
        } else {
            SerializableRequest request = new SerializableRequestInitializeWorkers(playerId + 1);
            notifyUpdateAllAndAnswerOnePlayer(update, request);
        }
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
        this.godPowers = GodPowerManager.createGodPowers(numOfPlayers);
        this.board = new Board();
        this.players = new ArrayList<>();
        this.observerList = new ArrayList<>();
        for (int i = 0; i < playersNames.size(); i++)
            this.players.add(new Player (playersNames.get(i), i+1));
    }
}
