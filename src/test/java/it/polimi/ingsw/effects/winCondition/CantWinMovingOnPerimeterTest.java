package it.polimi.ingsw.effects.winCondition;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.polimi.ingsw.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.Board;


public class CantWinMovingOnPerimeterTest {

    final CantWinMovingOnPerimeter winCondition = new CantWinMovingOnPerimeter();

    Board board;
    Cell workerCell;
    Cell destinationCell;

    @BeforeEach
    void setUp(){
        board = new Board();
    }

    //positive
    @Test
    void playerShouldNotWinByMovingOnAPerimeterCell(){
        workerCell = new Cell(1,1 ,2);
        destinationCell = new Cell(0,0 ,3);

        assertFalse(winCondition.win(workerCell.getPosition(), destinationCell.getPosition(), board));

    }

    //positive
    @Test
    void playerIsNotBlockedByTheCondition(){
        workerCell = new Cell(1,1 ,2);
        destinationCell = new Cell(2,1 ,3);

        assertTrue(winCondition.win(workerCell.getPosition(), destinationCell.getPosition(), board));
    }

    //negative
    @Test
    void throwsExceptionsWithNullParameters(){

        assertThrows(NullPointerException.class, () -> {
            winCondition.win(null, null, null);
        });
    }



}
