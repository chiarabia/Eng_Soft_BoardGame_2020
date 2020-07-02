package it.polimi.ingsw.effects.consolidateBuild;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.server.serializable.SerializableUpdateBuild;
import it.polimi.ingsw.server.serializable.SerializableUpdateActions;
import it.polimi.ingsw.server.serializable.SerializableUpdateMove;

/**
 * This method modifes the standardConsolidateMethod allowing the costruction of a building on the same position
 * of a player's worker.
 */
public class UnderWorker extends StandardConsolidateBuild {

    @Override
    public SerializableUpdateActions buildUp(Position buildingPosition, Board board, boolean forceDome) {
        Cell tempBuildingCell = board.getCell(buildingPosition);


        /**
         * Modifies the board to build a building in the Position chosen by the player.
         * If there is a worker in the cell, it will be moved above the newly built construction.
         * The movement of the worker cannot lead him to victory: it is a "forced move"
         * @param buildingPosition the position chosen by the player.
         * @param board the board.
         * @param forceDome If true it builds a dome at any level
         * @return a <Code>SerializableUpdateActions</Code> object which contains the build information for updating clients
         * and the move of the worker if necessary.
         */

        if (tempBuildingCell.isWorker()) {
            int workerId = tempBuildingCell.getWorkerId();
            int playerId = tempBuildingCell.getPlayerId();
            Position newPosition = new Position(buildingPosition.getX(), buildingPosition.getY(), buildingPosition.getZ() + 1);
            board.newCell(newPosition.getX(), newPosition.getY(), newPosition.getZ());
            Cell workerDestination = board.getCell(buildingPosition.getX(), buildingPosition.getY(), buildingPosition.getZ() + 1); //new Worker Position

            workerDestination.setWorker(tempBuildingCell.getWorker());
            tempBuildingCell.setWorker(null);
            tempBuildingCell.setBuilding(true);

            SerializableUpdateBuild updateBuild = new SerializableUpdateBuild(buildingPosition,
                    board.getCell(buildingPosition)
                            .isDome());
            SerializableUpdateMove updateMove = new SerializableUpdateMove(buildingPosition, newPosition, playerId, workerId);
            SerializableUpdateActions serializableUpdateActions = new SerializableUpdateActions();

            serializableUpdateActions.addBuildAction(updateBuild);
            serializableUpdateActions.addMoveAction(updateMove);
            
            return serializableUpdateActions;
        }

        else
            return super.buildUp(buildingPosition, board, forceDome);
    }
}
