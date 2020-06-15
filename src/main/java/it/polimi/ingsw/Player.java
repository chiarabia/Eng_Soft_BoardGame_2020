package it.polimi.ingsw;

import java.util.ArrayList;

/**
 *  This class defines the player
 *  <p> the player id will be a number from 1 to 3, in private final int id
 */


public class Player {
    private final String name;
    private final int id;
    private ArrayList<Worker> workers = new ArrayList<>();

    public Player (String name, int id){
        this.id = id;
        this.name = name;
    }

    public boolean areWorkersSet () {return workers.size()>0;}

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
        if(worker.getPlayerId() == this.id) { //checks if the worker is the worker's player and not of an opposing player
            if(getWorker(worker.getWorkerId())==null) { //no duplicates allowed
                workers.add(worker);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player)
            return this.name.equals(((Player) obj).name) &&
                    this.id == ((Player) obj).id;
        return false;
    }
}
