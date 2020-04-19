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

public class SwapMoveTest {
    int moves = 1;
    SwapMove swapMove = new SwapMove(moves);
    Cell workerOneCellBoardOne;
    Cell workerTwoCellBoardOne;
    Cell workerOneCellBoardTwo;
    Cell workerTwoCellBoardTwo;
    Turn turn;
    Board boardOne;
    Board boardTwo;
    Player playerOne = new Player("pippo",12);
    Worker workerPlayerOne = new Worker(playerOne, 12);
    Player playerTwo = new Player("ciccio",1);
    Worker workerPlayerTwo = new Worker(playerTwo,1);


    @BeforeEach
    void setUp(){
        turn = new Turn(playerOne);
        boardOne = new Board();
        boardTwo = new Board();
    }

    //negative
    //Throws Null Exception at cell.getWorkerId
    @Test
    void playerShouldSwapWithTheOtherPlayer(){
        //setting the first board before the swap
        workerOneCellBoardOne = boardOne.getCell(1,2,0);
        workerOneCellBoardOne.setWorker(workerPlayerOne);
        workerTwoCellBoardOne = boardOne.getCell(1,1,0);
        workerTwoCellBoardOne.setWorker(workerPlayerTwo);
        //setting the second board after the swap
        workerOneCellBoardTwo = boardTwo.getCell(1,1,0);
        workerOneCellBoardTwo.setWorker(workerPlayerOne);
        workerTwoCellBoardTwo = boardTwo.getCell(1,2,0);
        workerTwoCellBoardTwo.setWorker(workerPlayerTwo);

        assertEquals(boardTwo,swapMove.move(workerOneCellBoardOne.getPosition(),boardOne,turn));
    }
}
