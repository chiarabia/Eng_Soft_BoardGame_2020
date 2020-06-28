package it.polimi.ingsw.effects.build;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import it.polimi.ingsw.*;
import it.polimi.ingsw.effects.consolidateBuild.StandardConsolidateBuild;
import it.polimi.ingsw.effects.consolidateMove.StandardConsolidateMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

public class NotOnSamePositionTest {
    int builds = 2;
    NotOnSamePosition notOnSamePosition = new NotOnSamePosition(builds);
    StandardConsolidateBuild standardConsolidateBuild = new StandardConsolidateBuild();
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
    void buildShouldGiveTheStandardSetOfPossibleCellsForTheFirstMoveOfTurn() {
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
        assertEquals(collect, notOnSamePosition.build(workerCell.getPosition(), board, turn));
    }

    @Test
    void buildShouldGiveTheRightSetOfPossibleCellsWithoutFirstBuildingCell() {
        board = new Board();
        workerCell = board.getCell(3, 3, 0);
        workerCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(new Position(2, 2, 0), workerCell.getPosition(), board);
        board.getCell(4,4,0).setBuilding(true);
        turn.updateTurnInfoAfterBuild(new Position(4,4,0), worker.getWorkerId());

        Set<Position> collect = new HashSet<>();
        collect.add(new Position (2,2,0));
        collect.add(new Position (3,2,0));
        collect.add(new Position (2,3,0));
        collect.add(new Position (3,4,0));
        collect.add(new Position (4,3,0));
        collect.add(new Position (2,4,0));
        collect.add(new Position (4,2,0));
        assertEquals(collect, notOnSamePosition.build(workerCell.getPosition(), board, turn));
    }

    @Test
    void buildShouldBeEmpty () {
        board = new Board();
        board.newCell(3,3,3);
        workerCell = board.getCell(3,3,3);
        workerCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(new Position(2,2,0), workerCell.getPosition(), board);
        turn.updateTurnInfoAfterBuild(new Position(4,4,0), worker.getWorkerId());
        turn.updateTurnInfoAfterBuild(new Position(4,4, 1), worker.getWorkerId());

        Set <Position> collect = new HashSet<>();
        assertEquals(collect, notOnSamePosition.build(workerCell.getPosition(), board, turn));
    }



    @Test
    void buildShouldThrowExceptionWithNullParameters () {
        assertThrows(NullPointerException.class, () -> {
            notOnSamePosition.build(null, null, null);
        });
    }

    @Test
    void buildShouldGiveTheRightSetOfPossibleCellsWithoutFirstBuildingCell2() {
        board = new Board();
        Cell workerCell1 = board.getCell(0, 0, 0);
        Cell workerCell2 = board.getCell(1, 1, 0);
        Worker worker1 = new Worker (player, 1);
        Worker worker2 = new Worker (player, 2);

        workerCell1.setWorker(worker1);
        workerCell2.setWorker(worker2);

        new StandardConsolidateMove().moveInto(board, workerCell2.getPosition(), new Position(2,0,0));
        turn.updateTurnInfoAfterMove(workerCell2.getPosition(), new Position(2,0,0), board);

        new StandardConsolidateBuild().buildUp(new Position(3,0,0), board,false);
        turn.updateTurnInfoAfterBuild(new Position(3,0,0), worker2.getWorkerId());


        Set<Position> collect = new HashSet<>();
        collect.add(new Position (1,0,0));
        collect.add(new Position (2,1,0));
        collect.add(new Position (3,1,0));
        collect.add(new Position (1,1,0));

        assertEquals(collect, notOnSamePosition.build(new Position(2,0,0), board, turn));
    }

}
