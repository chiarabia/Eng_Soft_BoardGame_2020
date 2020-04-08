package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;


public class StandardConsolidateMove {
    public void moveInto (Board board, Cell workerCell, Cell destinationCell){
        destinationCell.setWorker(workerCell.getWorker());
        workerCell.setWorker(null);
        //TODO: notifyPlayer
    }
}