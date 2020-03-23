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

    public boolean isPerimetral() { //Questo metodo verifica se la cella si trova sul perimetro
        if (getY()==4 || getY()==0 ||getX() == 0 || getX() == 4)
            return true;
        else
            return false;
    }

    public Player getPlayer() {
        return player;
    } //Todo: Probabilmente basta salvarsi l'identificativo del player e non un riferimento a Player

    /* Una serie di setter poichè tutti gli attributi tranne la posizione possono essere modificati*/
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

    /* Metodi per avere la posizione della cella,
     non sono necessari ma velocizzano la scrittura di altri metodi
     */
    public int getX() {
        return cellPosition.getX();
    }
    public int getY() {
        return cellPosition.getX();
    }
    public int getZ() {
        return cellPosition.getX();
    }

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
