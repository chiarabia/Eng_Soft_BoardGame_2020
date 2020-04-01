package it.polimi.ingsw;

public class Turn {
    private Position workerstartingposition = null;
    private Position firstbuildingposition = null;
    //al momento non ci serve salvarsi dove i giocatori hanno costruito/si sono mossi, ma risulta immediato aggiornare i metodi per farlo
    //private List<Position> movecells = new ArrayList<Position>();
    //private List<Position> buildcells = new ArrayList<Position>();
    private boolean movebeforebuild = false;
    private boolean buildaftermove = false;
    private boolean move_Up = false;
    private boolean move_Down = false;
    private int move_times = 0;
    private int build_times = 0;
    private int workerused;
    private final int player_id;

    public Turn(Player player) {
        this.player_id = player.getId();
    }

    public boolean isMovebeforebuild() {
        return movebeforebuild;
    }

    public boolean isBuildaftermove() {
        return buildaftermove;
    }

    public boolean isMove_Up() {
        return move_Up;
    }

    public boolean isMove_Down() {
        return move_Down;
    }

    public int getMove_times() {
        return move_times;
    }

    public int getBuild_times() {
        return build_times;
    }

    public int getWorkerused() {
        return workerused;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public Position getWorkerstartingposition() {
        return workerstartingposition;
    }

    public Position getFirstbuildingposition() {
        return firstbuildingposition;
    }

    //this method must be invocated after a move action, and this will modify the parameters to track the actions made
    //by the current player
    public void updateTurnInfoAfterMove (Cell workerStartingCell, Cell workerDestinationCell) {
        Position startingPosition = workerStartingCell.getCellPosition();
        Position destinationPosition = workerStartingCell.getCellPosition();

        //saving the the worker_id to ensure only this player will do something again in this turn
        if (move_times == 0 && !movebeforebuild) {
            this.workerstartingposition = startingPosition;
            this.workerused = workerDestinationCell.getWorkerId();
        }

        //This is setted ture if this is the first move of the turn
        if (!movebeforebuild)
            this.movebeforebuild = true;

        //Verify if at least one time the worker has leveled up
        if (verifyMoveUp(startingPosition.getZ(), startingPosition. getZ()))
            this.move_Up = true;

        //Verify if at least one time the worker has moved down
        if (verifyMoveDown(startingPosition.getZ(), startingPosition. getZ()))
            this.move_Down = true;

        this.move_times ++; //without condition, this method has to be call after a move action
    }

    //verifico se sono salito
    private boolean verifyMoveUp (int starting_Z, int destinazion_Z) {
        return (destinazion_Z - starting_Z)>=1;
    }

    //verifico se sono sceso
    private boolean verifyMoveDown (int starting_Z, int destinazion_Z) {
        return (starting_Z - destinazion_Z)>=1;
    }

    public void updateTurnInfoAfterBuild ( Cell buildingCell) {
        Position buildingposition = buildingCell.getCellPosition();

        //set up the parameters after the standard build
        if (!buildaftermove && movebeforebuild) {
            buildaftermove = true;
            firstbuildingposition = buildingposition;
        }
        build_times ++;
    }
}


