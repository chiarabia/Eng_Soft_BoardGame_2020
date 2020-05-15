package it.polimi.ingsw.effects.consolidateBuild;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;

public class UnderWorker extends StandardConsolidateBuild {

    @Override
    public void buildUp(Position buildingCell, Board board, boolean forceDome) {
        Cell tempBuildingCell = board.getCell(buildingCell);

        if (tempBuildingCell.isWorker()) {
            board.newCell(buildingCell.getX(), buildingCell.getY(), buildingCell.getZ() + 1);
            Cell workerDestination = board.getCell(buildingCell.getX(), buildingCell.getY(), buildingCell.getZ() + 1); //new Worker Position

            workerDestination.setWorker(tempBuildingCell.getWorker());
            tempBuildingCell.setWorker(null);
            tempBuildingCell.setBuilding(true);
        }

        else
            super.buildUp(buildingCell, board, forceDome);
    }
}
