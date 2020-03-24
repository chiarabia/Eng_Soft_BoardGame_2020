package it.polimi.ingsw;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    private Set<Cell> board;

    public Board() {
        int x = 0, y = 0;
        for (x = 0; x<5; x++)
            for(y = 0; y<5; y++) board.add(new Cell(x, y, 0));
    }

    public Stream<Cell> getStream() {
        return board.stream();
    }

    public int getZoneLevel(int x, int y){ // restituisce l'ultimo livello occupato
        Set <Cell> streamZone = board.stream().filter(a->a.getX()==x)
                                              .filter(a->a.getY()==y)
                                              .collect(Collectors.toSet());
        Set <Cell> streamDome = streamZone.stream().filter(Cell::isDome)
                                          .collect(Collectors.toSet());
        Set <Cell> streamBuilding = streamZone.stream().filter(Cell::isBuilding)
                                              .collect(Collectors.toSet());
        if (streamDome.size()==1) return streamDome.iterator().next().getZ();
        int maxHeight = -1;
        for (Cell i: streamBuilding) if (i.getZ() > maxHeight) maxHeight = i.getZ();
        return maxHeight;
    }

    public boolean isFreeZone(int x, int y) {
            if (x <0 || y <0) return false;
            long count = board.stream()
                    .filter(a -> a.getX() == x && a.getY() == y)
                    .filter(Cell::isFree) //per costruzione degli altri metodi, se metto una cupola non ci possono essere caselle libere nella colonna
                    .count();
            return !(count == 0.0);
    }
}