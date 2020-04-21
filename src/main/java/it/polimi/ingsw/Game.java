package it.polimi.ingsw;

import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.GodPowerManager;
import it.polimi.ingsw.server.ServerThread;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private int numOfPlayers;
    private Board board;
    private Turn turn;
    private ServerThread serverThread;
    private List<Player> players;
    private List<GodPower> godPowers;

    /**
     * This class creates a match
     * @param serverThread
     * @param numOfPlayers
     * @param playersNames
     * @throws IOException
     * @throws ParseException
     */
    public Game (ServerThread serverThread, int numOfPlayers, List<String> playersNames) throws IOException, ParseException {
        this.serverThread = serverThread;
        this.numOfPlayers = numOfPlayers;
        this.godPowers = GodPowerManager.createGodPowers(numOfPlayers);
        this.board = new Board();
        this.players = new ArrayList<>();
        for (int i = 0; i < playersNames.size(); i++)
            this.players.add(new Player (playersNames.get(i), i+1));
    }
}
