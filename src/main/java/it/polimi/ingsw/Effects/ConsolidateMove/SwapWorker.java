package it.polimi.ingsw.Consolidate;

import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import java.util.stream.Stream;

public class SwapWorker extends StandardConsolidateMove {
    public void moveInto (Stream<Cell> board, Cell workerCell, Cell destinationCell){
        // trova quale player e worker vanno spostati
        Player player = workerCell.getPlayer();
        int numOfWorker;
        if (player.getWorkerCell(1).equals(workerCell)) numOfWorker = 1;
        else numOfWorker = 2;

        if (!destinationCell.isWorker()) {
            player.newWorkerCell(destinationCell, numOfWorker);
            return;
        }

        // trova player e worker dell'avversario da swappare
        Player opponentPlayer = destinationCell.getPlayer();
        int numOfOpponentWorker;
        if (opponentPlayer.getWorkerCell(1).equals(destinationCell)) numOfOpponentWorker = 1;
        else numOfOpponentWorker = 2;

        player.setWorkerCell(destinationCell, numOfWorker);
        opponentPlayer.setWorkerCell(workerCell, numOfOpponentWorker);
    }
}
