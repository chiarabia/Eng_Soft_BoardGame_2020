package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.server.serializable.SerializableUpdateActions;
import it.polimi.ingsw.server.serializable.SerializableUpdateMove;

/**
 * This class defines the general consolidate action. Modify the model following a choice of the player.
 */

public class StandardConsolidateMove {

    /**
     * Modifies the board to move a worker in the Position chosen by the player.
     * @param destinationPosition position chosen by the player.
     * @param board the board.
     * @param workerPosition the starting position of the worker.
     * @return a <Code>SerializableUpdateActions</Code> object which contains the move information for updating clients
     */
    public SerializableUpdateActions moveInto (Board board, Position workerPosition, Position destinationPosition){
        Cell workerCell = board.getCell(workerPosition);
        Cell destinationCell = board.getCell(destinationPosition);
        destinationCell.setWorker(workerCell.getWorker());
        workerCell.setWorker(null);

        int playerId = destinationCell.getPlayerId();
        int workerId = destinationCell.getWorkerId();

        SerializableUpdateMove updateMove = new SerializableUpdateMove(workerPosition, destinationPosition, playerId, workerId);
        SerializableUpdateActions serializableUpdateActions = new SerializableUpdateActions();
        serializableUpdateActions.addMoveAction(updateMove);
        return serializableUpdateActions;
    }
}