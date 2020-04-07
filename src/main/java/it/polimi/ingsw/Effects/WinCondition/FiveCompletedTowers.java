package it.polimi.ingsw.Effects.WinCondition;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;

public class FiveCompletedTowers extends StandardWinCondition {
    public boolean win (Cell workerCell, Cell destinationCell, Board board) {
        return board.getStream().filter(x->x.getZ()==3)
                                .filter(x->x.isDome())
                                .count() >= 5.0;
    }
}
