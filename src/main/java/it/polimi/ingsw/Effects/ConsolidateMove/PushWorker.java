package it.polimi.ingsw.Effects.ConsolidateMove;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Position;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PushWorker extends StandardConsolidateMove {
    public void moveInto(Board board, Cell workerCell, Cell destinationCell) {
        if (destinationCell.isFree())
            super.moveInto(board, workerCell, destinationCell);
        else {
            int temp_x = behindWorker_x(workerCell.getX(), destinationCell.getX());
            int temp_y = behindWorker_y(workerCell.getY(), workerCell.getY());
            int temp_z = board.getZoneLevel(temp_x, temp_y);
            //otteniamo la cella posta alle spalle del lavoratore nemico
            Cell behind_opposite_worker = board.getCell(temp_x, temp_y, temp_z);

            //sposto il lavoro avversario nella casella posta alle sue spalle
            super.moveInto(board, destinationCell, behind_opposite_worker);

            //sposto il mio player
            super.moveInto(board, workerCell, destinationCell);

        }
    }

    private int behindWorker_x(int myWorker_x, int opponentsWorker_x) {
        if (myWorker_x == opponentsWorker_x) {
            return opponentsWorker_x;
        } else if (myWorker_x > opponentsWorker_x) {
            return opponentsWorker_x - 1;
        } else
            return opponentsWorker_x + 1;
    }

    private int behindWorker_y(int myWorker_y, int opponentsWorker_y) {
        if (myWorker_y == opponentsWorker_y) {
            return opponentsWorker_y;
        } else if (myWorker_y > opponentsWorker_y) {
            return opponentsWorker_y - 1;
        } else
            return opponentsWorker_y + 1;
    }
}
