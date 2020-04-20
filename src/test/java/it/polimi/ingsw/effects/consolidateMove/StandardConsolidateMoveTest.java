package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Worker;
import it.polimi.ingsw.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
    void MovingOnTheSameLevel() {
        //The Standard ConsolidateMove just consolidate the action, so we can try every different Destination Cell of the board
        //So we can verify the mehod for every WorkerCell in Board and everyDestination

        board.newCell(0,1,1);
        workerCell = board.getCell(0,1,1);
        workerCell.setWorker(worker);
        board.newCell(1,1,1);
        destinationCell = board.getCell(1,1,1);

        standardConsolidateMove.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());

        assertAll("moveInto", () -> assertTrue(workerCell.isFree()),
                () ->assertTrue(destinationCell.isWorker()),
                () ->assertSame(destinationCell.getWorker(), worker),
                () ->assertSame(destinationCell.getPlayer(), player));

    }

    @Test
    void MovingUp() {
        //The Standard ConsolidateMove just consolidate the action, so we can try every different Destination Cell of the board
        //So we can verify the mehod for every WorkerCell in Board and everyDestination

        board.newCell(0,1,0);
        workerCell = board.getCell(0,1,0);
        workerCell.setWorker(worker);
        board.newCell(1,1,1);
        destinationCell = board.getCell(1,1,1);

        standardConsolidateMove.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());

        assertAll("moveInto", () -> assertTrue(workerCell.isFree()),
                () ->assertTrue(destinationCell.isWorker()),
                () ->assertSame(destinationCell.getWorker(), worker),
                () ->assertSame(destinationCell.getPlayer(), player));

    }

    @Test
    void MovingDown() {
        //The Standard ConsolidateMove just consolidate the action, so we can try every different Destination Cell of the board
        //So we can verify the mehod for every WorkerCell in Board and everyDestination

        board.newCell(0,1,1);
        workerCell = board.getCell(0,1,1);
        workerCell.setWorker(worker);
        board.newCell(1,1,0);
        destinationCell = board.getCell(1,1,0);

        standardConsolidateMove.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());

        assertAll("moveInto", () -> assertTrue(workerCell.isFree()),
                () ->assertTrue(destinationCell.isWorker()),
                () ->assertSame(destinationCell.getWorker(), worker),
                () ->assertSame(destinationCell.getPlayer(), player));

    }

    @Test
    void NullPointerException () {
        assertThrows(NullPointerException.class, () -> {
            standardConsolidateMove.moveInto(null, null, null);
        });
    }
}
