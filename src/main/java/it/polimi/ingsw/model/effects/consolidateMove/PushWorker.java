package it.polimi.ingsw.model.effects.consolidateMove;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.controller.server.serializable.SerializableUpdateActions;

/**
 * This class modifies the standard consolidate move to update the model after a PushForwardMove
 */

public class PushWorker extends StandardConsolidateMove {

    /**
     * Modifies the board to move a worker in the Position chosen by the player.
     * If the target position is occupied by a worker, the position of the worker will also be updated.
     * The enemy worker will be moved one cell back.
     * @param destinationPosition position chosen by the player.
     * @param board the board.
     * @param workerPosition the starting position of the worker.
     * @return a <Code>SerializableUpdateActions</Code> object which contains the move information of both workers for updating clients
     */

    public SerializableUpdateActions moveInto(Board board, Position workerPosition, Position destinationPosition) {
        Cell workerCell = board.getCell(workerPosition);
        Cell destinationCell = board.getCell(destinationPosition);
        SerializableUpdateActions serializableUpdateActions;

        if (destinationCell.isFree())
           return super.moveInto(board, workerPosition, destinationPosition);
        else {
            int tempX = behindWorker(workerCell.getX(), destinationCell.getX());
            int tempY = behindWorker(workerCell.getY(), destinationCell.getY());
            int tempZ = board.getZoneLevel(tempX, tempY);
            Cell behind_opposite_worker = board.getCell(tempX, tempY, tempZ);
            serializableUpdateActions = super.moveInto(board, destinationPosition, behind_opposite_worker.getPosition()); // it moves the opponent's workers
            serializableUpdateActions.mergeInfos(super.moveInto(board, workerPosition, destinationPosition)); // it moves the player's worker
            return serializableUpdateActions;
        }
    }

    /**
     * Calculates the coordinates of the new Position of the worker.
     * @param myWorkerCoordinate x or y coordinate of my worker
     * @param opponentsWorkerCoordinate x or y coordinate of enemy worker.
     * @return x or y coordinate of the position behind enemy worker.
     */

    protected int behindWorker(int myWorkerCoordinate, int opponentsWorkerCoordinate) {
        if (myWorkerCoordinate == opponentsWorkerCoordinate) {
            return opponentsWorkerCoordinate;
        } else if (myWorkerCoordinate > opponentsWorkerCoordinate) {
            return opponentsWorkerCoordinate - 1;
        } else
            return opponentsWorkerCoordinate + 1;
    }
}