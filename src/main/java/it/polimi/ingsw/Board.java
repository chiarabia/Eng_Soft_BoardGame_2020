package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class sets the board of the game.
 */

public class Board {
    private List<Cell> board = new ArrayList<>();

    /**
     * Constructs the board as a 5x5 table. Each cell has a Cell object
     * with coordinates xyz.
     */
    public Board() {
        int x, y;
        for (x = 0; x < 5; x++)
            for (y = 0; y < 5; y++) board.add(new Cell(x, y, 0));
    }

    public Stream<Cell> getStream() {
        return board.stream();
    }

    public Cell getCell(int x, int y, int z) { //metodo per ottenere la Cell sapendo le coordinate
        Position temp_position = new Position(x, y, z);

        for (int i = 0; i < board.size(); i++) {
            if (board.get(i).getCellPosition().equals(temp_position))
                return board.get(i);
        }

        return null;
    }

    public Cell getCell (Position position) {
        return getCell(position.getX(), position.getY(), position.getY());
    }

    public void newCell(int x, int y, int z) { //Per aggiungere nuove celle
        if (getCell(x, y, z) == null) //evito di creare duplicati
            return;
        else {
            Cell temp_cell = new Cell(x, y, z);
            board.add(temp_cell);
        }
    }

    public int getZoneLevel(int x, int y) {
        return getStream()
                .filter(a -> a.getX() == x && a.getY() == y)
                .mapToInt(e -> 1)
                .reduce(-1, (a, b) -> a + b); //Ottengo la massima z di una colonna
    }

    /**
     * This method tells us if a Cell is free
     *
     * @param x coordinate x of the Cell
     * @param y coordinate y of the Cell
     * @return true if the Cell is free, false otherwise
     */
    public boolean isFreeZone(int x, int y) {
        if (x < 0 || y < 0) return false; //the Cell is outside the limits of the board

        //counting the elements present in the stream in the selected Cell
        long count = board.stream()
                .filter(a -> a.getX() == x && a.getY() == y)
                .filter(Cell::isFree) //the cell has no domes
                .count();
        return !(count == 0.0);
    }

    public List<Cell> getWorkers() {
        return board.stream()
                .filter(Cell::isWorker)
                .collect(Collectors.toList());
    }

    public List<Cell> getPlayerWorkers(Player player) {
        return board.stream()
                .filter(Cell::isWorker)
                .filter(a -> a.getPlayerID() == player.getId())
                .collect(Collectors.toList());
    }

    public List<Cell> getPlayerWorkers(int player_id) {
        return board.stream()
                .filter(Cell::isWorker)
                .filter(a -> a.getPlayerID() == player_id)
                .collect(Collectors.toList());
    }

    public Cell getPlayerWorker(Player player, int worker_id) {
        for (int i = 0; i < board.size(); i++) {
            if (board.get(i).isWorker()
                    && board.get(i).getPlayerID() == player.getId()
                        && (board.get(i).getWorkerId() == worker_id))
                return board.get(i);

        }
        return null;
    }

}