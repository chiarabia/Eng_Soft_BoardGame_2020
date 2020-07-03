package it.polimi.ingsw.model.effects.consolidateMove;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.controller.server.serializable.SerializableUpdateActions;
import it.polimi.ingsw.controller.server.serializable.SerializableUpdateMove;

/**
 *  This class modifies the standard consolidate move to update the model after a SwapWMove
 */



public class SwapWorker extends StandardConsolidateMove {
    /**
     * Modifies the board to move a worker in the Position chosen by the player.
     * @param destinationPosition position chosen by the player.
     * @param board the board.
     * @param workerPosition the starting position of the worker.
     * @return a <Code>SerializableUpdateActions</Code> object which contains the move information of both workers for updating clients
     */
        public SerializableUpdateActions moveInto (Board board, Position workerPosition, Position destinationPosition){
            Cell workerCell = board.getCell(workerPosition);
            Cell destinationCell = board.getCell(destinationPosition);

            if (destinationCell.isFree()) return super.moveInto(board, workerPosition, destinationPosition);
            else {
                int myWorkerId = workerCell.getWorkerId();
                int myPlayerId = workerCell.getPlayerId();
                int enemyWorkerId = destinationCell.getWorkerId();
                int enemyPlayerId = destinationCell.getPlayerId();
                Worker tempWorker = workerCell.getWorker();
                workerCell.setWorker(destinationCell.getWorker());
                destinationCell.setWorker(tempWorker);

                SerializableUpdateMove myWorkerInfos = new SerializableUpdateMove(workerPosition,destinationPosition, myPlayerId, myWorkerId);
                SerializableUpdateMove enemyWorkerInfos = new SerializableUpdateMove(destinationPosition, workerPosition, enemyPlayerId, enemyWorkerId);

                SerializableUpdateActions serializableUpdateActions = new SerializableUpdateActions();
                serializableUpdateActions.addMoveAction(myWorkerInfos);
                serializableUpdateActions.addMoveAction(enemyWorkerInfos);
                return serializableUpdateActions;
            }
        }
    }