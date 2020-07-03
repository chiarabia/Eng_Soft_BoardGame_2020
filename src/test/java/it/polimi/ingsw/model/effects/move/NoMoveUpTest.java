package it.polimi.ingsw.model.effects.move;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

public class NoMoveUpTest {
    int moves = 1;
    StandardMove decoratedMove = new StandardMove(moves);
    NoMoveUp noMoveUp = new NoMoveUp(decoratedMove);
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
        workerCell = board.getCell(0,0,0);
        workerCell.setWorker(worker);
        board.getCell(0,1,0).setBuilding(true);
        board.newCell(0,1,1);
        Set <Position> collect = new HashSet<>();
        collect.add(new Position(1,0,0));
        collect.add(new Position(1,1,0));
        assertEquals(collect,noMoveUp.move(workerCell.getPosition(),board,turn));
    }

    //positive
    @Test
    void shouldObtainTheRightLevelDifference(){
        assertEquals(1,noMoveUp.heightsDifference(2,3));
    }

    //positive
    @Test
    void moveShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            noMoveUp.move(null, null,null);
        });
    }

}
