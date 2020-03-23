package it.polimi.ingsw.Effects;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;

import java.util.Set;
import java.util.stream.Stream;

public class UnlimitedPerimetralMove extends StandardMove  {


    public Set<Cell> unlimitedMove(Cell workerCell, Board board) {
        return super.move(workerCell, board);
    }
}