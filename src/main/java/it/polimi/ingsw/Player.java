package it.polimi.ingsw;

/**
 *  This class defines the player
 *  <p> the player id will be a number from 1 to 3, in private final int id
 *  the Cell in which the workers are will be stored in private Cell workerCell[],
 *  with workerCell[0] the first worker and workerCell[1] the second worker
 */


public class Player {
    private final String name;
    private final int id;
    private Cell workerCell[];

    public Player (String name, int id){
        this.id = id;
        this.name = name;
        workerCell = new Cell[2];
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Cell getWorkerCell(int whichWorker){
        return workerCell[whichWorker - 1];
    }

    /**
     * Upgrades the value in workerCell[] after a movement
     *
     * <p>If workerCell exist the worker is deleted from that Cell.
     * A new workerCell with the worker of the right Player is then created.
     * @param cell the new Cell
     * @param whichWorker 1 for worker one and 2 for worker two
     */

    public void newWorkerCell(Cell cell, int whichWorker){
        if (workerCell[whichWorker - 1]!=null) workerCell[whichWorker - 1].setWorker(false);
        setWorkerCell(cell, whichWorker);
    }

    public void setWorkerCell(Cell cell, int whichWorker){
        workerCell[whichWorker - 1] = cell;
        workerCell[whichWorker - 1].setWorker(true);
        workerCell[whichWorker - 1].setPlayer(this);
    }

    //Removes the workers from the Cells
    public void removeWorkerCells(){
        workerCell[0].setWorker(false);
        workerCell[1].setWorker(false);
    }

}
