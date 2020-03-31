package it.polimi.ingsw.Effects.ConsolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Position;

import java.util.stream.Stream;


public class StandardConsolidateMove {
    public void moveInto (Board board, Cell workerCell, Cell destinationCell){
        destinationCell.setWorker(true); // setto la cella di arrivo come nuova cella del lavoratore
        destinationCell.setPlayer(workerCell.getPlayer());
        destinationCell.setWorkerId(workerCell.getWorkerId());

        workerCell.setWorkerId(0); //libero la cella di partenza
        workerCell.setPlayer(null);
        workerCell.setWorker(false);


        //TODO: notifyPlayer

        /*Player player = workerCell.getPlayer();
        int numOfWorker;
        if (player.getWorkerCell(1).equals(workerCell)) numOfWorker = 1;
        else numOfWorker = 2;
        player.newWorkerCell(destinationCell, numOfWorker);*/
    }
}
