package it.polimi.ingsw.Effects.Build;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class defines
 */


public class BuildOnTopYourFirstBlock extends StandardBuild {
    public BuildOnTopYourFirstBlock(int builds) {
        super(builds);
    }

    @Override
    public Set<Cell> build(Cell workerCell, Board board, Turn turn) {
        Position first_block = turn.getFirstBuildingPosition();
        if (turn.getBuild_times()>0) {
            if (!checkBuildConditions(workerCell, turn))
                return new HashSet<>();
            else
                return board.getStream()
                        .filter(a -> a.isFree())
                        .filter(a-> a.getX() == (first_block.getX())) //Posso costruire solo al di sopra del blocco costruito
                        .filter(a-> a.getY() == (first_block.getY()))
                        .filter(a->a.getZ() <= 2) //Non può essere una cupola
                        .collect(Collectors.toSet());
        }
        else
            return super.build(workerCell, board, turn);
    }
}
