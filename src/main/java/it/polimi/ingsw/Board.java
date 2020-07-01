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
     * with coordinates xy.
     */
    public Board() {
        int x, y;
        for (x = 0; x < 5; x++)
            for (y = 0; y < 5; y++) board.add(new Cell(x, y, 0));
    }

    public Stream<Cell> getStream() {
        return board.stream();
    }

    //getter for the Cell from a known position
    public Cell getCell(int x, int y, int z) {
        Position temp_position = new Position(x, y, z);

        for (Cell cell : board) {
            if (cell.getPosition().equals(temp_position))
                return cell;
        }

        return null;
    }

    public Cell getCell (Position position) {
        return getCell(position.getX(), position.getY(), position.getZ());
    }


    /**
     * Adds new Cells
     * @param x
     * @param y
     * @param z
     */
    public void newCell(int x, int y, int z) {
        //check for duplicates
        if (getCell(x, y, z) == null) {
            Cell temp_cell = new Cell(x, y, z);
            board.add(temp_cell);
        }
    }

    /**
     * @param x coordinate x of a zone
     * @param y coordinate y of a zone
     * @return z coordinate that represents the building height
     */

    public int getZoneLevel(int x, int y) {
        return getStream()
                .filter(a -> a.getX() == x && a.getY() == y)
                .mapToInt(e -> 1)
                .reduce(-1, Integer::sum);
    }

    /**
     * Checks if a zone is free
     *
     * @param x coordinate x of the zone
     * @param y coordinate y of the zone
     * @return true if the zone is free, false otherwise
     */
    public boolean isFreeZone(int x, int y) {
        if (x < 0 || y < 0 || x > 4 || y > 4) return false; //the Cell is outside the limits of the board

        //counting the elements present in the stream in the selected Cell
        long count = board.stream()
                .filter(a -> a.getX() == x && a.getY() == y)
                .filter(Cell::isFree) //the cell has no domes
                .count();
        return !(count == 0.0);
    }


    //Getters

    public List<Cell> getWorkerCells() {
        return board.stream()
                .filter(Cell::isWorker)
                .collect(Collectors.toList());
    }

    public List<Cell> getWorkerCells(Player player) {
        return board.stream()
                .filter(Cell::isWorker)
                .filter(a -> a.getPlayerId() == player.getId())
                .collect(Collectors.toList());
    }

    public Cell getWorkerCell (Player player, int workerId) {
        for (Cell cell : board) {
            if (cell.isWorker()
                    && cell.getPlayerId() == player.getId()
                    && (cell.getWorkerId() == workerId))
                return cell;
        }
        return null;
    }
}