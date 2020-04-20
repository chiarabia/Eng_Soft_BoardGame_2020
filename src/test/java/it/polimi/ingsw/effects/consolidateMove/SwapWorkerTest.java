package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Worker;
import it.polimi.ingsw.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

public class SwapWorkerTest {
    SwapWorker swapWorker = new SwapWorker();
    Cell workerCell;
    Cell destinationCell;
    Board board;
    Turn turn;
    Player player1 = new Player("pippo",12);
    Player player2 = new Player("pluto", 21);
    Worker worker1 = new Worker(player1, 1);
    Worker worker2 = new Worker(player2, 1);

    @BeforeEach
    void setUp(){
        turn = new Turn(player1);
        board = new Board();
    }

    @Test
    void MoveintoAFreeCell() {
        //The SwapWorker class just consolidate the action, so we can try every different Destination Cell of the board
        //So we can verify the mehod for every WorkerCell in Board and everyDestination
        for (int x=0; x<5; x++) {
            for (int y=0; y<5; y++) {
                for (int z=0; z<4; z++) {
                    for (int i=0; i<5; i++) {
                        for (int j=0; j<5; j++) {
                            for (int k=0; k<4; k++) {
                                if(x!=i && y!=j && k!=z) {
                                    board.newCell(x, y, z);
                                    workerCell = board.getCell(x, y, z);
                                    workerCell.setWorker(worker1);


                                    board.newCell(i, j, k);
                                    destinationCell = board.getCell(i, j, k);

                                    swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
                                    assertTrue(workerCell.isFree());
                                    assertTrue(destinationCell.isWorker());
                                    assertSame(destinationCell.getWorker(), worker1);
                                    assertSame(destinationCell.getPlayer(), player1);
                                    board = new Board();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    void MoveingIntoWorker() {
        //The Standard ConsolidateMove just consolidate the action, so we can try every different Destination Cell of the board
        //So we can verify the mehod for every WorkerCell in Board and everyDestination

        for (int x=0; x<5; x++) {
            for (int y=0; y<5; y++) {
                for (int z=0; z<4; z++) {
                    for (int i=0; i<5; i++) {
                        for (int j=0; j<5; j++) {
                            for (int k=0; k<4; k++) {
                                if(x!=i && y!=j && k!=z) {
                                    board.newCell(x, y, z);
                                    workerCell = board.getCell(x, y, z);
                                    workerCell.setWorker(worker1);


                                    board.newCell(i, j, k);
                                    destinationCell = board.getCell(i, j, k);
                                    destinationCell.setWorker(worker2);

                                    swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
                                    assertTrue(workerCell.isWorker());
                                    assertTrue(destinationCell.isWorker());
                                    assertSame(workerCell.getWorker(), worker2);
                                    assertSame(workerCell.getPlayer(), player2);
                                    assertSame(destinationCell.getWorker(), worker1);
                                    assertSame(destinationCell.getPlayer(), player1);
                                    board = new Board();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
