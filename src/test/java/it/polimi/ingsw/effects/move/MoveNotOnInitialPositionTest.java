package it.polimi.ingsw.effects.move;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Worker;
import it.polimi.ingsw.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;


public class MoveNotOnInitialPositionTest {
    int moves = 1;
    MoveNotOnInitialPosition notOnInitialPosition = new MoveNotOnInitialPosition(moves);
    Cell workerCell;
    Turn turn;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);

    @BeforeEach
    void setUp(){
        turn = new Turn (player);
    }


    //negative
    @Test
    void playerShouldMoveTwoTimesInARow(){
         workerCell = new Cell (0,0,0);
         Cell workerCellFirst = new Cell(1,0,0);
         workerCellFirst.setWorker(worker);
         turn.updateTurnInfoAfterMove(workerCellFirst,workerCell);
         assertTrue(notOnInitialPosition.checkMoveConditions(workerCell,turn));
    }

    //positive
    @Test
    void moveConditionShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            notOnInitialPosition.checkMoveConditions(null, null);
        });
    }

}
