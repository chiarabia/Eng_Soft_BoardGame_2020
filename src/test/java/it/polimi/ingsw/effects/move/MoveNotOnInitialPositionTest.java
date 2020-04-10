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
import it.polimi.ingsw.effects.consolidateMove.StandardConsolidateMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;


public class MoveNotOnInitialPositionTest {
    int moves = 2;
    MoveNotOnInitialPosition notOnInitialPosition = new MoveNotOnInitialPosition(moves);
    Cell workerCell;
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
    @Test
    void playerShouldMoveTwoTimesInARow(){
        Cell workerCell = board.getCell(0,0,0); //worker initial position
        Cell destinationCell = new Cell(1,0, 0);
        workerCell.setWorker(worker);

        StandardConsolidateMove standardConsolidateMove = new StandardConsolidateMove();
        standardConsolidateMove.moveInto(board, workerCell, destinationCell);
        turn.updateTurnInfoAfterMove(workerCell,destinationCell);
        assert turn.getMoveTimes()==1;
        assert destinationCell.isWorker();
        assert !workerCell.isWorker();
        assert turn.getWorkerStartingPosition() != null;
        assert turn.getWorkerStartingPosition().equals(workerCell.getPosition());
        //I have already moved and I have updated the turn infos
        //The worker now is in 1,0,0

        assert !destinationCell.isFree();

        assert !notOnInitialPosition.move(destinationCell,board, turn).contains(workerCell); //toglie correttamente la posizione iniziale
        assert notOnInitialPosition.move(destinationCell,board, turn).contains(destinationCell);
        assert notOnInitialPosition.move(destinationCell,board, turn).size() == 5; //lol, non toglie la casella di partenza


         //turn.updateTurnInfoAfterMove(workerCell,workerCell);
         assertTrue(notOnInitialPosition.checkMoveConditions(destinationCell,turn));

    }

    //positive
    @Test
    void moveConditionShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            notOnInitialPosition.checkMoveConditions(null, null);
        });
    }

}
