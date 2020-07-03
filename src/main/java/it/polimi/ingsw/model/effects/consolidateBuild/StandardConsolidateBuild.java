package it.polimi.ingsw.model.effects.consolidateBuild;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.controller.server.serializable.SerializableUpdateBuild;
import it.polimi.ingsw.controller.server.serializable.SerializableUpdateActions;

/**
 * This class implements the general consolidate action. It modifies the model after a choice of the player.
 */

public class StandardConsolidateBuild {

    /**
     * Modifies the board to build a building in the Position chosen by the player.
      * @param buildingPosition the position chosen by the player.
     * @param board the board.
     * @param forceDome If true it builds a dome at any level
     * @return a <Code>SerializableUpdateActions</Code> object which contains the build information for updating clients
     */

    public SerializableUpdateActions buildUp(Position buildingPosition, Board board, boolean forceDome) {
        Cell tempCell = board.getCell(buildingPosition);

        if (forceDome) tempCell.setDome(true);
        else {
            if (buildingPosition.getZ()<3) {
                tempCell.setBuilding(true);
                board.newCell(buildingPosition.getX(), buildingPosition.getY(), buildingPosition.getZ() + 1);
            }
            else tempCell.setDome(true);
        }

        SerializableUpdateBuild updateBuild = new SerializableUpdateBuild(buildingPosition, board.getCell(buildingPosition).isDome());
        SerializableUpdateActions serializableUpdateActions = new SerializableUpdateActions();
        serializableUpdateActions.addBuildAction(updateBuild);
        return serializableUpdateActions;
    }

}
