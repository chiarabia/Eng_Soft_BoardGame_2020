package it.polimi.ingsw.effects.winCondition;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import it.polimi.ingsw.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.Board;



public class WinMovingDownTwoOrMoreLevelTest {

    final WinMovingDownTwoOrMoreLevels winCondition = new WinMovingDownTwoOrMoreLevels();

    Board board;
    Cell workerCell;
    Cell destinationCell;

    @BeforeEach
    void setUp(){
        board = new Board();
    }

    //positive
    @Test
    void playerShouldWinMovingDownFromLevel3toLevel1(){
        board.newCell(1,1,3);
        board.newCell(2,1,1);
        workerCell = board.getCell(1,1 ,3);
        destinationCell = board.getCell(2,1 ,1);

        assertTrue(winCondition.win(workerCell.getPosition(), destinationCell.getPosition(), board));

    }

    //positive
    @Test
    void playerShouldWinMovingDownFromLevel2toLevel0(){
        board.newCell(1,1,2);
        board.newCell(2,1,0);
        workerCell = board.getCell(1,1 ,2);
        destinationCell = board.getCell(2,1 ,0);

        assertTrue(winCondition.win(workerCell.getPosition(), destinationCell.getPosition(), board));

    }

    //positive
    @Test
    void playerDoesNotWinBecauseMovesDownButNotByTwoLevels(){
        board.newCell(1,1,2);
        board.newCell(1,1,1);
        workerCell = board.getCell (1,1,2);
        destinationCell = board.getCell(1,1,1);

        assertFalse(winCondition.win(workerCell.getPosition(),destinationCell.getPosition(),board));
    }

    //positive
    @Test
    void playerDoesNotWinBecauseMovesUpByTwoLevels(){
        board.newCell(1,1,0);
        board.newCell(1,1,2);
        workerCell = board.getCell (1,1,0);
        destinationCell = board.getCell(1,1,2);

        assertFalse(winCondition.win(workerCell.getPosition(),destinationCell.getPosition(),board));
    }

    //positive
    @Test
    void shouldNotThrowsExceptionsWithNullParameters(){

        assertFalse(winCondition.win(null, null, null));
    }


}
