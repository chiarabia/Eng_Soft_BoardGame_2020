package it.polimi.ingsw.EffectsTest.WinConditionTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.polimi.ingsw.effects.winCondition.FiveCompletedTowers;
import it.polimi.ingsw.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.Board;



public class FiveCompletedTowersTest {

    final FiveCompletedTowers winCondition = new FiveCompletedTowers();

    Board board;
    Cell workerCell;
    Cell destinationCell;
    Cell[] Tower;
    int i = 0;

    @BeforeEach
    void SetUp(){
        board = new Board();
    }


    //negative
    @Test
    void PlayerShouldWin(){
        workerCell = new Cell(2,0,2);
        destinationCell = new Cell(1,0 ,3);

        while( i < 5){
            board.newCell(i,i,3);
            Tower[i] = board.getCell(i,i,3);
            Tower[i].setDome(true);
            i++;
        }


        assertTrue(winCondition.win(workerCell, destinationCell, board));

    }

    //negative
    @Test
    void PlayerShouldNotWinBecauseTheyDontMoveUp(){
        workerCell = new Cell(2,0,2);
        destinationCell = new Cell(1,0 ,2);

        while( i < 5){
            board.newCell(i,i,3);
            Tower[i] = board.getCell(i,i,3);
            Tower[i].setDome(true);
            i++;
        }

        assertFalse(winCondition.win(workerCell, destinationCell, board));

    }

    //negative
    @Test
    void PlayerShouldNotWinBecauseThereAreLessThanFiveCompletedTowers(){
        workerCell = new Cell(2,0,2);
        destinationCell = new Cell(1,0 ,3);

        while( i < 4){
            board.newCell(i,i,3);
            Tower[i] = board.getCell(i,i,3);
            Tower[i].setDome(true);
            i++;
        }


        assertFalse(winCondition.win(workerCell, destinationCell, board));
    }

    //positive
    @Test
    void ThrowsExceptionsWithNullParameters(){

        assertThrows(NullPointerException.class, () -> {
            winCondition.win(null, null, null);
        });
    }



}
