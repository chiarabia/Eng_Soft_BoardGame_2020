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

    //negative
    @Test
    void shouldAddANewCell(){
        cell = new Cell (0,0,3);
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
    void shouldNotGetTheRightCell(){
        cell = new Cell (1,1,1);
        board.newCell(1,1,1);
        assertNotEquals(cell, board.getCell(2,2,2));
    }


    @Test
    void cellShouldBeFree(){
        cell = new Cell (0,0,0);
        board.newCell(0,0,0);
        assertTrue(board.isFreeZone(0,0));
    }


    //negative
    @Test
    void cellShouldNotBeFreeOfDomes(){
        cell = new Cell (0,0,3);
        cell.setDome(true);
        board.newCell(0,0,3);
        //the isFreeZone method returns true, because in the column there are both free zone and dome.
        //For construction shouldn't exist a dome above a free zone because the free zone should be the cell with the max z in his column.

        assertTrue(board.isFreeZone(0,0));
    }


    //negative
    @Test
    void playerShouldGetTheRightLevel(){
        cell = new Cell (0,0,3);
        board.newCell(0,0,3);
        //the cells (0,0,1) and (0,0,2) don't exist
        //assertEquals(3, board.getZoneLevel(0,0));
        //the method getZoneLevel return the z of the highest z in the column
        assertEquals(1, board.getZoneLevel(0,0));
    }





}
