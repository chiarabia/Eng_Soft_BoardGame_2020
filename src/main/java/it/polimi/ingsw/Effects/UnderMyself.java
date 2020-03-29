package it.polimi.ingsw.Effects;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Effects.Build.StandardBuild;
import it.polimi.ingsw.Turn;
import java.util.Set;

public class UnderMyself extends StandardBuild {
    public Set<Cell> build(Cell workerCell, Board board, Turn turn) {
        Set <Cell> standardBuild = super.build(workerCell, board, turn);
        if (workerCell.getZ()<3) standardBuild.add(workerCell);
        return standardBuild;
    }
}
