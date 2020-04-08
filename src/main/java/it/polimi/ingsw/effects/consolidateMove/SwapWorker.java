package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Worker;


    public class SwapWorker extends StandardConsolidateMove {
        public void moveInto (Board board, Cell workerCell, Cell destinationCell) {
            // se nella cella di arrivo non c'è un lavoratore, eseguo il metodo della classe padre
            if (destinationCell.isFree()) super.moveInto(board, workerCell, destinationCell);
            else {
                Worker tempWorker = workerCell.getWorker();
                workerCell.setWorker(destinationCell.getWorker());
                destinationCell.setWorker(tempWorker);
            }
        }
    }