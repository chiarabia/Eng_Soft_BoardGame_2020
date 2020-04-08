package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;



public class PushWorker extends StandardConsolidateMove {
    public void moveInto(Board board, Cell workerCell, Cell destinationCell) {
        if (destinationCell.isFree())
            super.moveInto(board, workerCell, destinationCell);
        else {
            int tempX = behindWorkerX(workerCell.getX(), destinationCell.getX());
            int tempY = behindWorkerY(workerCell.getY(), workerCell.getY());
            int tempZ = board.getZoneLevel(tempX, tempY);
            //otteniamo la cella posta alle spalle del lavoratore nemico
            Cell behind_opposite_worker = board.getCell(tempX, tempY, tempZ);
            //sposto il lavoro avversario nella casella posta alle sue spalle
            super.moveInto(board, destinationCell, behind_opposite_worker);
            //sposto il mio player
            super.moveInto(board, workerCell, destinationCell);
        }
    }
    private int behindWorkerX(int myWorkerX, int opponentsWorkerX) {
        if (myWorkerX == opponentsWorkerX) {
            return opponentsWorkerX;
        } else if (myWorkerX > opponentsWorkerX) {
            return opponentsWorkerX - 1;
        } else
            return opponentsWorkerX + 1;
    }
    private int behindWorkerY(int myWorkerY, int opponentsWorkerY) {
        if (myWorkerY == opponentsWorkerY) {
            return opponentsWorkerY;
        } else if (myWorkerY > opponentsWorkerY) {
            return opponentsWorkerY - 1;
        } else
            return opponentsWorkerY + 1;
    }
}