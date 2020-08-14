package it.polimi.ingsw.model.effects.move;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SwapMoveTest {
    int moves = 1;
    SwapMove swapMove = new SwapMove(moves);
    Cell workerOneCell;
    Cell workerTwoCell;
    Turn turn;
    Board board;
    Player playerOne = new Player("pippo",12);
    Worker workerPlayerOne = new Worker(playerOne, 12);
    Player playerTwo = new Player("ciccio",1);
    Worker workerPlayerTwo = new Worker(playerTwo,1);


    @BeforeEach
    void setUp(){
        turn = new Turn(playerOne);
        board = new Board();
    }

    //positive

    @Test
    void playerShouldSwapWithTheOtherPlayer(){
        //setting the first board before the swap
        workerOneCell = board.getCell(1,2,0);
        workerOneCell.setWorker(workerPlayerOne);
        workerTwoCell = board.getCell(1,1,0);
        workerTwoCell.setWorker(workerPlayerTwo);

        assertTrue(swapMove.move(workerOneCell.getPosition(),board,turn).contains(workerTwoCell.getPosition()));
    }
}
