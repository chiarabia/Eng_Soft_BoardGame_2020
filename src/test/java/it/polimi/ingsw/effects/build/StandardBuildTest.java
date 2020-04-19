package it.polimi.ingsw.effects.build;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Worker;
import it.polimi.ingsw.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

public class StandardBuildTest {
    int builds = 1;
    StandardBuild standardBuild = new StandardBuild(builds);
    Cell workerCell;
    Turn turn;
    Board board;
    Player player = new Player("pippo",12);
    Player player2 = new Player("ciccio",3);
    Worker worker = new Worker(player, 12);
    Worker worker2 = new Worker(player,12);

    @BeforeEach
    void setUp(){
        workerCell = new Cell(0,0,0);
        turn = new Turn(player);
        board = new Board();
    }

    //positive
    @Test
    void buildShouldGiveTheRightSetOfPossibleCellsWhenThereAreNoCompletedBuildings(){
        Cell workerStartingCell = board.getCell(1,0,0);
        workerCell = board.getCell(0,0,0);
        workerCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerCell.getPosition(), board);
        Set <Cell> collect = new HashSet<>();
        collect.add(new Cell (1,0,0));
        collect.add(new Cell (0,1,0));
        collect.add(new Cell (1,1,0));
        assertEquals(collect,standardBuild.build(workerCell.getPosition(),board,turn));
    }

    //positive
    @Test
    void buildShouldGiveTheRightSetOfPossibleCellsWhenThereIsACompletedBuilding(){
        Cell workerStartingCell = board.getCell(1,0,0);
        workerCell = board.getCell(0,0,0);
        workerCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerCell.getPosition(), board);
        board.getCell(0,1,0).setBuilding(true);
        board.newCell(0,1,1);
        board.getCell(0,1,1).setBuilding(true);
        board.newCell(0,1,2);
        board.getCell(0,1,2).setBuilding(true);
        board.newCell(0,1,3);
        board.getCell(0,1,3).setDome(true);
        Set <Cell> collect = new HashSet<>();
        collect.add(new Cell (1,0,0));
        collect.add(new Cell(1,1,0));
        assertEquals(collect,standardBuild.build(workerCell.getPosition(),board,turn));
    }

    //positive
    @Test
    void buildShouldGiveTheRightCellsRegardlessOfTheWorkerLevel(){
        Cell workerStartingCell = board.getCell(1,0,0);
        workerCell = board.getCell(0,0,0);
        workerCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerCell.getPosition(), board);
        board.getCell(0,1,0).setBuilding(true);
        board.newCell(0,1,1);
        board.getCell(0,1,1).setBuilding(true);
        board.newCell(0,1,2);
        board.getCell(1,1,0).setBuilding(true);
        board.newCell(1,1,1);
        Set <Cell> collect = new HashSet<>();
        collect.add(new Cell (1,0,0));
        collect.add(new Cell(0,1,2));
        collect.add(new Cell(1,1,1));
        assertEquals(collect,standardBuild.build(workerCell.getPosition(),board,turn));
    }



    //positive
    @Test
    void buildConditionShouldBeFalseBecauseMissingWorker (){
        assertFalse(standardBuild.checkBuildConditions(workerCell,turn));
    }

    //positive
    @Test
    void buildConditionsShouldBeFalseBecauseTurnPlayerIdIsNotTheSameOfThePlayerId(){
        workerCell.setWorker(worker);
        Turn turn2 = new Turn(player2);
        assertFalse(standardBuild.checkBuildConditions(workerCell,turn2));
    }

    //positive
    @Test
    void buildConditionsShouldBeFalseBecauseWorkerHasntAlreadyMoved(){
        workerCell.setWorker(worker);
        assertFalse(standardBuild.checkBuildConditions(workerCell,turn));
    }

    @Test
    void buildConditionShouldBeTrueBecauseTheWorkerHasAlreadyMovedAndNotBuilt(){
        workerCell.setWorker(worker);
        Cell workerStartingCell = new Cell(1,0,0);
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerCell.getPosition(), board);
        assertTrue(standardBuild.checkBuildConditions(workerCell,turn));
    }

    //positive
    @Test
    void buildConditionShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            standardBuild.checkBuildConditions(null, null);
        });
    }





}
