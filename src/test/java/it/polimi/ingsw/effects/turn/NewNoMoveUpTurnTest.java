package it.polimi.ingsw.effects.turn;

import it.polimi.ingsw.*;
import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.turn.NewNoMoveUpTurn;
import it.polimi.ingsw.effects.GodPower;
import it.polimi.ingsw.effects.GodPowerManager;
import it.polimi.ingsw.effects.move.NoMoveUp;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewNoMoveUpTurnTest  {
    NewNoMoveUpTurn newNoMoveUpTurn = new NewNoMoveUpTurn();
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
    public void setup () {
    }

    @Test
    void TheEnemyShouldHaveNoMoveUppEffectAfterAthenaTurn() throws ParseException, IOException {
        athenaGodPower = GodPowerManager.power("AthenaCard.json", 1);
        oppositeGodPower = GodPowerManager.power("HestiaCard.json", 2);
        godList.add(athenaGodPower);
        godList.add(oppositeGodPower);

        board.getCell(0,0,0).setWorker(worker1);
        board.newCell(1,1,1);

        Position workerPosition = board.getCell(0,0,0).getPosition();
        Position destinationPosition = board.getCell(1,1,1).getPosition();


        athenaGodPower.moveInto(board, workerPosition, destinationPosition);

        currentTurn.updateTurnInfoAfterMove(workerPosition, destinationPosition, board);
        newNoMoveUpTurn.endTurn(currentTurn, godList, player1);
        assertEquals(godList.get(1).getMove().getClass(), NoMoveUp.class);
    }
}
