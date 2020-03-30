package it.polimi.ingsw;

import java.util.Objects;

/**
 * This class creates the Cell for the board
 */

public class Cell {
    private final Position cellPosition;
    private boolean worker;
    private boolean building;
    private boolean dome;
    private Player player;
    private int workerId;

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
        cellPosition = new Position(x, y, z);
        worker = false;
        building = false;
        dome = false;
        player = null;
    }

    public Position getCellPosition() {return cellPosition; }

    public boolean isWorker() {
        return worker;
    }

    /**
     * This method checks if the Cell is occupied by a worker, a building or a dome
     * @return true if the Cell is free, false otherwise
     */

    public boolean isFree() {
        return !(worker || building || dome);
    }

    public boolean isBuilding() {
        return building;
    }

    public boolean isDome() {
        return dome;
    }

    /**
     * This method checks if the Cell is on the perimeter of the board
     * @return true if the Cell is on the perimeter, false otherwise
     */

    public boolean isPerimetral() {
        return (getY()==4 || getY()==0 ||getX() == 0 || getX() == 4);
    }

    public Player getPlayer(){return player;}

    public int getWorkerId() {return workerId;}

    //Setters

    public void setPlayer(Player player){this.player = player;}

    public void setWorker(boolean worker) {
        this.worker = worker;
    }

    public void setBuilding(boolean building) {
        this.building = building;
    }

    public void setDome(boolean dome) {
        this.dome = dome;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    //Getters for coordinates

    public int getX() {
        return cellPosition.getX();
    }
    public int getY() {
        return cellPosition.getY();
    }
    public int getZ() {
        return cellPosition.getZ();
    }

    /**
     * This method gives us the Player Id
     * @return 0 if there is no worker, the Id of the worker's Player if a worker is present
     */
    public int getPlayerID ()  {
        if (!isWorker()) return 0;
        return player.getId();
    }

    /**
     *
     * @param o
     * @return
     */

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
        return getCellPosition().equals(cell.getCellPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCellPosition());
    }
}
