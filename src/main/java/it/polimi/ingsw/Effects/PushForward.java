package it.polimi.ingsw.Effects;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;

import java.util.Set;
import java.util.stream.Collectors;


public class PushForward extends StandardBuild{
    @Override
    public Set<Cell> move(Cell workerCell, Board board) {
        return board
                .getStream()
                .filter(a -> a.isFree()||a.isWorker())
                /* in questo metodo vanno bene lavoratori e caselle libere*/
                //TODO: Creare un metodo di Cell che restituisca l'id del Player del turno :D
                //Dobbiamo togleire i lavoratori del giocatore del turno in corso
                .filter(a -> a.getX()<=workerCell.getX()+1)
                .filter(a -> a.getX()>=workerCell.getX()-1)
                .filter(a -> a.getY()<=workerCell.getX()+1)
                .filter(a -> a.getY()>=workerCell.getX()-1)
                .filter( a -> heightsdifference(workerCell.getZ(), a.getZ()) <= 1)
                /*Filtriamo le caselle di lavoratori, ci vanno bene solo i lavoratori avversai che hanno una casella libera
                * alle loro spalle in cui possano essere spinti */
                .filter(a -> a.isFree() //non vogliamo perdere le normali caselle, quindi accettiamo quelle libere
                        || a.isWorker() //vogliamo i lavoratori per cui
                            && !board.completeTower //la cella alle loro spalle non è completa
                                //tx e ty sono metodi che calcolano la x e la y della casella alle spalle del lavoratoe
                                // se la casella alle spalle non esiste perch+ fuori dalla mappa il metodo completeTower resituisce
                                //true come se ci fosse una casella completa
                                    (tx(workerCell.getX(), a.getX()),
                                            ty(workerCell.getY(), a.getY())))
                .collect(Collectors.toSet());

    }
    private int ty (int yw, int ya) {
        if (yw == ya) {
            return ya;
        }
        else if(yw>ya) {
            return ya-1;
        }
        else
            return ya+1;
    }
    private int tx (int xw, int xa) {
        if (xw == xa) {
            return xa;
        }
        else if(xw>xa) {
            return xa-1;
        }
        else
            return xa+1;
    }
}
