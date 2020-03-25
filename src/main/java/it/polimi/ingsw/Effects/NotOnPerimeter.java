package it.polimi.ingsw.Effects;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Turn;
import java.util.Set;
import java.util.stream.Collectors;

public class NotOnPerimeter extends StandardBuild {
    public Set<Cell> build(Cell workerCell, Board board, Turn turn) {
        // todo: questo codice non verifica se sia o meno la prima costruzione del turno, Turn va definito e poi gestito
        return super.build(workerCell, board, turn).stream()
                .filter(a->(a.getX()>0 && a.getX()<4 && a.getY()>0 && a.getY()<4))
                .collect(Collectors.toSet());
    }
}
