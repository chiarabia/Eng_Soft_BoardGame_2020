package it.polimi.ingsw.effects.consolidateBuild;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;

public class StandardConsolidateBuild {
    public void buildUp(Position buildingCell, Board board, boolean forceDome) {
        Cell tempCell = board.getCell(buildingCell);

        if (forceDome) { //costruisco una cupola a qualsiasi livello
            tempCell.setDome(true);
        }

        else {
            if (buildingCell.getZ()<3) { //Se la cella libera non è in cima, costruisco il building e la casella al di sopra diventa free
                tempCell.setBuilding(true);
                board.newCell(buildingCell.getX(), buildingCell.getY(), buildingCell.getZ() + 1);
            }
            else { //sono sulla sommità e quindi costruisco una cupola
                tempCell.setDome(true);
            }
        }
    }
}
