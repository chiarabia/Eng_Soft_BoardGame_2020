package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.server.serializable.SerializableUpdateActions;


public class PushWorker extends StandardConsolidateMove {
    public SerializableUpdateActions moveInto(Board board, Position workerPosition, Position destinationPosition) {
        Cell workerCell = board.getCell(workerPosition);
        Cell destinationCell = board.getCell(destinationPosition);
        SerializableUpdateActions serializableUpdateActions;

        if (destinationCell.isFree())
           return super.moveInto(board, workerPosition, destinationPosition);
        else {
            int tempX = behindWorkerX(workerCell.getX(), destinationCell.getX());
            int tempY = behindWorkerY(workerCell.getY(), destinationCell.getY());
            int tempZ = board.getZoneLevel(tempX, tempY);
            Cell behind_opposite_worker = board.getCell(tempX, tempY, tempZ);
            serializableUpdateActions = super.moveInto(board, destinationPosition, behind_opposite_worker.getPosition()); // it moves the opponent's workers
            serializableUpdateActions.mergeInfos(super.moveInto(board, workerPosition, destinationPosition)); // it moves the player's worker
            return serializableUpdateActions;
        }
    }

    protected int behindWorkerX(int myWorkerX, int opponentsWorkerX) {
        if (myWorkerX == opponentsWorkerX) {
            return opponentsWorkerX;
        } else if (myWorkerX > opponentsWorkerX) {
            return opponentsWorkerX - 1;
        } else
            return opponentsWorkerX + 1;
    }
    protected int behindWorkerY(int myWorkerY, int opponentsWorkerY) {
        if (myWorkerY == opponentsWorkerY) {
            return opponentsWorkerY;
        } else if (myWorkerY > opponentsWorkerY) {
            return opponentsWorkerY - 1;
        } else
            return opponentsWorkerY + 1;
    }
}