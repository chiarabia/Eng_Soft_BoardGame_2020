package it.polimi.ingsw.model.effects.turn;

import it.polimi.ingsw.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.effects.GodPower;
import it.polimi.ingsw.model.effects.GodPowerManager;
import it.polimi.ingsw.model.effects.build.NotOnSamePosition;
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
    Turn currentTurn;
    List<GodPower> godList = new ArrayList<>();
    Board board = new Board();

    @BeforeEach
    void setUp() throws IOException, ParseException {
        board = new Board();
        currentTurn = new Turn(player1);

        godList.add(GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard3.json"), 1));
        godList.add(GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard6.json"), 2));
    }

    @Test
    void originalDemeterGodPowerShouldBeRestored() {
        Turn oldTurn = new Turn(player1);
        assert godList.get(0).getPlayerId()==1;
        assert godList.get(1).getPlayerId()==2;

        board.getCell(0,0,0).setWorker(worker1);
        board.getCell(1,1,0).setBuilding(true);
        board.newCell(1,1,1);

        Position workerPosition = board.getCell(0,0,0).getPosition();
        Position destinationPosition = board.getCell(1,1,1).getPosition();

        godList.get(0).moveInto(board, workerPosition, destinationPosition);

        oldTurn.updateTurnInfoAfterMove(workerPosition, destinationPosition, board);

        currentTurn = godList.get(0).endTurn(oldTurn, godList, player2);
        assertEquals(godList.get(1).getNewTurn().getClass(),  RestoreOriginalGodPower.class);

        restoreOriginalGodPower = new RestoreOriginalGodPower(godList.get(1).copyGodPower(godList.get(1)));
        godList.get(1).setBuild(null); //random modification
        restoreOriginalGodPower.endTurn(currentTurn, godList, player2);

        assertAll("OriginalGod Restored", () -> assertEquals(godList.get(1).getBuild().getClass(), NotOnSamePosition.class));
    }
}
