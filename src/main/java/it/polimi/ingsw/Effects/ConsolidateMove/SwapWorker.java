package it.polimi.ingsw.Effects.ConsolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import java.util.stream.Stream;

public class SwapWorker extends StandardConsolidateMove {
    public void moveInto (Board board, Cell workerCell, Cell destinationCell){
        // se nella cella di arrivo non c'è un lavoratore, eseguo il metodo della classe padre
        if (destinationCell.isFree())
            super.moveInto(board, workerCell, destinationCell);
        else {
            Player temp_player = workerCell.getPlayer();
            int temp_id = workerCell.getWorkerId();

            workerCell.setPlayer (destinationCell.getPlayer()); //sposto il player avversario nella mia casella
            workerCell.setWorkerId (destinationCell.getWorkerId());

            destinationCell.setPlayer(temp_player); //sposto il mio player nella casella precedentemente occupata dal nemico
            destinationCell.setWorkerId(temp_id);
        }


        /*// trova quale player e worker vanno spostati
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
        opponentPlayer.setWorkerCell(workerCell, numOfOpponentWorker);*/
    }

}
