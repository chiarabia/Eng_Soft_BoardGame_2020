package it.polimi.ingsw.effects.consolidateBuild;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnderWorkerTest {
    UnderWorker underWorker = new UnderWorker();
    Cell buildingCell;
    Board board;
    boolean forceDome;
    Cell workerCell;
    Cell destinationCell;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);

    @BeforeEach
    void setUP () {
        board = new Board();
    }

    @Test
    void BuildUpShouldForceWorkerAbove () {
        workerCell = board.getCell(2,2,0);
        workerCell.setWorker(worker);
        underWorker.buildUp(workerCell.getPosition(), board, false);
        destinationCell =  board.getCell(2,2,1);
        assertAll ("underWorker",  () -> assertTrue(destinationCell.isWorker()),
                () -> assertEquals(destinationCell.getWorker(), worker),
                () -> assertTrue(workerCell.getWorker()== null));

    }
}
