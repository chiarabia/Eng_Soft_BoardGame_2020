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

    int moves = 2;
    MoveNotOnInitialPosition notOnInitialPosition = new MoveNotOnInitialPosition(moves);
    Cell workerCell;
    Cell destinationCell;
    Board board;

    Turn turn;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);

    @BeforeEach
    void setUp(){
        board = new Board();
        turn = new Turn (player);
    }


    //negative
    //Throws Null Exception at cell.getWorkerId
    @Test
    void playerShouldNotMoveOnInitialPosition(){
        worker = new Worker(player, 12);
        workerCell = board.getCell(0,0,0);
        destinationCell = board.getCell(1,0, 0);
        workerCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(workerCell,destinationCell);
        Set <Cell> collect = new HashSet<>();
        collect.add(new Cell (1,0,0));
        collect.add(new Cell (0,2,0));
        collect.add(new Cell (1,1,0));
        collect.add(new Cell(1,2,0));
        assertEquals(collect,notOnInitialPosition.move(destinationCell,board,turn));
    }

    //positive
    @Test
    void moveConditionShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            notOnInitialPosition.checkMoveConditions(null, null);
        });
    }

}
