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

public class OnSamePositionBlockOnlyTest {
    int builds = 2;
    OnSamePositionBlockOnly onSamePositionBlockOnly = new OnSamePositionBlockOnly(builds);
    Cell workerCell;
    Cell destinationCell;
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
        assertEquals(collect, onSamePositionBlockOnly.build(workerCell.getPosition(), board, turn));
    }

    @Test
    void buildShouldGiveOnlyTheFirstPosition() {
        board = new Board();
        workerCell = board.getCell(2, 2, 0);
        workerCell.setWorker(worker);
        destinationCell = board.getCell(3,3,0);

        new StandardConsolidateMove().moveInto(board, workerCell.getPosition(), destinationCell.getPosition());
        turn.updateTurnInfoAfterMove(workerCell.getPosition(), destinationCell.getPosition(), board);
        new StandardConsolidateBuild().buildUp(new Position(4,4,0), board, false);
        turn.updateTurnInfoAfterBuild(new Position(4,4,0));

        Set<Position> collect = new HashSet<>();
        collect.add(new Position (4,4,1));
        assertEquals(collect, onSamePositionBlockOnly.build(destinationCell.getPosition(), board, turn));
    }

    @Test
    void shouldReturnANewHasSetIfConditionsAreNotMet(){
        new StandardConsolidateBuild().buildUp(new Position(4,4,0), board, false);
        turn.updateTurnInfoAfterBuild(new Position(4,4,0));
        workerCell = board.getCell(0,0,0);
        Set collect = new HashSet();
        assertEquals(collect, onSamePositionBlockOnly.build(workerCell.getPosition(),board,turn));
    }

    @Test
    void buildShouldThrowExceptionWithNullParameters () {
        assertThrows(NullPointerException.class, () -> {
            onSamePositionBlockOnly.build(null, null, null);
        });
    }
}
