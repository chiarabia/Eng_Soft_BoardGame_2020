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

    //getter for the Cell from a known position
    public Cell getCell(int x, int y, int z) {
        Position temp_position = new Position(x, y, z);

        for (int i = 0; i < board.size(); i++) {
            if (board.get(i).getPosition().equals(temp_position))
                return board.get(i);
        }

        return null;
    }

    public Cell getCell (Position position) {
        return getCell(position.getX(), position.getY(), position.getY());
    }


    //This method adds new Cells
    public void newCell(int x, int y, int z) {
        //check for duplicates
        if (getCell(x, y, z) != null)
            return;
        else {
            Cell temp_cell = new Cell(x, y, z);
            board.add(temp_cell);
        }
    }

    /**
     * @param x coordinate x of a cell
     * @param y coordinate y of a cell
     * @return z coordinate that represents the building height
     */

    public int getZoneLevel(int x, int y) {
        return getStream()
                .filter(a -> a.getX() == x && a.getY() == y)
                .mapToInt(e -> 1)
                .reduce(-1, (a, b) -> a + b);
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
        for (int i = 0; i < board.size(); i++) {
            if (board.get(i).isWorker()
                    && board.get(i).getPlayerId() == player.getId()
                        && (board.get(i).getWorkerId() == workerId))
                return board.get(i);
        }
        return null;
    }
}