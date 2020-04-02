package it.polimi.ingsw;

/**
 * This class defines a turn of the game
 */

public class Turn {
    private Position workerStartingPosition = null;
    private Position firstBuildingPosition = null;
    //al momento non ci serve salvarsi dove i giocatori hanno costruito/si sono mossi, ma risulta immediato aggiornare i metodi per farlo
    //private List<Position> movecells = new ArrayList<Position>();
    //private List<Position> buildcells = new ArrayList<Position>();

    private boolean moveBeforeBuild = false;
    private boolean buildAfterMove = false;

    private boolean move_Up = false;
    private boolean move_Down = false;

    private int move_times = 0;
    private int build_times = 0;

    private int workerUsed;
    private final int player_id;

    public Turn(Player player) {
        this.player_id = player.getId();
    }

    public boolean isMoveBeforeBuild() {
        return moveBeforeBuild;
    }

    public boolean isBuildAfterMove() {
        return buildAfterMove;
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

    public int getWorkerUsed() {
        return workerUsed;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public Position getWorkerStartingPosition() {
        return workerStartingPosition;
    }

    public Position getFirstBuildingPosition() {
        return firstBuildingPosition;
    }

    /**
     * This method needs to be called after a move action to update the data
     * with the choices of the current player.
     *
     * @param workerStartingCell the Cell where the worker starts the turn
     * @param workerDestinationCell the Cell where the worker ends the turn
     */

    public void updateTurnInfoAfterMove (Cell workerStartingCell, Cell workerDestinationCell) {
        Position startingPosition = workerStartingCell.getCellPosition();
        Position destinationPosition = workerDestinationCell.getCellPosition();

        //saving the the worker_id to ensure only this player will do something again in this turn
        if (move_times == 0 && !moveBeforeBuild) {
            this.workerStartingPosition = startingPosition;
            this.workerUsed = workerDestinationCell.getWorkerId();
        }

        //This is set as true if this is the first move of the turn
        if (!moveBeforeBuild)
            this.moveBeforeBuild = true;

        //Verify if at least one time the worker has moved up
        if (verifyMoveUp(startingPosition.getZ(), startingPosition. getZ()))
            this.move_Up = true;

        //Verify if at least one time the worker has moved down
        if (verifyMoveDown(startingPosition.getZ(), startingPosition. getZ()))
            this.move_Down = true;

        this.move_times ++; //without condition, this method has to be call after a move action
    }


    /**
     * This method verifies if the worker moved up
     *
     * @param starting_Z z coordinate of the worker starting position
     * @param destination_Z z coordinate of the worker ending position
     * @return true if the worker moved up
     */
    private boolean verifyMoveUp (int starting_Z, int destination_Z) {
        return (destination_Z - starting_Z)>=1;
    }

    /**
     * This method verifies if the worker moved down
     *
     * @param starting_Z z coordinate of the worker starting position
     * @param destination_Z z coordinate of the worker ending position
     * @return true if the worker moved down
     */
    private boolean verifyMoveDown (int starting_Z, int destination_Z) {
        return (starting_Z - destination_Z)>=1;
    }

    /**
     * This method needs to be called after a build action to update the data
     * with the choices of the current player.
     *
     * @param buildingCell the Cell where the worker has built
     */

    public void updateTurnInfoAfterBuild ( Cell buildingCell) {
        Position buildingPosition = buildingCell.getCellPosition();

        //set up the parameters after the standard build
        if (!buildAfterMove && moveBeforeBuild) {
            buildAfterMove = true;
            firstBuildingPosition = buildingPosition;
        }
        build_times ++;
    }
}


