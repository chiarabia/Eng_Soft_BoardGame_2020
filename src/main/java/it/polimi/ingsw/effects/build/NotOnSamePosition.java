package it.polimi.ingsw.effects.build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class NotOnSamePosition extends StandardBuild{
    public NotOnSamePosition(int builds) {
        super(builds);
    }

    @Override
    public Set<Position> build(Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);

        if (!checkBuildConditions(workerCell, turn))
            return new HashSet<>();
        else
            if(turn.getBuildTimes()>0) {
                return board.getStream()
                        .filter(a -> a.isFree())
                        .filter(a -> !a.getPosition().equals(turn.getFirstBuildingPosition()))
                        .filter(a -> !(a.getX()==turn.getFirstBuildingPosition().getX()&&
                                a.getY()==turn.getFirstBuildingPosition().getY()))
                        .filter(a -> a.getX() <= workerCell.getX() + 1)
                        .filter(a -> a.getX() >= workerCell.getX() - 1)
                        .filter(a -> a.getY() <= workerCell.getY() + 1)
                        .filter(a -> a.getY() >= workerCell.getY() - 1)
                        .map(a->a.getPosition())
                        .collect(Collectors.toSet());
            }
            else
                return super.build(workerPosition, board, turn);
            }
    }
