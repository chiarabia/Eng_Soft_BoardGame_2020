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
        turn.updateTurnInfoAfterMove(workerStartingCell,workerCell);
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
