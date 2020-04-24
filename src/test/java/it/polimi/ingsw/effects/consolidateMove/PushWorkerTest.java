package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.*;
import it.polimi.ingsw.effects.consolidateBuild.StandardConsolidateBuild;
import it.polimi.ingsw.effects.winCondition.StandardLoseCondition;
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
    void pushWorkerShouldPushTheWorkerWhenOnTheSameLevel() {
        board.newCell(1,1,2);
        workerCell = board.getCell(1,1,2);
        workerCell.setWorker(worker1);
        board.newCell(2,1,2);
        destinationCell = board.getCell(2,1,2);
        destinationCell.setWorker(worker2);

        board.newCell(3,1,0);
        board.newCell(3,1,1);
        board.newCell(3,1,2);
        board.getCell(3,1,0).setBuilding(true);
        board.getCell(3,1,1).setBuilding(true);

        pushWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
        assertAll("Push Worker",
                () -> assertTrue(workerCell.isFree()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertTrue(board.getCell(3,1,2).isWorker()),
                () -> assertSame(worker1, destinationCell.getWorker()),
                ()-> assertSame(worker2, board.getCell(3,1,2).getWorker()));
    }

    @Test
    void pushingWorkerOnTop() {
        board.newCell(1,1,2);
        workerCell = board.getCell(1,1,2);
        workerCell.setWorker(worker1);
        board.newCell(2,1,2);
        destinationCell = board.getCell(2,1,2);
        destinationCell.setWorker(worker2);

        board.newCell(3,1,0);
        board.newCell(3,1,1);
        board.newCell(3,1,2);
        board.newCell(3,1,3);
        board.getCell(3,1,0).setBuilding(true);
        board.getCell(3,1,1).setBuilding(true);
        board.getCell(3,1,3).setBuilding(true);

        pushWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
        assertAll("Push Worker",
                () -> assertTrue(workerCell.isFree()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertTrue(board.getCell(3,1,3).isWorker()),
                () -> assertSame(worker1, destinationCell.getWorker()),
                ()-> assertSame(worker2, board.getCell(3,1,3).getWorker()));
    }

    @Test
    void workerShouldBePushedWithSameYCoordinates() {
        board.newCell(1,1,2);
        workerCell = board.getCell(1,1,2);
        workerCell.setWorker(worker1);
        board.newCell(2,1,2);
        destinationCell = board.getCell(2,1,2);
        destinationCell.setWorker(worker2);

        pushWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
        assertAll("Push Worker",
                () -> assertTrue(workerCell.isFree()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertTrue(board.getCell(3,1,0).isWorker()),
                () -> assertSame(worker1, destinationCell.getWorker()),
                ()-> assertSame(worker2, board.getCell(3,1,0).getWorker()));
    }

    @Test
    void workerShouldBePushedDiagonally() {
        board.newCell(1, 1, 2);
        workerCell = board.getCell(1, 1, 2);
        workerCell.setWorker(worker1);
        board.newCell(2, 2, 2);
        destinationCell = board.getCell(2, 2, 2);
        destinationCell.setWorker(worker2);

        pushWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
        assertAll("Push Worker",
                () -> assertTrue(workerCell.isFree()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertTrue(board.getCell(3, 3, 0).isWorker()),
                () -> assertSame(worker1, destinationCell.getWorker()),
                () -> assertSame(worker2, board.getCell(3, 3, 0).getWorker()));
    }

    @Test
    void workerShouldBePushedWithTheSameXCoordinate() {
        board.newCell(1, 1, 2);
        workerCell = board.getCell(1, 1, 2);
        workerCell.setWorker(worker1);
        board.newCell(1, 2, 2);
        destinationCell = board.getCell(1, 2, 2);
        destinationCell.setWorker(worker2);

        pushWorker.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
        assertAll("Push Worker",
                () -> assertTrue(workerCell.isFree()),
                () -> assertTrue(destinationCell.isWorker()),
                () -> assertTrue(board.getCell(1, 3, 0).isWorker()),
                () -> assertSame(worker1, destinationCell.getWorker()),
                () -> assertSame(worker2, board.getCell(1, 3, 0).getWorker()));
    }

    @Test
    void whenTheWorkerIsPushedOutsideOfTheBoardShouldThrowNullException () {
        board.newCell(1,1,0);
        workerCell = board.getCell(1,1,0);
        workerCell.setWorker(worker1);
        board.newCell(0,0,0);
        destinationCell = board.getCell(0,0,0);
        destinationCell.setWorker(worker2);

        assertThrows(NullPointerException.class, () -> {
            pushWorker.moveInto(board, null, null);
        });

    }

    //positive
    @Test
    void whenDestinationCellIsFreeMoveIntoShouldCallMoveIntoFromStandardConsolidateMove(){
        workerCell = board.getCell(0,0,0);
        workerCell.setWorker(worker1);
        destinationCell = board.getCell(0,1,0);
        pushWorker.moveInto(board,workerCell.getPosition(),destinationCell.getPosition());
        assertTrue(destinationCell.isWorker());
    }

    //positive
    @Test
    void shouldReturnTheRightXCoordinateOfTheWorkerWhenPlayerAndOpponentWorkerHaveSameX(){
        assertEquals(1,pushWorker.behindWorkerX(1,1));
    }

    //positive
    @Test
    void shouldReturnTheRightXCoordinateOfTheWorkerWhenPlayerHasHigherXThanOpponent(){
        assertEquals(1,pushWorker.behindWorkerX(3,2));
    }

    //positive
    @Test
    void shouldReturnTheRightXCoordinateOfTheWorkerWhenPlayerHasMinorXThanOpponent(){
        assertEquals(3,pushWorker.behindWorkerX(1,2));
    }

    //positive
    @Test
    void shouldReturnTheRightXCoordinateOfTheWorkerWhenPlayerAndOpponentWorkerHaveSameY(){
        assertEquals(1,pushWorker.behindWorkerY(1,1));
    }

    //positive
    @Test
    void shouldReturnTheRightXCoordinateOfTheWorkerWhenPlayerHasHigherYThanOpponent(){
        assertEquals(1,pushWorker.behindWorkerY(3,2));
    }

    //positive
    @Test
    void shouldReturnTheRightXCoordinateOfTheWorkerWhenPlayerHasMinorYThanOpponent(){
        assertEquals(3,pushWorker.behindWorkerY(1,2));
    }

    //positive
    @Test
    void pushWorkerThrowsExceptionWithNullParameters () {
        assertThrows(NullPointerException.class, () -> {
            pushWorker.moveInto(null, null, null);
        });
    }

}

