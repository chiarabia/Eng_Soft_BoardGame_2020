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
    private ServerThread serverThread;
    private List<Player> players;
    private List<GodPower> godPowers;
    public Game (ServerThread serverThread, int numOfPlayers, List<String> playersNames) throws IOException, ParseException {
        this.serverThread = serverThread;
        this.numOfPlayers = numOfPlayers;
        this.godPowers = GodPowerManager.createGodPowers(numOfPlayers);
        this.players = new ArrayList<>();
        for (int i = 0; i < playersNames.size(); i++)
            this.players.add(new Player (playersNames.get(i), i+1));
    }
}
