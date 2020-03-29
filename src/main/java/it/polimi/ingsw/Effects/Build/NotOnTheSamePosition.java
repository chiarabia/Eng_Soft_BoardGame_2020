package it.polimi.ingsw.Effects.Build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BuildAgainButNotIntheSamePosition extends StandardBuild{
    public BuildAgainButNotIntheSamePosition(int builds) {
        super(builds);
    }

    @Override
    public Set<Cell> build(Cell workerCell, Board board, Turn turn) {
        if (!canIbuild(workerCell, turn))
            return new HashSet<>();
        else
            if(turn.getBuild_times()>0) {
                return board.getStream()
                        .filter(a -> a.isFree())
                        .filter(a -> !a.getCellPosition().equals(turn.getFirstbuildingposition()))
                        .filter(a -> a.getX() <= workerCell.getX() + 1)
                        .filter(a -> a.getX() >= workerCell.getX() - 1)
                        .filter(a -> a.getY() <= workerCell.getY() + 1)
                        .filter(a -> a.getY() >= workerCell.getY() - 1)
                        .collect(Collectors.toSet());
            }
            else
                return super.build(workerCell, board, turn);
            }
    }
