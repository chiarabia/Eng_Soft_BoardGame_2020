package it.polimi.ingsw.effects.move;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import it.polimi.ingsw.*;
import it.polimi.ingsw.effects.consolidateMove.StandardConsolidateMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;


public class MoveNotOnInitialPositionTest {
    int moves = 2;
    MoveNotOnInitialPosition notOnInitialPosition = new MoveNotOnInitialPosition(moves);
    StandardConsolidateMove standardConsolidateMove = new StandardConsolidateMove();
    Cell workerCell;
    Cell destinationCell;
    Cell startingCell;
    Board board;
    Turn turn;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);

    @BeforeEach
    void setUp(){
        board = new Board();
        turn = new Turn (player);
    }


    //positive
    @Test
    void playerShouldNotMoveOnInitialPosition(){
        worker = new Worker(player, 12);
        Cell workerCell = board.getCell(0,0,0);
        destinationCell = board.getCell(1,0, 0);
        workerCell.setWorker(worker);
        Position startingPosition = workerCell.getPosition();
        Position destinationPosition = destinationCell.getPosition();

        standardConsolidateMove.moveInto(board,startingPosition,destinationPosition);
        turn.updateTurnInfoAfterMove(startingPosition,destinationPosition, board);

        Set <Position> collect = new HashSet<>();
        collect.add(new Position (1,1,0));
        collect.add(new Position (0,1,0));
        collect.add(new Position (2,1,0));
        collect.add(new Position(2,0,0));
        assertEquals(collect,notOnInitialPosition.move(destinationPosition,board,turn));
    }

    //positive
    @Test
    void moveConditionShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            notOnInitialPosition.checkMoveConditions(null, null);
        });
    }

}
