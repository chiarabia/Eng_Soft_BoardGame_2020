package it.polimi.ingsw.effects.build;

import it.polimi.ingsw.*;
import it.polimi.ingsw.effects.consolidateBuild.StandardConsolidateBuild;
import it.polimi.ingsw.effects.consolidateMove.StandardConsolidateMove;
import it.polimi.ingsw.effects.move.NoMoveUpAfterBuild;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BuildBeforeMoveTest{
    int builds = 1;
    BuildBeforeMove buildBeforeMove = new BuildBeforeMove(builds);
    Cell workerCell;
    Turn turn;
    Board board;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);

    @BeforeEach
    void setUp(){
        turn = new Turn (player);
        board = new Board();
    }

    //negative
    @Test
    void buildConditionShouldReturnTrueAlsoIfTheWorkerDidNotMove(){
        workerCell = new Cell(0,0,0);
        workerCell.setWorker(worker);
        assertTrue(buildBeforeMove.checkBuildConditions(workerCell,turn));
    }

    //positive
    @Test
    void buildConditionShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            buildBeforeMove.checkBuildConditions(null, null);
        });
    }

    @Test
    void buildShouldNotBeEmptyBeforeMove () {
        Cell workerCell =  board.getCell(2,2,0);
        workerCell.setWorker(worker);

        Set <Position> collect = new HashSet<>();
        collect.add(new Position (1,1,0));
        collect.add(new Position (1,2,0));
        collect.add(new Position (1,3,0));
        collect.add(new Position (2,3,0));
        collect.add(new Position (3,3,0));
        collect.add(new Position (3,2,0));
        collect.add(new Position (3,1,0));
        collect.add(new Position (2,1,0));

        assertEquals(buildBeforeMove.build(workerCell.getPosition(), board, turn), collect );

    }

    @Test
    void buildShouldNotBeEmptyAfterMoveWithoutPreviousBuild () {
        Cell workerCell =  board.getCell(1,1,0);
        Cell destinationCell = board.getCell(2,2,0);
        workerCell.setWorker(worker);

        StandardConsolidateMove standardConsolidateMove = new StandardConsolidateMove();
        standardConsolidateMove.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());

        turn.updateTurnInfoAfterMove(workerCell.getPosition(),destinationCell.getPosition(), board);

        Set <Position> collect = new HashSet<>();
        collect.add(new Position (1,1,0));
        collect.add(new Position (1,2,0));
        collect.add(new Position (1,3,0));
        collect.add(new Position (2,3,0));
        collect.add(new Position (3,3,0));
        collect.add(new Position (3,2,0));
        collect.add(new Position (3,1,0));
        collect.add(new Position (2,1,0));

        workerCell = destinationCell;

        assertEquals(buildBeforeMove.build(workerCell.getPosition(), board, turn), collect );

    }

    @Test
    void buildShouldNotBeEmptyAfterMoveAndPreviousBuild () {
        Cell workerCell =  board.getCell(1,1,0);
        Cell destinationCell = board.getCell(2,2,0);
        workerCell.setWorker(worker);

        StandardConsolidateBuild standardConsolidateBuild = new StandardConsolidateBuild();
        standardConsolidateBuild.buildUp(board.getCell(0,0,0).getPosition(),board,false);
        turn.updateTurnInfoAfterBuild(board.getCell(0,0,0).getPosition());

        StandardConsolidateMove standardConsolidateMove = new StandardConsolidateMove();
        standardConsolidateMove.moveInto(board, workerCell.getPosition(), destinationCell.getPosition());

        turn.updateTurnInfoAfterMove(workerCell.getPosition(),destinationCell.getPosition(), board);

        Set <Position> collect = new HashSet<>();
        collect.add(new Position (1,1,0));
        collect.add(new Position (1,2,0));
        collect.add(new Position (1,3,0));
        collect.add(new Position (2,3,0));
        collect.add(new Position (3,3,0));
        collect.add(new Position (3,2,0));
        collect.add(new Position (3,1,0));
        collect.add(new Position (2,1,0));


        assertEquals(buildBeforeMove.build(destinationCell.getPosition(), board, turn), collect );
    }

    @Test
    void playerShouldNotBeAbleToMoveTwiceBeforeMove () {
        workerCell = board.getCell(0,0,0);
        workerCell.setWorker(worker);

        new StandardConsolidateBuild().buildUp(board.getCell(1,1,0).getPosition(), board, false);
        turn.updateTurnInfoAfterBuild(board.getCell(1,1,0).getPosition());

        Set <Position> col = new HashSet<>();
        /*collect.add(new Position (1,1,0));
        collect.add(new Position (1,2,0));
        collect.add(new Position (1,3,0));
        collect.add(new Position (2,3,0));
        collect.add(new Position (3,3,0));
        collect.add(new Position (3,2,0));
        collect.add(new Position (3,1,0));
        collect.add(new Position (2,1,0));*/


        assertAll("noMoveUpAfterBuild",
                () -> assertTrue(!turn.isMoveBeforeBuild() && turn.isBuildBeforeMove()),
                () -> assertTrue(turn.getBuildTimes()>0),
                () -> assertFalse(buildBeforeMove.checkBuildConditions(workerCell, turn)),
                () -> assertEquals(buildBeforeMove.build(workerCell.getPosition(), board, turn), col));
    }

}
