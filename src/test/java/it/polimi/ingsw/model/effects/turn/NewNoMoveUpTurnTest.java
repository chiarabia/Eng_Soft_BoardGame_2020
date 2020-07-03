package it.polimi.ingsw.model.effects.turn;

import it.polimi.ingsw.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.effects.GodPower;
import it.polimi.ingsw.model.effects.move.StandardMove;
import it.polimi.ingsw.model.effects.GodPowerManager;
import it.polimi.ingsw.model.effects.move.NoMoveUp;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NewNoMoveUpTurnTest  {
    NewNoMoveUpTurn newNoMoveUpTurn = new NewNoMoveUpTurn();
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
    public void setUp () {
        godList = new ArrayList<>();
        board = new Board();
        currentTurn = new Turn(player1);
    }

    @Test
    void theEnemyShouldHaveNoMoveUpEffectAfterAthenaTurn() throws ParseException, IOException {
        athenaGodPower = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard3.json"), 1);;
        oppositeGodPower = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard9.json"), 2);;
        godList.add(athenaGodPower);
        godList.add(oppositeGodPower);

        board.getCell(0,0,0).setWorker(worker1);
        board.getCell(1,1,0).setBuilding(true);
        board.newCell(1,1,1);

        Position workerPosition = board.getCell(0,0,0).getPosition();
        Position destinationPosition = board.getCell(1,1,1).getPosition();

        athenaGodPower.moveInto(board, workerPosition, destinationPosition);

        currentTurn.updateTurnInfoAfterMove(workerPosition, destinationPosition, board);
        newNoMoveUpTurn.endTurn(currentTurn, godList, player2);
        assertAll("NoMoveUp", () -> assertTrue(currentTurn.isMoveUp()),
                () -> assertEquals(godList.get(1).getMove().getClass(), NoMoveUp.class),
                () -> assertEquals(godList.get(1).getNewTurn().getClass(), RestoreOriginalGodPower.class));
    }

    @Test
    void theEnemyShouldNotHaveNoMoveUpEffectAfterAthenaTurn() throws ParseException, IOException {
        athenaGodPower = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard3.json"), 1);;
        oppositeGodPower = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard9.json"), 2);;
        godList.add(athenaGodPower);
        godList.add(oppositeGodPower);

        board.getCell(0,0,0).setWorker(worker1);
        board.newCell(1,1,0);

        Position workerPosition = board.getCell(0,0,0).getPosition();
        Position destinationPosition = board.getCell(1,1,0).getPosition();

        athenaGodPower.moveInto(board, workerPosition, destinationPosition);

        currentTurn.updateTurnInfoAfterMove(workerPosition, destinationPosition, board);
        newNoMoveUpTurn.endTurn(currentTurn, godList, player2);
        assertEquals(godList.get(1).getMove().getClass(), StandardMove.class);
    }
}
