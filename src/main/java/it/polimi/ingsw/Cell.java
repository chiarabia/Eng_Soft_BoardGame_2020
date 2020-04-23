package it.polimi.ingsw;

import java.util.Objects;

/**
 * This class creates the Cell for the board
 */

public class Cell {
    private final Position position;
    private boolean building;
    private boolean dome;
    private Worker worker;

    /**
     * Constructor for the Cell
     *
     * <p>The cell starts without a worker, a building, a player or a dome
     *
     * @param x coordinate x of the Cell
     * @param y coordinate y of the Cell
     * @param z coordinate z of the Cell
     */
    public Cell(int x, int y, int z) {
        position = new Position(x, y, z);
        worker = null;
        building = false;
        dome = false;
    }


    /**
     * This method checks if the Cell is occupied by a worker, a building or a dome
     * @return true if the Cell is free, false otherwise
     */

    public boolean isFree() {
        return !(worker!=null || building || dome);
    }
    public boolean isBuilding() {
        return building;
    }
    public boolean isDome() { return dome; }
    public boolean isWorker() { return worker!=null; }

    /**
     * This method checks if the Cell is on the perimeter of the board
     * @return true if the Cell is on the perimeter, false otherwise
     */

    public boolean isPerimetral() {
        return (getY()==4 || getY()==0 ||getX() == 0 || getX() == 4);
    }

    //getters for worker, player and their IDs
    public Worker getWorker(){return worker;}
    public Player getPlayer(){return worker.getPlayer();}
    public int getWorkerId() {return worker.getWorkerId();}
    public int getPlayerId() {return worker.getPlayerId();}

    //Setters
    public void setWorker(Worker worker) {
        this.worker = worker;
    }
    public void setBuilding(boolean building) {
        this.building = building;
    }
    public void setDome(boolean dome) {
        this.dome = dome;
    }

    //Getters for coordinates
    public int getX() {
        return position.getX();
    }
    public int getY() {
        return position.getY();
    }
    public int getZ() {
        return position.getZ();
    }
    public Position getPosition() {return position; }


    /* Ridefinisco Equals guardando solo alla posizione, gli altri attributi non li
    guardo anche perchè noi dobbiamo essere sicuri di non avere duplicati. Il set va a vedere se due
    oggetti sono dublicati attraverso questo metodo. Se mettessi anche gli altri campi,
     che non sono final rischierei di avere duplicati o magari più versioni della stessa cella con campi modificati.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return getPosition().equals(cell.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition());
    }
}
