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

public class UnderMyselfTest {
    int builds = 1;
    UnderMyself underMyself = new UnderMyself(builds);
    Cell workerCell;
    Turn turn;
    Board board;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);

    @BeforeEach
    void setUp(){
        turn = new Turn(player);
        board = new Board();
    }

    @Test
    void buildShouldGiveTheStandardSetOfPossibleCellsAndTheWorkerPosition() {
        workerCell = board.getCell(3,3,0);
        workerCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(new Position(2,2,0), workerCell.getPosition(), board);

        Set <Position> collect = new HashSet<>();
        collect.add(new Position (4,4,0));
        collect.add(new Position(3,3,0));
        collect.add(new Position (2,2,0));
        collect.add(new Position (3,2,0));
        collect.add(new Position (2,3,0));
        collect.add(new Position (3,4,0));
        collect.add(new Position (4,3,0));
        collect.add(new Position (2,4,0));
        collect.add(new Position (4,2,0));
        assertEquals(collect, underMyself.build(workerCell.getPosition(), board, turn));
    }
    @Test
    void buildShouldNotContainTheWorkerPositionBeacuseZis3() {
        board = new Board();
        board.newCell(3,3,0);
        board.newCell(3,3,1);
        board.newCell(3,3,2);
        board.newCell(3,3,3);

        board.getCell(3,3,0).setBuilding(true);
        board.getCell(3,3,1).setBuilding(true);
        board.getCell(3,3,2).setBuilding(true);

        workerCell = board.getCell(3,3,3);
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
        assertEquals(collect, underMyself.build(workerCell.getPosition(), board, turn));
    }

    @Test
    void buildShouldBeEmpty () {
        board = new Board();
        board.newCell(3,3,3);
        workerCell = board.getCell(3,3,3);
        workerCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(new Position(2,2,0), workerCell.getPosition(), board);
        turn.updateTurnInfoAfterBuild(new Position(4,4,0), worker.getWorkerId());

        Set <Position> collect = new HashSet<>();
        assertEquals(collect, underMyself.build(workerCell.getPosition(), board, turn));
    }

    @Test

    void buildShouldThrowExceptionWithNullParameters () {
        assertThrows(NullPointerException.class, () -> {
            underMyself.build(null, null, null);
        });
    }

}
