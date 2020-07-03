package it.polimi.ingsw.model.effects.winCondition;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.effects.winCondition.StandardWinCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.Board;

public class StandardWinConditionTest {

    final StandardWinCondition winCondition = new StandardWinCondition();

    Board board;
    Cell workerCell;
    Cell destinationCell;

    @BeforeEach
    void setUp(){
        board = new Board();
    }

    //positive
    @Test
    void playerShouldWin(){
        board.newCell(1,1,2);
        board.newCell(2,1,3);
        workerCell = board.getCell(1,1 ,2);
        destinationCell = board.getCell(2,1 ,3);

        assertTrue(winCondition.win(workerCell.getPosition(), destinationCell.getPosition(), board));

    }

    //positive
    @Test
    void playerDoesNotWinBecauseMovesOnSameLevel() {
        board.newCell(1,1,3);
        board.newCell(1,1,3);
        workerCell = board.getCell(1, 1, 3);
        destinationCell = board.getCell(1, 1, 3);


        assertFalse(winCondition.win(workerCell.getPosition(), destinationCell.getPosition(),board));
    }

    //positive
    @Test
    void playerDoesNotWinBecauseMovesUpButNotOnLevel3(){
        board.newCell(1,1,1);
        board.newCell(1,1,2);
        workerCell = board.getCell(1,1,1);
        destinationCell = board.getCell(1,1,2);

        assertFalse(winCondition.win(workerCell.getPosition(),destinationCell.getPosition(),board));
    }

    //positive
    @Test
    void shouldNotThrowsExceptionsWithNullParameters(){
        assertFalse(winCondition.win(null, null, null));
    }



}
