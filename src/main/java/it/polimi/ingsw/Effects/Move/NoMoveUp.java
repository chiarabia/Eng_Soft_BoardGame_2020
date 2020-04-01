package it.polimi.ingsw.Effects.Move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

//userei questa classe solo per gli avversari influenzati da Athena
public class NoMoveUp extends StandardMove {
    private StandardMove decoratedMove;

    public NoMoveUp(int moves) { //costruttore di default, da non usare, ma non è possibile farne a meno
        super(moves);
    }

    public NoMoveUp (StandardMove decoratedMove, int moves){ //costruttore per decorare un metodo
        super(moves);
        this.decoratedMove = decoratedMove;
    }


    @Override
    public Set<Cell> move(Cell workerCell, Board board, Turn turn) {
        if (!canImove(workerCell, turn)) return new HashSet<Cell>();
        else {
            Set<Cell> standardMove = decoratedMove.move(workerCell, board, turn);
            return standardMove.stream()
                    .filter(a -> a.getZ() <= workerCell.getZ())
                    .collect(Collectors.toSet());
        }
    }

    @Override
    protected int heightsDifference(int z_worker, int z_cell) {
        return decoratedMove.heightsDifference(z_worker, z_cell);
    }
}

