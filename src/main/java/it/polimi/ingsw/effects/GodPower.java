package it.polimi.ingsw.effects;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.effects.build.StandardBuild;
import it.polimi.ingsw.effects.consolidateBuild.StandardConsolidateBuild;
import it.polimi.ingsw.effects.consolidateMove.StandardConsolidateMove;
import it.polimi.ingsw.effects.move.StandardMove;
import it.polimi.ingsw.effects.turn.NewTurn;
import it.polimi.ingsw.effects.winCondition.StandardLoseCondition;
import it.polimi.ingsw.effects.winCondition.StandardWinCondition;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;
import it.polimi.ingsw.server.serializable.SerializableUpdateActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GodPower {
    private final String godName;
    private final int playerId;

    private StandardMove move;
    private StandardBuild build;

    private StandardConsolidateMove consolidateMove;
    private StandardConsolidateBuild consolidateBuild;

    private StandardLoseCondition loseCondition;
    private List<StandardWinCondition> positiveWinConditions = new ArrayList<>();
    private List<StandardWinCondition> blockingWinConditions = new ArrayList<>();

    private NewTurn newTurn;

    private boolean askToBuildDomes = false;

    public Set<Position> move (Position workerPosition, Board board, Turn turn)  {
        return move.move(workerPosition, board, turn);
    }

    public Set<Position> build(Position workerPosition, Board board, Turn turn) {
        return build.build(workerPosition, board, turn);
    }

    //returns the state of the board after the worker has moved
    public SerializableUpdateActions moveInto (Board board, Position workerPosition, Position destinationPosition) {
        return consolidateMove.moveInto(board, workerPosition, destinationPosition);
    }

    //returns the state of the board after the workers has built
    public SerializableUpdateActions buildUp (Position buildingPosition, Board board, boolean god_power) {
        return consolidateBuild.buildUp(buildingPosition, board, god_power);
    }

    public Turn endTurn (Turn oldTurn, List<GodPower> godPowers, Player player){
        return newTurn.endTurn(oldTurn, godPowers, player);
    }

    /**
     * Checks if the player has lost
     *
     * @param collectMove a <code>Set&lt;Cell&gt;</code> with the possible cells where the player can move
     * @param collectBuild  a <code>Set&lt;Cell&gt;</code> with the possible cells where the player can build
     * @return true if the player has lost, false otherwise
     */
    public boolean lose (Set<Position> collectMove, Set<Position> collectBuild){
        return  loseCondition.lose(collectMove, collectBuild);
    }

    /**
     *  This method checks if the player has won
     *
     * @param workerPosition the worker's cell
     * @param destinationPosition the worker's destination cell after its move
     * @param board the board of the game
     * @return true if the player has won, false otherwise
     */


    public boolean win (Position workerPosition, Position destinationPosition, Board board) {
        boolean win = false;

        //checks if one of the player has won with one of the win conditions
        //that they have available with their god power
        for (int i =0; i<positiveWinConditions.size(); i++) {
           if (positiveWinConditions.get(i).win(workerPosition, destinationPosition, board))
               win = true;
        }

        //checks if one of the conditions that block the player possibility to win
        //was set to false

        for (int j=0; j<blockingWinConditions.size(); j++) {
            if(!blockingWinConditions.get(j).win(workerPosition, destinationPosition, board)){
                win = false;
            }
        }
        return win;
    }


    /**
     * Creates a save state of the abilities that the player has with their god power
     * @param godPower the god power the player has that we want to save
     * @return a temporal save of all the abilities the player
     */
    public GodPower copyGodPower (GodPower godPower) {
        GodPower tempGodPower = new GodPower(godPower.playerId, godPower.godName);
        tempGodPower.setMove(this.move);
        tempGodPower.setBuild(this.build);
        tempGodPower.setConsolidateBuild(this.consolidateBuild);
        tempGodPower.setConsolidateMove(this.consolidateMove);
        tempGodPower.setPositiveWinConditions(this.positiveWinConditions);
        tempGodPower.setBlockingWinConditions(this.blockingWinConditions);
        tempGodPower.setLoseCondition(this.loseCondition);
        tempGodPower.setNewTurn(this.newTurn);
        return tempGodPower;
    }

    public GodPower(int playerId, String godName) {
        this.playerId = playerId;
        this.godName = godName;
    }

    //Setters and Getters

    public String getGodName() { return godName; }

    public int getPlayerId() {
        return playerId;
    }

    public StandardMove getMove() {
        return move;
    }

    public void setMove(StandardMove move) {
        this.move = move;
    }

    public StandardBuild getBuild() {
        return build;
    }

    public void setBuild(StandardBuild build) {
        this.build = build;
    }

    public StandardConsolidateMove getConsolidateMove() {
        return consolidateMove;
    }

    public void setConsolidateMove(StandardConsolidateMove consolidateMove) {
        this.consolidateMove = consolidateMove;
    }

    public StandardConsolidateBuild getConsolidateBuild() {
        return consolidateBuild;
    }

    public void setConsolidateBuild(StandardConsolidateBuild consolidateBuild) {
        this.consolidateBuild = consolidateBuild;
    }

    public StandardLoseCondition getLoseCondition() {
        return loseCondition;
    }

    public void setLoseCondition(StandardLoseCondition loseCondition) {
        this.loseCondition = loseCondition;
    }

    public List<StandardWinCondition> getPositiveWinConditions() {
        return positiveWinConditions;
    }

    public void setPositiveWinConditions(List<StandardWinCondition> positiveWinConditions) {
        this.positiveWinConditions = positiveWinConditions;
    }

    public List<StandardWinCondition> getBlockingWinConditions() {
        return blockingWinConditions;
    }

    public void setBlockingWinConditions(List<StandardWinCondition> blockingWinConditions) {
        this.blockingWinConditions = blockingWinConditions;
    }

    public void setNewTurn(NewTurn newTurn) { this.newTurn = newTurn; }

    public NewTurn getNewTurn() { return newTurn; }

    public void setAskToBuildDomes(boolean askToBuildDomes) {
        this.askToBuildDomes = askToBuildDomes;
    }

    public boolean isAskToBuildDomes() {return askToBuildDomes; }

    public void addPositiveWinConditions(StandardWinCondition positiveWinConditions) {
        this.positiveWinConditions.add(positiveWinConditions);
    }
    public void addBlockingWinConditions(StandardWinCondition blockingWinConditions) {
        this.blockingWinConditions.add(blockingWinConditions);
    }
}
