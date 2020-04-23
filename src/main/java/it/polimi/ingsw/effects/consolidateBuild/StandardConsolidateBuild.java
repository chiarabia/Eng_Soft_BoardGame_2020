package it.polimi.ingsw.effects.consolidateBuild;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;

public class StandardConsolidateBuild {
    public void BuildUp (Position buildingCell, Board board, boolean god_power) {
        if (god_power) { //Se god_power attivato, costruisco una cupola a qualsiasi livello
            Cell temp_cell = board.getCell(buildingCell);
            temp_cell.setDome(true);
        }

        else {
            Cell temp_cell = board.getCell(buildingCell);
            if(buildingCell.getZ()<3) { //Se la cella libera non è in cima, costruisco il building e la casella al di sopra diventa free
                temp_cell.setBuilding(true);
                board.newCell(buildingCell.getX(), buildingCell.getY(), buildingCell.getZ() + 1);
            }
            else { //sono sulla sommità e quindi costruisco una cupola
                temp_cell.setDome(true);
            }
        }
    }
}
