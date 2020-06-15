package it.polimi.ingsw.effects.consolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.server.serializable.SerializableUpdateActions;
import it.polimi.ingsw.server.serializable.SerializableUpdateMove;


public class StandardConsolidateMove {
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