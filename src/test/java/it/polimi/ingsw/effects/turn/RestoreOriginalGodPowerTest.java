package it.polimi.ingsw.effects.turn;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Turn;
import it.polimi.ingsw.Worker;
import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.GodPowerManager;
import it.polimi.ingsw.effects.build.NotOnSamePosition;
import it.polimi.ingsw.effects.build.StandardBuild;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

public class RestoreOriginalGodPowerTest {
    RestoreOriginalGodPower restoreOriginalGodPower;
    GodPowerManager godPowerManager = new GodPowerManager();
    GodPower athenaGodPower;
    GodPower oppositeGodPower;
    Player player1 = new Player("pippo", 1);
    Player player2 = new Player("pluto",2);
    Worker worker1 = new Worker(player1, 1);
    Turn currentTurn = new Turn(player1);
    List<GodPower> godList = new ArrayList<>();
    Board board = new Board();

    @BeforeEach
    void setUp() throws IOException, ParseException {
        godList.add(GodPowerManager.power("AthenaCard.json", 1));
        godList.add(GodPowerManager.power("DemeterCard.json", 2));
    }

    @Test
    void OriginalDemeterGodPowerShouldBeRestored() {
        currentTurn = new Turn(player2);
        assert godList.get(0).getPlayerId()==1;
        assert godList.get(1).getPlayerId()==2;

        restoreOriginalGodPower = new RestoreOriginalGodPower(godList.get(1).copyGodPower(godList.get(1)));
        godList.get(1).setBuild(null); //random modification
        restoreOriginalGodPower.endTurn(currentTurn, godList, player2);

        assertAll("OriginalGod Restored", () -> assertEquals(godList.get(1).getBuild().getClass(), new NotOnSamePosition(2).getClass()));


    }
}
