package it.polimi.ingsw.Effects;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;
import java.util.Set;
import java.util.stream.Collectors;

public class NoMoveUp extends StandardMove {
    private StandardMove decoratedMove;
    @Override
    public Set<Cell> move(Cell workerCell, Board board, Turn turn) {
        //todo: per ora questo decorator rende sempre impossibile salire,
        // bisogna implementare Turn e poi si potrà rendere il comportamento variabile

        Set <Cell> standardMove = decoratedMove.move(workerCell, board, turn);
        return standardMove.stream().filter(a->a.getZ()<=workerCell.getZ()).collect(Collectors.toSet());
    }

    @Override
    protected int heightsDifference(int z_worker, int z_cell) {
        return decoratedMove.heightsDifference(z_worker, z_cell);
    }

    public NoMoveUp (StandardMove decoratedMove){
        this.decoratedMove = decoratedMove;
    }
}
