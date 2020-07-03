package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TurnTest {
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player,1);
    Turn turn;
    Cell workerStartingCell;
    Cell workerDestinationCell;
    Cell buildingCell;
    Board board;

    @BeforeEach
    void setUp(){
        turn = new Turn(player);
        board = new Board();
        workerStartingCell = board.getCell(0,0,0);
        workerDestinationCell = board.getCell(0,1,0);
        workerDestinationCell.setWorker(worker);
        board.newCell(1,1,1);
        buildingCell = board.getCell(1,1,1);
        board.getCell(1,1,0).setBuilding(true);
    }

    //positive
    @Test
    void updateTurnMoveShouldAddAMoveTime(){
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerDestinationCell.getPosition(), board);
        assertEquals(1,turn.getMoveTimes());
    }


    //positive
    @Test
    void updateTurnMoveShouldSaveTheRightWorkerId(){
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerDestinationCell.getPosition(), board);
        assertEquals(workerDestinationCell.getWorkerId(),turn.getWorkerUsed());
    }

    //positive
    @Test
    void updateTurnMoveShouldSaveTheRightWorkerStartingPosition(){
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerDestinationCell.getPosition(), board);
        assertEquals(workerStartingCell.getPosition(),turn.getWorkerStartingPosition());
    }

    //positive
    @Test
    void updateTurnMoveShouldUSetToTrueMoveBeforeBuild(){
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerDestinationCell.getPosition(), board
        );
        assertTrue(turn.isMoveBeforeBuild());
    }

    //Positive
    //The isMoveBeforeBuild is set true after the move, so the test should be positive (before negative).
    @Test
    void updateTurnMoveShouldSetToTrueMoveBeforeBuild(){
        Cell startingCell = board.getCell(1,0,0);
        workerStartingCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(startingCell.getPosition(),workerStartingCell.getPosition(), board);
        assertTrue(turn.isMoveBeforeBuild());
    }


    //positive
    @Test
    void verifyMoveUpShouldReturnTrue(){
        int starting_z = 1;
        int destination_z= 2;
        assertTrue(turn.verifyMoveUp(starting_z,destination_z));
    }

    //positive
    @Test
    void verifyMoveUpShouldReturnFalseBecauseTheMovementIsOnTheSameLevel(){
        int starting_z = 1;
        int destination_z = 1;
        assertFalse(turn.verifyMoveUp(starting_z,destination_z));
    }

    //positive
    @Test
    void verifyMoveUpShouldReturnFalseBecauseDownwardMovement(){
        int starting_z = 1;
        int destination_z = 0;
        assertFalse(turn.verifyMoveUp(starting_z,destination_z));
    }

    //positive
    @Test
    void verifyMoveDownShouldReturnTrue(){
        int starting_z = 1;
        int destination_z = 0;
        assertTrue(turn.verifyMoveDown(starting_z,destination_z));
    }

    //positive
    @Test
    void verifyMoveDownShouldReturnFalseBecauseTheMovementIsOnTheSameLevel(){
        int starting_z=1;
        int destination_z=1;
        assertFalse(turn.verifyMoveDown(starting_z,destination_z));
    }

    //positive
    @Test
    void verifyMoveDownShouldReturnFalseBecauseUpwardMovement(){
        int starting_z=1;
        int destination_z = 2;
        assertFalse(turn.verifyMoveDown(starting_z,destination_z));
    }

    //negative
    @Test
    void updateTurnMoveShouldSetMoveUpToTrue(){
        Cell lowCell = board.getCell(0,0,0);
        board.newCell(0,1,1);
        Cell highCell = board.getCell (0,1,1);
        highCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(lowCell.getPosition(),highCell.getPosition(), board);
        assertTrue(turn.isMoveUp());
    }

    //positive
    @Test
    void updateTurnMoveShouldSetMoveUpToFalseBecauseTheMovementIsOnTheSameLevel(){
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerDestinationCell.getPosition(), board);
        assertFalse(turn.isMoveUp());
    }

    //positive
    @Test
    void updateTurnMoveShouldSetMoveUpToFalseBecauseDownwardMovement(){
        Cell lowCell = board.getCell(0,0,0);
        board.newCell(0,1,1);
        Cell highCell = board.getCell (0,1,1);
        lowCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(highCell.getPosition(),lowCell.getPosition(),board);
        assertFalse(turn.isMoveUp());
    }

    //negative
    @Test
    void updateTurnMoveShouldSetMoveDownToTrue(){
        Cell lowCell = board.getCell (0,0,0);
        board.newCell(0,1,1);
        Cell highCell = board.getCell(0,1,1);
        lowCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(highCell.getPosition(),lowCell.getPosition(), board);
        assertTrue(turn.isMoveDown());
    }

    //positive
    @Test
    void updateTurnMoveShouldSetMoveDownToFalseBecauseTheMovementIsOnTheSameLevel(){
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerDestinationCell.getPosition(), board);
        assertFalse(turn.isMoveDown());
    }

    //positive
    @Test
    void updateTurnMoveShouldSetMoveDownToFalseBecauseUpwardMovement(){
        Cell lowCell = board.getCell(0,0,0);
        board.newCell(0,1,1);
        Cell highCell = board.getCell(0,1,1);
        highCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(lowCell.getPosition(),highCell.getPosition(), board);
        assertFalse(turn.isMoveDown());
    }

    //positive
    @Test
    void updateTurnMoveShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            turn.updateTurnInfoAfterMove(null, null, null);
        });
    }

    //positive
    @Test
    void updateTurnBuildShouldAddABuildTime(){
        turn.updateTurnInfoAfterBuild(buildingCell.getPosition(), 1);
        assertEquals(1,turn.getBuildTimes());
    }

    //positive
    @Test
    void updateTurnBuildShouldSaveTheRightBuildingPosition(){
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerDestinationCell.getPosition(), board);
        turn.updateTurnInfoAfterBuild(buildingCell.getPosition(), 1);
        assertEquals(buildingCell.getPosition(),turn.getFirstBuildingPosition());
    }

    //positive
    @Test
    void updateTurnBuildShouldSetBuildAfterMoveToTrue(){
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerDestinationCell.getPosition(), board);
        turn.updateTurnInfoAfterBuild(buildingCell.getPosition(),1);
        assertTrue(turn.isBuildAfterMove());
    }

    //positive
    @Test
    void updateTurnBuildShouldLeaveBuildAfterMoveToFalse(){
        turn.updateTurnInfoAfterBuild(buildingCell.getPosition(), 1);
        assertFalse(turn.isBuildAfterMove());
    }

    //positive
    /*@Test
    //TODO, this method doesn't call methods on the BuildingPosition object, then it will never throw the Null Pointer Exception
    void updateTurnBuildShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            turn.updateTurnInfoAfterBuild(null);
        });
    }*/
}
