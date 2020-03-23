package it.polimi.ingsw;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    private Set<Cell> board;

    public Board() {
        int x = 0;
        int y = 0;
        for (x = 0; x<5; x++) {
            for(y = 0; y<5; y++){
                board.add(new Cell(x, y, 0));
            }
        }
    }
    public Stream<Cell> getStream() {
        return board.stream();
    }

    public boolean completeTower(int x, int y) {
        /* this method checks if a tower is complete
         */
            if (x <0 || y <0)
                return false;
            long count = board.stream()
                    .filter(a -> a.getX() == x && a.getY() == y)
                    .filter(Cell::isFree) //per costruzione degli altri metodi, se metto una cupola non ci possono essere caselle libere nella colonna
                    .count();
            if (count == 0.0)
                return true;
            else
                return false;

        }
    }
