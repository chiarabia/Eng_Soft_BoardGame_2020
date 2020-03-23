package it.polimi.ingsw.Effects;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Board;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//Questo è il metodo principale per sapere quali sono le caselle attorno ad un lavoratore disponibili per uno spostamento

public class StandardBuild {
    public Set<Cell> move (Cell workerCell, Board board) {
        return  board.getStream()
                /*(Questo metodo serve poichè l'effettiva rappresentazione della tabella é private e non deve
                essere mai esposta, con questo metodo chiedo alla Board di darmi uno stream della sua rappresentazione. Lo
                 stream è una copia degli elementi della rappresentazione interna, e (se ben ricordo) non dovrebbe modificare nulla
                 nella Board)*/
                .filter(Cell::isFree) //filtro le caselle libere
                .filter(a -> a.getX()<=workerCell.getX()+1)
                .filter(a -> a.getX()>=workerCell.getX()-1)
                .filter(a -> a.getY()<=workerCell.getX()+1)
                .filter(a -> a.getY()>=workerCell.getX()-1) //Filtro tutte le caselle attorno, al massimo le 8 colonne
                //attorno al mio lavoratore
                .filter( a -> heightsdifference(workerCell.getZ(), a.getZ()) <= 1) //riduco le caselle in cui posso spostarmi,
                //devono essere raggiungibili in altezza
                .collect(Collectors.toSet());
    }

    protected int heightsdifference (int z_worker, int z_cell)  { //Metodo "protected" perchè mi potrebbe servire in
        //figlie di questo metodo
        final int i = z_cell - z_worker;
        return i;
    }
}

