package it.polimi.ingsw.effects.build;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import it.polimi.ingsw.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

public class NotOnPerimeterTest {
    int builds = 2;
    NotOnPerimeter notOnPerimeter = new NotOnPerimeter(builds);
    Cell workerCell;
    Turn turn;
    Board board;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 1);

    @BeforeEach
    void setUp(){
        turn = new Turn(player);
    }

    @Test
    void buildShouldGiveTheStandardSetOfPossibleCellsforTheFirstMoveOfTurn() {
        board = new Board();
        workerCell = board.getCell(3,3,0);
        workerCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(new Position(2,2,0), workerCell.getPosition(), board);

        Set <Position> collect = new HashSet<>();
        collect.add(new Position (4,4,0));
        collect.add(new Position (2,2,0));
        collect.add(new Position (3,2,0));
        collect.add(new Position (2,3,0));
        collect.add(new Position (3,4,0));
        collect.add(new Position (4,3,0));
        collect.add(new Position (2,4,0));
        collect.add(new Position (4,2,0));
        assertEquals(collect, notOnPerimeter.build(workerCell.getPosition(), board, turn));
    }

    @Test
    void buildShouldGiveTheRightSetOfPossibleCellswithoutPerimetalCellsAftertheFirstBuild() {
        board = new Board();
        workerCell = board.getCell(3, 3, 0);
        workerCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(new Position(2, 2, 0), workerCell.getPosition(), board);
        board.getCell(1,1,0).setBuilding(true);
        turn.updateTurnInfoAfterBuild(new Position(1,1,0), worker.getWorkerId());

        Set<Position> collect = new HashSet<>();
        collect.add(new Position(2, 3, 0));
        collect.add(new Position(2, 2, 0));
        collect.add(new Position(3, 2, 0));
        assertEquals(collect, notOnPerimeter.build(workerCell.getPosition(), board, turn));
    }

    @Test
    void buildShouldThrowExceptionWithNullParameters () {
        assertThrows(NullPointerException.class, () -> {
            notOnPerimeter.build(null, null, null);
        });
    }
}
