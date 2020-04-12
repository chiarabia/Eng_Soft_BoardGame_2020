package it.polimi.ingsw.effects.winCondition;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


import it.polimi.ingsw.Cell;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Worker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.Board;



public class FiveCompletedTowersTest {

    final FiveCompletedTowers winCondition = new FiveCompletedTowers();

    Board board;
    Cell workerCell;
    Cell destinationCell;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);

    @BeforeEach
    void SetUp(){
        board = new Board();
    }

    //negative
    @Test
    void playerShouldWin(){
        board.newCell(2,2,2);
        workerCell = board.getCell(2,2,2);
        workerCell.setWorker(worker);
        board.newCell(2,2 ,1);
        destinationCell = board.getCell(2,2 ,1);

        for (int x = 0; x<5; x++) {
            for (int z= 0; z<3; z++ ) {
                board.newCell(x, 0, z);
                board.getCell(x, 0, z).setBuilding(true);
            }
            board.newCell(x,0,3);
            board.getCell(x, 0, 3).setDome(true);

        }
        assertTrue(winCondition.win(workerCell, destinationCell, board));
    }

    //positive
    @Test
    void playerShouldWin2(){ //This victory condition does not consider the starting and finishing boxes
        workerCell = new Cell(2,0,2);
        destinationCell = new Cell(1,0 ,2);
        board = new Board();

        for (int i = 0;  i < 5; i++){
            board.newCell(i,i,3);
            board.getCell(i,i,3).setDome(true);
        }

        assertTrue(winCondition.win(workerCell, destinationCell, board));

    }

    //positive

    @Test
    void playerShouldNotWinBecauseThereAreLessThanFiveCompletedTowers(){
        workerCell = new Cell(2,0,2);
        destinationCell = new Cell(1,0 ,3);
        board = new Board();
        while( i < 4){
            board.newCell(i,i,3);
            board.getCell(i,i,3).setDome(true);
            i++;
        }


        assertFalse(winCondition.win(workerCell, destinationCell, board));
    }

    //positive
    @Test
    void throwsExceptionsWithNullParameters(){

        assertThrows(NullPointerException.class, () -> {
            winCondition.win(null, null, null);
        });
    }



}
