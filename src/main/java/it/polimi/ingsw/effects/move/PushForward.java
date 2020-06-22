package it.polimi.ingsw.effects.move;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class PushForward extends StandardMove{
    public PushForward(int moves) {
        super(moves);
    }

    @Override
    public Set<Position> move (Position workerPosition, Board board, Turn turn) {
        Cell workerCell = board.getCell(workerPosition);
        if (!checkMoveConditions(workerCell, turn))
            return new HashSet<Position>();
        else{
            return board
                .getStream()
                .filter(a -> a.isFree() || a.isWorker())
                /* in questo metodo vanno bene lavoratori e caselle libere*/
                //Dobbiamo togliere i lavoratori del giocatore del turno in corso
                .filter(a -> a.getX() <= workerCell.getX() + 1)
                .filter(a -> a.getX() >= workerCell.getX() - 1)
                .filter(a -> a.getY() <= workerCell.getY() + 1)
                .filter(a -> a.getY() >= workerCell.getY() - 1)
                .filter(a -> heightsDifference(workerCell.getZ(), a.getZ()) <= 1)
                /*Filtriamo le caselle di lavoratori, ci vanno bene solo i lavoratori avversari che hanno una casella libera
                alle loro spalle in cui possano essere spinti */
                .filter(a -> a.isFree()
                        || (a.isWorker()
                        && a.getPlayerId() != workerCell.getPlayerId() // specifica che il lavoratore deve essere avversario
                        //&& !a.isPerimetral()
                        && board.isFreeZone
                        (behindWorker_x(workerCell.getX(), a.getX()),
                                behindWorker_y(workerCell.getY(), a.getY()))))
                    .map(Cell::getPosition)
                .collect(Collectors.toSet());
        }
    }

     protected int behindWorker_x (int myWorker_x, int opponentsWorker_x) {
        if (myWorker_x == opponentsWorker_x) {
            return opponentsWorker_x;
        }
        else if(myWorker_x>opponentsWorker_x) {
            return opponentsWorker_x-1;
        }
        else
            return opponentsWorker_x+1;
    }
    protected int behindWorker_y (int myWorker_y, int opponentsWorker_y) {
        if (myWorker_y == opponentsWorker_y) {
            return opponentsWorker_y;
        }
        else if(myWorker_y>opponentsWorker_y) {
            return opponentsWorker_y-1;
        }
        else
            return opponentsWorker_y+1;
    }




}
