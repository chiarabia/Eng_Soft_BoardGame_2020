package it.polimi.ingsw;

import java.util.Objects;
import java.util.stream.Stream;

public class Cell {
    private final Position cellPosition;
    private boolean worker;
    private boolean free;
    private boolean building;
    private boolean dome;
    private final Player player;

    public Cell(int x, int y, int z) {
        cellPosition = new Position(x, y, z);
        worker = false;
        free = true;
        building = false;
        dome = false;
        player = null;
    }

    public Position getCellPosition() {
        return cellPosition;
    }

    public boolean isWorker() {
        return worker;
    }

    public boolean isFree() {
        return free;
    }

    public boolean isBuilding() {
        return building;
    }

    public boolean isDome() {
        return dome;
    }

    public boolean isPerimetral() {
        if (getY()==4 || getY()==0 ||getX() == 0 || getX() == 4)
            return true;
        else
            return false;
    }

    public Player getPlayer() {
        return player;
    }

    public void setWorker(boolean worker) {
        this.worker = worker;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public void setBuilding(boolean building) {
        this.building = building;
    }

    public void setDome(boolean dome) {
        this.dome = dome;
    }

    public int getX() {
        return cellPosition.getX();
    }
    public int getY() {
        return cellPosition.getX();
    }
    public int getZ() {
        return cellPosition.getX();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return //isWorker() == cell.isWorker() &&
               //isFree() == cell.isFree() &&
                //isBuilding() == cell.isBuilding() &&
                //isDome() == cell.isDome() &&
                getCellPosition().equals(cell.getCellPosition()); //&&
                //getPlayer().equals(cell.getPlayer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCellPosition(), isWorker(), isFree(), isBuilding(), isDome(), getPlayer());
    }
}
