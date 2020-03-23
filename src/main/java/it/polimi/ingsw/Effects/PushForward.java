package it.polimi.ingsw.Effects;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;

import java.util.Set;
import java.util.stream.Collectors;


public class PushForward extends StandardMove{
    @Override
    public Set<Cell> move(Cell workerCell, Board board) {
        return board
                .getStream()
                .filter(a -> a.isFree()||a.isWorker())
                /* in questo metodo vanno bene lavoratori e caselle libere*/
                //TODO: Creare un metodo di Cell che restituisca l'id del Player del turno :D
                //Dobbiamo togliere i lavoratori del giocatore del turno in corso
                .filter(a -> a.getX()<=workerCell.getX()+1)
                .filter(a -> a.getX()>=workerCell.getX()-1)
                .filter(a -> a.getY()<=workerCell.getY()+1)
                .filter(a -> a.getY()>=workerCell.getY()-1)
                .filter( a -> heightsDifference(workerCell.getZ(), a.getZ()) <= 1)
                /*Filtriamo le caselle di lavoratori, ci vanno bene solo i lavoratori avversai che hanno una casella libera
                * alle loro spalle in cui possano essere spinti */
                .filter(a -> a.isFree() //non vogliamo perdere le normali caselle, quindi accettiamo quelle libere
                        || a.isWorker() //vogliamo i lavoratori per cui
                            && !board.completeTower //la cella alle loro spalle non è completa
                                //tx e ty sono metodi che calcolano la x e la y della casella alle spalle del lavoratoe
                                // se la casella alle spalle non esiste perchè fuori dalla mappa il metodo completeTower resituisce
                                //true come se ci fosse una casella completa
                                    (behindWorker_x(workerCell.getX(), a.getX()),
                                            behindWorker_y(workerCell.getY(), a.getY())))
                .collect(Collectors.toSet());

    }
    private int behindWorker_y (int myWorker_y, int opponentsWorker_y) {
        if (myWorker_y == opponentsWorker_y) {
            return opponentsWorker_y;
        }
        else if(myWorker_y>opponentsWorker_y) {
            return opponentsWorker_y-1;
        }
        else
            return opponentsWorker_y+1;
    }
    private int behindWorker_x (int myWorker_x, int opponentsWorker_x) {
        if (myWorker_x == opponentsWorker_x) {
            return opponentsWorker_x;
        }
        else if(myWorker_x>opponentsWorker_x) {
            return opponentsWorker_x-1;
        }
        else
            return opponentsWorker_x+1;
    }
}
