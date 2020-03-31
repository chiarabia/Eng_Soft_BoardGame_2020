package it.polimi.ingsw.Effects.Build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Effects.Build.StandardBuild;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class NoBuildOnPerimeter extends StandardBuild {
    public Set<Cell> build(Cell workerCell, Board board, Turn turn) {
        Set<Cell> temp_cells = super.build(workerCell, board, turn);

        if(turn.getBuild_times()>0)
            return temp_cells.stream()
                            .filter(a->!a.isPerimetral())
                            .collect(Collectors.toSet());
        else return temp_cells;
    }

    public NoBuildOnPerimeter(int builds) {
        super(builds);
    }
}
