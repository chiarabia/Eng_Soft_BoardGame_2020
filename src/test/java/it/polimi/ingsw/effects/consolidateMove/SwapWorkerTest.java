package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.*;
import it.polimi.ingsw.server.serializable.SerializableUpdateActions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SwapWorkerTest {
    SwapWorker swapWorker = new SwapWorker();
    Cell workerCell;
    Cell destinationCell;
    Board board;
    Turn turn;
    Player player1 = new Player("pippo", 1);
    Player player2 = new Player("pluto", 2);
    Worker worker11 = new Worker(player1, 1);
    Worker worker21 = new Worker(player2, 1);
    SerializableUpdateActions updateInfos;

    @BeforeEach
    void setUp() {
        turn = new Turn(player1);
        board = new Board();
        updateInfos = null;
    }

    @Test
    void swapWorkerShouldWorkWhenTheWorkersAreOnTheSameLevel() {
        //The Standard ConsolidateMove just consolidate the action, so we can try every different Destination Cell of the board
        //So we can verify the mehod for every WorkerCell in Board and everyDestination

        board.newCell(0, 1, 1);
        workerCell = board.getCell(0, 1, 1);
        workerCell.setWorker(worker11);
        board.newCell(1, 1, 1);
        destinationCell = board.getCell(1, 1, 1);
        destinationCell.setWorker(worker21);

        updateInfos = swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());

        assertAll("moveInto", () -> assertFalse(workerCell.isFree()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertSame(destinationCell.getWorker(), worker11),
                () -> assertSame(workerCell.getWorker(), worker21),
                () -> assertSame(destinationCell.getPlayer(), player1),
                () -> assertEquals(2, updateInfos.getUpdateMove().size()),
                () -> assertSame(updateInfos.getUpdateMove().get(0).getStartingPosition(), workerCell.getPosition()),
                () -> assertSame(updateInfos.getUpdateMove().get(0).getNewPosition(), destinationCell.getPosition()));
    }

    @Test
    void swapWorkerShouldWorkWhenTheWorkerIsMovingUp() {
        //The Standard ConsolidateMove just consolidate the action, so we can try every different Destination Cell of the board
        //So we can verify the mehod for every WorkerCell in Board and everyDestination

        board.newCell(0, 1, 0);
        workerCell = board.getCell(0, 1, 0);
        workerCell.setWorker(worker11);
        board.newCell(1, 1, 1);
        destinationCell = board.getCell(1, 1, 1);

        swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());

        assertAll("moveInto", () -> assertTrue(workerCell.isFree()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertSame(destinationCell.getWorker(), worker11),
                () -> assertSame(destinationCell.getPlayer(), player1));

    }

    @Test
    void swapWorkerShouldWorkWhenTheWorkerIsMovingDown() {
        //The Standard ConsolidateMove just consolidate the action, so we can try every different Destination Cell of the board
        //So we can verify the mehod for every WorkerCell in Board and everyDestination

        board.newCell(0, 1, 1);
        workerCell = board.getCell(0, 1, 1);
        workerCell.setWorker(worker11);
        board.newCell(1, 1, 0);
        destinationCell = board.getCell(1, 1, 0);

        swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());

        assertAll("moveInto", () -> assertTrue(workerCell.isFree()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertSame(destinationCell.getWorker(), worker11),
                () -> assertSame(destinationCell.getPlayer(), player1));

    }

    @Test
    void swapWorkerThrowsExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> swapWorker.moveInto(null, null, null));
    }

    @Test
    void movingIntoWorkerCellOnSameLevel() {
        board.newCell(1, 1, 2);
        workerCell = board.getCell(1, 1, 2);
        workerCell.setWorker(worker11);
        board.newCell(2, 1, 2);
        destinationCell = board.getCell(2, 1, 2);
        destinationCell.setWorker(worker21);

        swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
        assertAll("movingIntoWorkerCellOnSameLevel",
                () -> assertTrue(workerCell.isWorker()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertSame(workerCell.getWorker(), worker21),
                () -> assertSame(destinationCell.getWorker(), worker11));
    }

    @Test
    void movingIntoWorkerCellUp() {
        board.newCell(1, 1, 2);
        workerCell = board.getCell(1, 1, 2);
        workerCell.setWorker(worker11);
        board.newCell(2, 1, 3);
        destinationCell = board.getCell(2, 1, 3);
        destinationCell.setWorker(worker21);

        swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
        assertAll("movingIntoWorkerCellOnSameLevel",
                () -> assertTrue(workerCell.isWorker()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertSame(workerCell.getWorker(), worker21),
                () -> assertSame(destinationCell.getWorker(), worker11));
    }

    @Test
    void movingIntoWorkerCellDown() {
        board.newCell(1, 1, 2);
        workerCell = board.getCell(1, 1, 2);
        workerCell.setWorker(worker11);
        board.newCell(2, 1, 1);
        destinationCell = board.getCell(2, 1, 1);
        destinationCell.setWorker(worker21);

        swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
        assertAll("movingIntoWorkerCellOnSameLevel",
                () -> assertTrue(workerCell.isWorker()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertSame(workerCell.getWorker(), worker21),
                () -> assertSame(destinationCell.getWorker(), worker11));
    }
}
