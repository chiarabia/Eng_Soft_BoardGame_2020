package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.*;
import it.polimi.ingsw.effects.consolidateBuild.StandardConsolidateBuild;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PushWorkerTest {
    PushWorker pushWorker = new PushWorker();
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
        //The PushWorker class just consolidate the action, so we can try every different Destination Cell of the board
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

                                    pushWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
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
    void ThrowNullException () {
        assertThrows(NullPointerException.class, () -> {
            pushWorker.moveInto(board, null, null);
        });
    }

    @Test
    void PushingIntoWorker() {
        //The PushWorker class just consolidate the action, so we can try every different Destination Cell of the board
        //So we can verify the mehod for every WorkerCell in Board and everyDestination
        int behindWorkerX;
        int behindWorkerY;
        Cell behindCell;

        for (int x=1; 0<x&&x<4; x++) {
            for (int y=1; 0<y&&y<4; y++) {
                for (int z = 0; z < 4; z++) {
                    for (int k = 0; k <= z + 1 && k < 4; k++) {
                        if (x != 2 && y != 2) {
                            board.newCell(x, y, z);
                            workerCell = board.getCell(x, y, z);
                            workerCell.setWorker(worker1); //try the 8 different positions around the enemy worker

                            if (x == 2) {
                                behindWorkerX = 2;
                            } else if (x > 2) {
                                behindWorkerX = 2 - 1;
                            } else
                                behindWorkerX = 2 + 1;
                            if (y == 2) {
                                behindWorkerY = 2;
                            } else if (y > 2) {
                                behindWorkerY = 2 - 1;
                            } else
                                behindWorkerY = 2 + 1;

                            board.newCell(2, 2, k); //the enemy worker is in the central cell of the board
                            destinationCell = board.getCell(2, 2, k);
                            destinationCell.setWorker(worker2);

                            int i;
                            for (i = 0; i <= k && i < 3; i++) {//creating a tower of building(complete or not) behind worker
                                new StandardConsolidateBuild().BuildUp(new Position(behindWorkerX, behindWorkerY, i), board, false);
                            }
                            behindCell = board.getCell(behindWorkerX, behindWorkerY, i);

                            pushWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());

                            assertTrue(workerCell.isFree());
                            assertTrue(destinationCell.isWorker());
                            assertSame(behindCell.getWorker(), worker2);
                            assertSame(behindCell.getPlayer(), player2);
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
