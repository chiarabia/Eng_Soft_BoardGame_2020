package it.polimi.ingsw.model.effects.move;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NoMoveUpAfterBuildTest {
    int moves = 1;
    NoMoveUpAfterBuild noMoveUpAfterBuild = new NoMoveUpAfterBuild(moves);
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

    //positive
    @Test
    void playerShouldNotBeAbleToMoveUp(){
        Cell buildCell = board.getCell(1,1,0);
        turn.updateTurnInfoAfterBuild(buildCell.getPosition(), worker.getWorkerId());
        board.getCell(1,1,0).setBuilding(true);
        board.newCell(0,1,1);
        workerCell = board.getCell(0,0,0);
        workerCell.setWorker(worker);

        Set<Position> collect = new HashSet<>();
        collect.add(new Position(0,1,0));
        collect.add(new Position(1,0,0));
        assertEquals(collect,noMoveUpAfterBuild.move(workerCell.getPosition(),board,turn));
    }

    @Test
    void playerShoulBeAbleToMoveUp(){
        Cell buildCell = board.getCell(1,1,0);
        buildCell.setBuilding(true);
        board.newCell(1,1,1);
        workerCell = board.getCell(0,0,0);
        workerCell.setWorker(worker);

        Set<Position> collect = new HashSet<>();
        collect.add(new Position(0,1,0));
        collect.add(new Position(1,0,0));
        collect.add(new Position(1,1,1));

        assertEquals(collect,noMoveUpAfterBuild.move(workerCell.getPosition(),board,turn));
    }
}
