package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class BoardTest {

    private Board board;
    Player player = new Player("pippo",12);
    Player player2 = new Player("ciccio",3);
    Worker worker = new Worker(player, 12);
    Worker worker3 = new Worker(player,34);
    Worker worker2 = new Worker(player2,12);

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
    void getWorkerCellsShouldGetTheRightCells(){
        board.getCell(0,0,0).setWorker(worker);
        List<Cell> collect = new ArrayList<>();
        collect.add(new Cell(0,0,0));
        assertEquals(collect,board.getWorkerCells());
    }

    //positive
    @Test
    void getWorkerCellsShouldGetTheRightCellsOfTheSamePlayer(){
        board.getCell(0,0,0).setWorker(worker);
        board.getCell(0,1,0).setWorker(worker2);
        List<Cell> collect = new ArrayList<>();
        collect.add(new Cell(0,0,0));
        assertEquals(collect,board.getWorkerCells(player));
    }

    //positive
    @Test
    void getWorkerCellShouldGetTheRightCellOfTheSpecificWorkerAndPlayer(){
        board.getCell(0,0,0).setWorker(worker);
        board.getCell(0,1,0).setWorker(worker2);
        board.getCell(1,0,0).setWorker(worker3);
        Cell expctedCell = new Cell(0,0,0);
        assertEquals(expctedCell,board.getWorkerCell(player,12));
    }

    //positive
    @Test
    void getWorkerCellShouldReturnNullIfThereAreNoWorkers(){
        assertEquals(null,board.getWorkerCell(player,12));
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
