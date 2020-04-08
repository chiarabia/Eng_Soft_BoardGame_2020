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
    private Worker workers[];

    public Player (String name, int id){
        this.id = id;
        this.name = name;
        workers = new Worker[2];
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Worker getWorker(int whichWorker){
        return workers[whichWorker];
    }
}
