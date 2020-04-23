package it.polimi.ingsw.effects.build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;

public class UnderMyself extends StandardBuild {
    public Set<Position> build(Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);
        if (!checkBuildConditions(workerCell, turn)) return new HashSet<Position>();
        else {
            Set<Position> standardBuild = super.build(workerPosition, board, turn);
            if (workerCell.getZ() < 3) standardBuild.add(workerPosition);
            return standardBuild;
        }
    }

    public UnderMyself(int builds) {
        super(builds);
    }
}
