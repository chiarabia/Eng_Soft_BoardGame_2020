package it.polimi.ingsw.Consolidate;

import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import java.util.stream.Stream;

public class StandardConsolidateMove {
    public void moveInto (Stream<Cell> board, Cell workerCell, Cell destinationCell){
        Player player = workerCell.getPlayer();
        int numOfWorker;
        if (player.getWorkerCell(1).equals(workerCell)) numOfWorker = 1;
        else numOfWorker = 2;
        player.newWorkerCell(destinationCell, numOfWorker);
    }
}
