package it.polimi.ingsw.Effects.Move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;
import it.polimi.ingsw.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MoveButNoOnInitialPosition extends StandardMove{
    public MoveButNoOnInitialPosition(int moves) {
        super(moves);
    }

    @Override
    public Set<Cell> move(Cell workerCell, Board board, Turn turn) {
        if (!canImove(workerCell, turn)) return new HashSet<Cell>();
        else {
            Position initialposition = turn.getWorkerstartingposition();
            if (initialposition != null) { //Se mi sono già mosso, allora escludo la cella iniziale
                return super.move(workerCell, board, turn)
                        .stream()
                        .filter(a -> !a.getCellPosition().equals(initialposition))
                        .collect(Collectors.toSet());
            } else return super.move(workerCell, board, turn); //se non mi sono mosso, eseguo una mossa normale
        }
    }

    @Override
    protected boolean canImove(Cell workercell, Turn turn) {
        if (turn.getMove_times() != 0) return false; //posso muovermi due volte, ma di seguito (non posso muovermi/costruire/muvoermi)
        else {
            return super.canImove(workercell, turn);
        }
    }
}
