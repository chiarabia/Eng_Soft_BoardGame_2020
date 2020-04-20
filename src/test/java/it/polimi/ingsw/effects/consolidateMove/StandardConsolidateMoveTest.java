package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Worker;
import it.polimi.ingsw.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class StandardConsolidateMoveTest {
    StandardConsolidateMove standardConsolidateMove = new StandardConsolidateMove();
    Cell workerCell;
    Cell destinationCell;
    Board board;
    Turn turn;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);

    @BeforeEach
    void setUp(){
        turn = new Turn(player);
        board = new Board();
    }

    @Test
    void VerifyNormalCondition() {
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
                                    workerCell.setWorker(worker);


                                    board.newCell(i, j, k);
                                    destinationCell = board.getCell(i, j, k);

                                    standardConsolidateMove.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
                                    assertTrue(workerCell.isFree());
                                    assertTrue(destinationCell.isWorker());
                                    assertSame(destinationCell.getWorker(), worker);
                                    assertSame(destinationCell.getPlayer(), player);
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
