package it.polimi.ingsw;

import java.util.ArrayList;

/**
 *  This class defines the player
 *  <p> the player id will be a number from 1 to 3, in private final int id
 *  the Cell in which the workers are will be stored in private Cell workerCell[],
 *  with workerCell[0] the first worker and workerCell[1] the second worker
 */


public class Player {
    private final String name;
    private final int id;
    private ArrayList<Worker> workers = new ArrayList();

    public Player (String name, int id){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Worker getWorker(int whichWorker){
        for(int i = 0; i<workers.size(); i++)
            if (workers.get(i).getWorkerId() == whichWorker) {
                return workers.get(i);
            }
        return null;
    }

    public void addWorker(Worker worker) {
        if(worker.getPlayerId() == this.id) { //correctly add only if the worker isnot an opposite worker
            if(getWorker(worker.getWorkerId())==null) { //no duplicates allowed
                workers.add(worker);
            }
        }
    }
}
