package it.polimi.ingsw;

import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.GodPowerManager;
import it.polimi.ingsw.server.GameObserver;
import it.polimi.ingsw.server.ProxyObserver;
import it.polimi.ingsw.server.ServerThread;
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

    public void addObserver(GameObserver observer){
        observerList.add(observer);
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
