package it.polimi.ingsw.effects.consolidateBuild;

import it.polimi.ingsw.*;
import it.polimi.ingsw.server.serializable.SerializableUpdateInfos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnderWorkerTest {
    UnderWorker underWorker = new UnderWorker();
    Board board;
    Cell workerCell;
    Cell destinationCell;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);
    SerializableUpdateInfos updateInfos;

    @BeforeEach
    void setUP () {
        board = new Board();
        updateInfos = null;
    }

    @Test
    void BuildUpShouldForceWorkerAbove () {
        workerCell = board.getCell(2,2,0);
        workerCell.setWorker(worker);
        updateInfos = underWorker.buildUp(workerCell.getPosition(), board, false);
        destinationCell =  board.getCell(2,2,1);
        assertAll ("underWorker",  () -> assertTrue(destinationCell.isWorker()),
                () -> assertEquals(destinationCell.getWorker(), worker),
                () -> assertNull(workerCell.getWorker()),
                () -> assertEquals(updateInfos.getUpdateBuild().get(0).getNewPosition(), workerCell.getPosition()),
                () -> assertEquals(updateInfos.getUpdateMove().get(0).getNewPosition(), destinationCell.getPosition()));
    }


}
