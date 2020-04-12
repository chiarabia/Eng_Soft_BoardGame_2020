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
    void setUp(){
        cell = new Cell (0,0,0);
    }

    //positive
    @Test
    void cellShouldBeFree(){
        assertTrue(cell.isFree());
    }

    //positive
    @Test
    void cellShouldNotBeFreeBecauseOfWorker(){
        cell.setWorker(worker);
        assertFalse(cell.isFree());
    }

    //positive
    @Test
    void cellShouldNotBeFreeBecauseOfDome(){
        cell.setDome(true);
        assertFalse(cell.isFree());
    }

    //positive
    @Test
    void cellShouldNotBeFreeBecauseOfBuilding(){
        cell.setBuilding(true);
        assertFalse(cell.isFree());
    }

    //positive
    @Test
    void cellShouldHaveAWorker(){
        cell.setWorker(worker);
        assertTrue(cell.isWorker());
    }

    //positive
    @Test
    void cellShouldHaveADome(){
        cell.setDome(true);
        assertTrue(cell.isDome());
    }

    //positive
    @Test
    void cellShouldHaveABuilding(){
        cell.setBuilding(true);
        assertTrue(cell.isBuilding());
    }

    //positive
    @Test
    void cellGetsTheRightX(){
        assertEquals(0, cell.getX());
    }

    //positive
    @Test
    void cellShouldGetTheRightY(){
        assertEquals(0, cell.getY());
    }

    //positive
    @Test
    void cellShouldGetTheRightZ(){
        assertEquals(0, cell.getX());
    }

    //positive
    @Test
    void cellShouldGetTheRightWorker(){
        cell.setWorker(worker);
        assertEquals(worker, cell.getWorker());
    }

    //positive
    @Test
    void cellShouldGetTheRightPlayer(){
        cell.setWorker(worker);
        assertEquals(worker.getPlayer(), cell.getPlayer());
    }

    //positive
    @Test
    void cellShouldGetTheRightPlayerId(){
        cell.setWorker(worker);
        assertEquals(worker.getPlayerId(), cell.getPlayerId());
    }

    //positive
    @Test
    void cellShouldGetTheRightWorkerId(){
        cell.setWorker(worker);
        assertEquals(worker.getWorkerId(),cell.getWorkerId());
    }




}
