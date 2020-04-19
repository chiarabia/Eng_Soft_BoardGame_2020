package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;


public class BoardTest {

    private Board board;
    private Cell cell;

    @BeforeEach
    void setUp(){
        board = new Board();
    }

    //positive
    @Test
    void shouldAddANewCell(){
        board.newCell(0,0,3);
        assertNotNull(board.getCell(0,0,3));
    }

    //positive
    @Test
    void shouldNotHaveANullCell(){
        int x = 2;
        int y = 2;
        assertNotNull(board.getCell(x,y,0));
    }


    //positive
    @Test
    void cellShouldBeFree(){
        board.newCell(0,0,0);
        assertTrue(board.isFreeZone(0,0));
    }


    //positive
    @Test
    void cellShouldNotBeFree(){
        board.getCell(0,0,0).setBuilding(true);
        board.newCell(0,0,3);
        board.getCell(0,0,3).setDome(true);
        board.newCell(0,0,1);
        board.getCell(0,0,1).setBuilding(true);
        board.newCell(0,0,2);
        board.getCell(0,0,2).setBuilding(true);
        assertFalse(board.isFreeZone(0,0));
    }


    //positive
    @Test
    void playerShouldGetTheRightLevel(){
        board.newCell(0,0,3);
        board.newCell(0,0,1);
        board.newCell(0,0,2);
        assertEquals(3, board.getZoneLevel(0,0));
    }

    @Test
    void playerShouldNotGetTheRightLevelBecauseCellIsOutsideOfTheBoard(){
        assertFalse(board.isFreeZone(6,6));
    }




}
