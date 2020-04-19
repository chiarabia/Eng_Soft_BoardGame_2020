package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;


public class StandardConsolidateMove {
    public void moveInto (Board board, Position workerPosition, Position destinationPosition){
        Cell workerCell = board.getCell(workerPosition);
        Cell destinationCell = board.getCell(destinationPosition);
        destinationCell.setWorker(workerCell.getWorker());
        workerCell.setWorker(null);
    }
}