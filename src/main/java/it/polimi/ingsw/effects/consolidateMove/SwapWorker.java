package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.*;


public class SwapWorker extends StandardConsolidateMove {
        public void moveInto (Board board, Position workerPosition, Position destinationPosition){
            Cell workerCell = board.getCell(workerPosition);
            Cell destinationCell = board.getCell(destinationPosition);
            // se nella cella di arrivo non c'è un lavoratore, eseguo il metodo della classe padre
            if (destinationCell.isFree()) super.moveInto(board, workerPosition, destinationPosition);
            else {
                Worker tempWorker = workerCell.getWorker();
                workerCell.setWorker(destinationCell.getWorker());
                destinationCell.setWorker(tempWorker);
            }
        }
    }