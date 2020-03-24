package it.polimi.ingsw;

public class Player {
    private final String name;
    private final int id; // convenzione: usiamo come id 1, 2 ed eventualmente 3
    private Cell workerCell[]; // convenzione: usiamo woker 1 (in posizione 0 dell'array) e 2 (in posizione 1 dell'array)

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Cell getWorkerCell(int whichWorker){
        return workerCell[whichWorker - 1];
    }

    public void newWorkerCell(Cell cell, int whichWorker){
        // spegne il bit "worker" alla cella attuale e lo accende a quella ventura
        if (workerCell[whichWorker - 1]!=null) workerCell[whichWorker - 1].setWorker(false);
        workerCell[whichWorker - 1] = cell;
        workerCell[whichWorker - 1].setWorker(true);
    }

    public void removeWorkerCells(){
        workerCell[0].setWorker(false);
        workerCell[1].setWorker(false);
    }

    public Player (String name, int id){
        this.id = id;
        this.name = name;
        workerCell = new Cell[2];
    }
}
