package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.Test;


public class WorkerTest {
    Worker worker;
    private Player player;

    @Test
    void workerHasTheRightPlayer(){
        player = new Player ("pippo",12);
        worker = new Worker(player, 12);
        assertEquals(player, worker.getPlayer());
    }

    @Test
    void workerShouldNotHaveTheRightPlayer(){
        Player player1 = new Player ("ciccio", 14);
        Player player2 = new Player ("pippo",12);
        worker = new Worker (player2, 12);
        assertNotEquals(player1, worker.getPlayer());
    }



}


