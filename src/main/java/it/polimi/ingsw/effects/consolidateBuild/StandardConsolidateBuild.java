package it.polimi.ingsw.effects.consolidateBuild;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.server.serializable.SerializableUpdateBuild;
import it.polimi.ingsw.server.serializable.SerializableUpdateInfos;
import it.polimi.ingsw.server.serializable.SerializableUpdateMove;

public class StandardConsolidateBuild {
    public SerializableUpdateInfos buildUp(Position buildingPosition, Board board, boolean forceDome) {
        Cell tempCell = board.getCell(buildingPosition);

        if (forceDome) { //costruisco una cupola a qualsiasi livello
            tempCell.setDome(true);
        }

        else {
            if (buildingPosition.getZ()<3) { //Se la cella libera non è in cima, costruisco il building e la casella al di sopra diventa free
                tempCell.setBuilding(true);
                board.newCell(buildingPosition.getX(), buildingPosition.getY(), buildingPosition.getZ() + 1);
            }
            else { //sono sulla sommità e quindi costruisco una cupola
                tempCell.setDome(true);
            }
        }
        SerializableUpdateBuild updateBuild = new SerializableUpdateBuild(buildingPosition,
                board.getCell(buildingPosition)
                        .isDome());
        SerializableUpdateInfos serializableUpdateInfos = new SerializableUpdateInfos();
        serializableUpdateInfos.addBuildAction(updateBuild);
        return serializableUpdateInfos;
    }

}
