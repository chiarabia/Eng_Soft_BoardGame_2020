package it.polimi.ingsw;


import java.util.Objects;

public class Cell {
    private final Position cellPosition;
    private boolean worker;
    private boolean building;
    private boolean dome;
    private Player player;

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

    public boolean isFree() {
        return !(worker || building || dome);
    }

    public boolean isBuilding() {
        return building;
    }

    public boolean isDome() {
        return dome;
    }

    public boolean isPerimetral() { //Questo metodo verifica se la cella si trova sul perimetro
        return (getY()==4 || getY()==0 ||getX() == 0 || getX() == 4);
    }

    public Player getPlayer(){return player;}

    /* Una serie di setter poichè tutti gli attributi tranne la posizione possono essere modificati*/

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

    /* Metodi per avere la posizione della cella e id,
     non sono necessari ma velocizzano la scrittura di altri metodi */

    public int getX() {
        return cellPosition.getX();
    }
    public int getY() {
        return cellPosition.getY();
    }
    public int getZ() {
        return cellPosition.getZ();
    }

    public int getWorkerID ()  {
        if (!isWorker()) return 0;
        return player.getId();
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
        return getCellPosition().equals(cell.getCellPosition());
    }
}
