package it.polimi.ingsw;

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
        workerStartingCell = new Cell(0,0,0);
        workerDestinationCell = new Cell (0,1,0);
        workerDestinationCell.setWorker(worker);
        buildingCell = new Cell (1,1,1);
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

    //negative
    @Test
    void updateTurnMoveShouldNotSetToTrueMoveBeforeBuild(){
        Cell startingCell = new Cell (1,0,0);
        workerStartingCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(startingCell.getPosition(),workerStartingCell.getPosition(), board);
        workerStartingCell.setWorker(null);
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerDestinationCell.getPosition(), board);
        assertFalse(turn.isMoveBeforeBuild());
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
        Cell lowCell = new Cell (0,0,0);
        Cell highCell = new Cell (0,1,1);
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
        Cell lowCell = new Cell (0,0,0);
        Cell highCell = new Cell (0,1,1);
        lowCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(highCell.getPosition(),lowCell.getPosition(),board);
        assertFalse(turn.isMoveUp());
    }

    //negative
    @Test
    void updateTurnMoveShouldSetMoveDownToTrue(){
        Cell lowCell = new Cell (0,0,0);
        Cell highCell = new Cell (0,1,1);
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
        Cell lowCell = new Cell (0,0,0);
        Cell highCell = new Cell (0,1,1);
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
        turn.updateTurnInfoAfterBuild(buildingCell.getPosition());
        assertEquals(1,turn.getBuildTimes());
    }

    //positive
    @Test
    void updateTurnBuildShouldSaveTheRightBuildingPosition(){
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerDestinationCell.getPosition(), board);
        turn.updateTurnInfoAfterBuild(buildingCell.getPosition());
        assertEquals(buildingCell.getPosition(),turn.getFirstBuildingPosition());
    }

    //positive
    @Test
    void updateTurnBuildShouldSetBuildAfterMoveToTrue(){
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerDestinationCell.getPosition(), board);
        turn.updateTurnInfoAfterBuild(buildingCell.getPosition());
        assertTrue(turn.isBuildAfterMove());
    }

    //positive
    @Test
    void updateTurnBuildShouldLeaveBuildAfterMoveToFalse(){
        turn.updateTurnInfoAfterBuild(buildingCell.getPosition());
        assertFalse(turn.isBuildAfterMove());
    }

    //positive
    @Test
    void updateTurnBuildShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            turn.updateTurnInfoAfterBuild(null);
        });
    }
}
