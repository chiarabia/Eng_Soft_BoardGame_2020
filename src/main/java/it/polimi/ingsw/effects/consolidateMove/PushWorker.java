package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.server.serializable.SerializableUpdateInfos;


public class PushWorker extends StandardConsolidateMove {
    public SerializableUpdateInfos moveInto(Board board, Position workerPosition, Position destinationPosition) {
        Cell workerCell = board.getCell(workerPosition);
        Cell destinationCell = board.getCell(destinationPosition);
        SerializableUpdateInfos serializableUpdateInfos;

        if (destinationCell.isFree())
           return super.moveInto(board, workerPosition, destinationPosition);
        else {
            int tempX = behindWorkerX(workerCell.getX(), destinationCell.getX());
            int tempY = behindWorkerY(workerCell.getY(), destinationCell.getY());
            int tempZ = board.getZoneLevel(tempX, tempY);
            //otteniamo la cella posta alle spalle del lavoratore nemico
            Cell behind_opposite_worker = board.getCell(tempX, tempY, tempZ);
            //sposto il lavoro avversario nella casella posta alle sue spalle
            serializableUpdateInfos = super.moveInto(board, destinationPosition, behind_opposite_worker.getPosition());
            //sposto il mio player
            serializableUpdateInfos.mergeInfos(super.moveInto(board, workerPosition, destinationPosition));
            return serializableUpdateInfos;
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