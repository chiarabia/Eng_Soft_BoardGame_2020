package it.polimi.ingsw.effects.move;

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



public class StandardMoveTest {
    int moves = 1;
    StandardMove standardMove = new StandardMove(moves);
    Cell workerCell;
    Turn turn;
    Board board;
    Player player = new Player("pippo",12);
    Player player2 = new Player("ciccio",3);
    Worker worker = new Worker(player, 12);
    Worker worker2 = new Worker(player,13);


    @BeforeEach
    void setUp(){
        workerCell = new Cell(0,0,0);
        turn = new Turn(player);
        board = new Board();
    }

    //positive
    @Test
    void moveConditionShouldBeFalseBecauseMissingWorker (){
        assertFalse(standardMove.checkMoveConditions(workerCell,turn));
    }

    //positive
    @Test
    void moveConditionsShouldBeFalseBecauseTurnPlayerIdIsNotTheSameOfThePlayerId(){
        workerCell.setWorker(worker);
        Turn turn2 = new Turn(player2);
        assertFalse(standardMove.checkMoveConditions(workerCell,turn2));
    }

    //positive
    @Test
    void moveConditionsShouldBeFalseBecauseWorkerHasAlreadyMoved(){
        workerCell = board.getCell(0, 0, 0);
        workerCell.setWorker(worker);
        Cell workerStartingCell = board.getCell(1,0,0);
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerCell.getPosition(),board);
        assertFalse(standardMove.checkMoveConditions(workerCell,turn));
    }

    //positive
    @Test
    void moveConditionShouldBeFalseBecauseTheWorkerMovingASecondTimeHasADifferentId(){
        workerCell.setWorker(worker);
        Cell workerCellFirst = board.getCell(1,0,0);
        Cell workerStartingCell = board.getCell(2,0,0);
        workerCellFirst.setWorker(worker2);
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerCellFirst.getPosition(),board);
        assertFalse(standardMove.checkMoveConditions(workerCell,turn));
    }


    //positive
    @Test
    void moveConditionShouldReturnTrue(){
        workerCell.setWorker(worker);
        assertTrue(standardMove.checkMoveConditions(workerCell,turn));
    }

    //positive
    @Test
    void moveShouldNotWorkBecauseMoveConditionsReturnsFalse(){
        Set temp = new HashSet<>();
        assertEquals(temp, standardMove.move(workerCell.getPosition(),board,turn));
    }

    //positive
    @Test
    void moveShouldGiveTheRightSetOfPossibleCellsOnSameLevel(){
        Cell cellWorker=board.getCell(0,0,0);
        cellWorker.setWorker(worker);
        Set <Position> collect = new HashSet<>();
        collect.add(new Position(1,0,0));
        collect.add(new Position (0,1,0));
        collect.add(new Position (1,1,0));
        assertEquals(collect,standardMove.move(cellWorker.getPosition(),board,turn));
    }

    //positive
    @Test
    void moveShouldGiveTheRightSetOfPossibleCellsWithMultipleLevels(){
        board.newCell(0,0,1);
        Cell cellWorker = board.getCell(0,0,1);
        cellWorker.setWorker(worker);
        board.newCell(0,1,1);
        board.getCell(0,1,1).setBuilding(true);
        board.getCell(0,1,0).setBuilding(true);
        board.getCell(0,0,0).setBuilding(true);
        board.newCell(0,1,2);
        Set <Position> collect = new HashSet<>();
        collect.add(new Position(1,0,0));
        collect.add(new Position(1,1,0));
        collect.add(new Position(0,1,2));
        assertEquals(collect,standardMove.move(cellWorker.getPosition(),board,turn));
    }

    //positive
    @Test
    void shouldObtainTheRightLevelDifference(){
        assertEquals(1,standardMove.heightsDifference(2,3));
    }

    //positive
    @Test
    void moveConditionShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            standardMove.checkMoveConditions(null, null);
        });
    }

    //positive
    @Test
    void moveShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            standardMove.move(null, null,null);
        });
    }



    @Test
    void correctInstantiation() {
        board = new Board();
        workerCell = board.getCell(2,2,0);
        workerCell.setWorker(worker);
        //assert workerCell.isWorker();
        //assert !workerCell.isFree();
        //assert !workerCell.isDome();
        //assert !workerCell.isBuilding();
        //assert board.getStream().filter(a-> a.isFree()).count() == 24;
    }

    @Test
    void PlayerCouldnotMoveIfSourrendedByDomes () {
        workerCell = board.getCell(0,4,0);
        workerCell.setWorker(worker);

        board.getCell(0,3,0).setDome(true);
        board.getCell(1,3,0).setDome(true);
        board.getCell(1,4,0).setDome(true);

        assertTrue(standardMove.move(workerCell.getPosition(), board, turn).isEmpty());
    }

}
