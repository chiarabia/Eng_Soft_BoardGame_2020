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
                //TODO: Creare un metodo di Cell che restituisca l'id del Player del turno :D
                //togliere gli operai dello stesso player :D
                .filter(a -> a.getCellPosition().getX()<=workerCell.getCellPosition().getX()+1)
                .filter(a -> a.getCellPosition().getX()>=workerCell.getCellPosition().getX()-1)
                .filter(a -> a.getCellPosition().getY()<=workerCell.getCellPosition().getX()+1)
                .filter(a -> a.getCellPosition().getY()>=workerCell.getCellPosition().getX()-1)
                .filter( a -> diffheight(workerCell.getZ(), a.getZ()) <= 1)
                .filter(a -> a.isFree()
                        || a.isWorker()
                            && !board.completeTower
                                    (tx(workerCell.getX(), a.getX()),ty(workerCell.getY(), a.getY())))
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
