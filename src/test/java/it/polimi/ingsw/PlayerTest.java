package it.polimi.ingsw;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Worker;
import it.polimi.ingsw.Turn;
import it.polimi.ingsw.effects.consolidateMove.StandardConsolidateMove;
import it.polimi.ingsw.effects.move.StandardMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    Player player;
    Worker worker1;
    Worker worker2;

    @BeforeEach
    void setUp() {
        player = new Player ("name", 1);
        worker1 = new Worker (player, 1);
        worker2 = new Worker(player, 2);
    }

    @Test
    void CorrectInstantiationOfPlayer() {
        assertTrue(player.getId() == worker1.getPlayerId() && player.getId() == worker2.getPlayerId());
        assertEquals("name", player.getName());
    }

    @Test
    void NewPlayerHasNoWorkerReference () {
        assertNull(player.getWorker(1));
        assertNull(player.getWorker(2));
    }

    @Test
    void WorkerRightlyAdded () {
        player.addWorker(worker1);
        assertSame(player.getWorker(1), worker1); //the reference must be the same contained in the board
        assertSame(player.getWorker(1).getPlayer(), player);
        assertEquals(player.getWorker(1).getPlayerId(), player.getId());
    }

    @Test
    void CouldnotAddaWorkerTwice () {
        player.addWorker(worker1);
        player.addWorker(worker1);
        int count = 0;

        for (int i = 0; i<3; i++) {
            if(player.getWorker(i)!=null) count++;
        }
        assertEquals(count, 1);
    }

    @Test
    void CouldnotAddaWorkerOfDifferentPlayer () {
        Player player2 = new Player("anotherName", 2);
        player2.addWorker(worker1);
        assertNull(player2.getWorker(worker1.getWorkerId()));
    }
}
