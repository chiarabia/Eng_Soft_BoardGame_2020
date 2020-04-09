package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class CellTest {

    private Cell cell;
    private Player player = new Player("pippo",12);

    private Worker worker = new Worker(player, 12);

    @BeforeEach
    void SetUp(){
        cell = new Cell (0,0,0);
    }

    //positive
    @Test
    void CellShouldBeFree(){
        assertTrue(cell.isFree());
    }

    //positive
    @Test
    void CellShouldNotBeFreeBecauseOfWorker(){
        cell.setWorker(worker);
        assertFalse(cell.isFree());
    }

    //positive
    @Test
    void CellShouldNotBeFreeBecauseOfDome(){
        cell.setDome(true);
        assertFalse(cell.isFree());
    }

    //positive
    @Test
    void CellShouldNotBeFreeBecauseOfBuilding(){
        cell.setBuilding(true);
        assertFalse(cell.isFree());
    }

    //positive
    @Test
    void CellShouldHaveAWorker(){
        cell.setWorker(worker);
        assertTrue(cell.isWorker());
    }

    //positive
    @Test
    void CellShouldHaveADome(){
        cell.setDome(true);
        assertTrue(cell.isDome());
    }

    //positive
    @Test
    void CellShouldHaveABuilding(){
        cell.setBuilding(true);
        assertTrue(cell.isBuilding());
    }

    //positive
    @Test
    void CellGetsTheRightX(){
        assertEquals(0, cell.getX());
    }

    //positive
    @Test
    void CellShouldGetTheRightY(){
        assertEquals(0, cell.getY());
    }

    //positive
    @Test
    void CellShouldGetTheRightZ(){
        assertEquals(0, cell.getX());
    }

    //positive
    @Test
    void CellShouldGetTheRightWorker(){
        cell.setWorker(worker);
        assertEquals(worker, cell.getWorker());
    }

    //positive
    @Test
    void CellShouldGetTheRightPlayer(){
        cell.setWorker(worker);
        assertEquals(worker.getPlayer(), cell.getPlayer());
    }

    //positive
    @Test
    void CellShouldGetTheRightPlayerId(){
        cell.setWorker(worker);
        assertEquals(worker.getPlayerId(), cell.getPlayerId());
    }

    //positive
    @Test
    void CellShouldGetTheRightWorkerId(){
        cell.setWorker(worker);
        assertEquals(worker.getWorkerId(),cell.getWorkerId());
    }




}
