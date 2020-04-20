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
    void MovingOnTheSameLevel() {
        //The Standard ConsolidateMove just consolidate the action, so we can try every different Destination Cell of the board
        //So we can verify the mehod for every WorkerCell in Board and everyDestination

        board.newCell(0,1,1);
        workerCell = board.getCell(0,1,1);
        workerCell.setWorker(worker1);
        board.newCell(1,1,1);
        destinationCell = board.getCell(1,1,1);

        swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());

        assertAll("moveInto", () -> assertTrue(workerCell.isFree()),
                () ->assertTrue(destinationCell.isWorker()),
                () ->assertSame(destinationCell.getWorker(), worker1),
                () ->assertSame(destinationCell.getPlayer(), player1));

    }

    @Test
    void MovingUp() {
        //The Standard ConsolidateMove just consolidate the action, so we can try every different Destination Cell of the board
        //So we can verify the mehod for every WorkerCell in Board and everyDestination

        board.newCell(0,1,0);
        workerCell = board.getCell(0,1,0);
        workerCell.setWorker(worker1);
        board.newCell(1,1,1);
        destinationCell = board.getCell(1,1,1);

        swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());

        assertAll("moveInto", () -> assertTrue(workerCell.isFree()),
                () ->assertTrue(destinationCell.isWorker()),
                () ->assertSame(destinationCell.getWorker(), worker1),
                () ->assertSame(destinationCell.getPlayer(), player1));

    }

    @Test
    void MovingDown() {
        //The Standard ConsolidateMove just consolidate the action, so we can try every different Destination Cell of the board
        //So we can verify the mehod for every WorkerCell in Board and everyDestination

        board.newCell(0,1,1);
        workerCell = board.getCell(0,1,1);
        workerCell.setWorker(worker1);
        board.newCell(1,1,0);
        destinationCell = board.getCell(1,1,0);

        swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());

        assertAll("moveInto", () -> assertTrue(workerCell.isFree()),
                () ->assertTrue(destinationCell.isWorker()),
                () ->assertSame(destinationCell.getWorker(), worker1),
                () ->assertSame(destinationCell.getPlayer(), player1));

    }

    @Test
    void NullPointerException () {
        assertThrows(NullPointerException.class, () -> {
            swapWorker.moveInto(null, null, null);
        });
    }

    @Test
    void MovingIntoWorkerCellOnSameLevel() {
        board.newCell(1,1,2);
        workerCell = board.getCell(1,1,2);
        workerCell.setWorker(worker1);
        board.newCell(2,1,2);
        destinationCell = board.getCell(2,1,2);
        destinationCell.setWorker(worker2);

        swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
        assertAll("movingIntoWorkerCellOnSameLevel",
                () -> assertTrue(workerCell.isWorker()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertSame(workerCell.getWorker(), worker2),
                () -> assertSame(destinationCell.getWorker(), worker1));
    }

    void MovingIntoWorkerCellUp() {
        board.newCell(1,1,2);
        workerCell = board.getCell(1,1,2);
        workerCell.setWorker(worker1);
        board.newCell(2,1,3);
        destinationCell = board.getCell(2,1,3);
        destinationCell.setWorker(worker2);

        swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
        assertAll("movingIntoWorkerCellOnSameLevel",
                () -> assertTrue(workerCell.isWorker()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertSame(workerCell.getWorker(), worker2),
                () -> assertSame(destinationCell.getWorker(), worker1));
    }

    void MovingIntoWorkerCellDown() {
        board.newCell(1,1,2);
        workerCell = board.getCell(1,1,2);
        workerCell.setWorker(worker1);
        board.newCell(2,1,1);
        destinationCell = board.getCell(2,1,1);
        destinationCell.setWorker(worker2);

        swapWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
        assertAll("movingIntoWorkerCellOnSameLevel",
                () -> assertTrue(workerCell.isWorker()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertSame(workerCell.getWorker(), worker2),
                () -> assertSame(destinationCell.getWorker(), worker1));
    }

}
