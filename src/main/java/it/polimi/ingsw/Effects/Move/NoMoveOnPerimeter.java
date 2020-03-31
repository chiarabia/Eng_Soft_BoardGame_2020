package it.polimi.ingsw.Effects.Move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;

import java.util.Set;

public class NoMoveOnPerimeter extends StandardMove {
    public NoMoveOnPerimeter(int moves) { //per convenzione metterei 1, tanto basta che possa muoversi almeno una volta il player per non perdere il turno
        super(moves);
    }

    @Override
    public Set<Cell> move(Cell workerCell, Board board, Turn turn) {
        return super.move(workerCell, board, turn);
    }

    @Override
    protected boolean canImove(Cell workercell, Turn turn) {
        if (!workercell.isWorker()) //robusto, devo invocare il metodo su un worker
            return false;

        if (workercell.getPlayerID() != turn.getPlayer_id()) //il player deve essere uguale
            return false;

        if (turn.getWorkerused() == 0) //nessun worker è stato mosso, tutti i worker devorebbero potersi muovere
            return (turn.getMove_times() < moves);

        if (workercell.getWorkerId() != turn.getWorkerused()) //if the id doesn't match, false
            return false;

        else {
            if (workercell.isPerimetral())
                return true;
            else
                return (turn.getMove_times() < moves);
        }
    }
}
