package it.polimi.ingsw.Effects;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Effects.Build.StandardBuild;
import it.polimi.ingsw.Effects.ConsolidateBuild.StandardConsolidateBuild;
import it.polimi.ingsw.Effects.ConsolidateMove.StandardConsolidateMove;
import it.polimi.ingsw.Effects.Move.StandardMove;
import it.polimi.ingsw.Effects.Turn.NewTurn;
import it.polimi.ingsw.Effects.WinCondition.StandardLoseCondition;
import it.polimi.ingsw.Effects.WinCondition.StandardWinCondition;
import it.polimi.ingsw.Position;
import it.polimi.ingsw.Turn;

import java.util.List;
import java.util.Set;

public class GodPower {
    private StandardMove move;
    private StandardBuild build;
    private StandardConsolidateMove consolidateMove;
    private StandardConsolidateBuild consolidateBuild;
    private StandardLoseCondition loseCondition;
    private List<StandardWinCondition> positiveWinConditions;
    private List<StandardWinCondition> blockingWinConditions;
    private NewTurn newTurn;
    private boolean askToBuildDomes = false;
    private boolean askToBuildBeforeMoveAndNotMoveUp = false;

    public Set<Cell> move (Cell workerCell, Board board, Turn turn)  {
        return move.move(workerCell, board, turn);
    }

    public Set<Cell> build(Cell workerCell, Board board, Turn turn) {
        return build.build(workerCell, board, turn);
    }

    //returns the state of the board after the worker has moved
    public void moveInto (Board board, Cell workerCell, Cell destinationCell) {
        consolidateMove.moveInto(board, workerCell, destinationCell);
    }

    //returns the state of the board after the workers has built
    public void BuildUp (Position buildingCell, Board board, boolean god_power) {
        consolidateBuild.BuildUp(buildingCell, board, god_power);
    }

    /**
     * Checks if the player has lost
     *
     * @param collectMove a set<Cell> with the possible cells where the player can move
     * @param collectBuild  a set<Cell> with the possible cells where the player can build
     * @return true if the player has lost, false otherwise
     */
    public boolean lose (Set<Cell> collectMove, Set<Cell> collectBuild){
        return  loseCondition.lose(collectMove, collectBuild);
    }

    /**
     *  This method checks if the player has won
     *
     * @param workerCell the worker's cell
     * @param destinationCell the worker's destination cell after its move
     * @param board the board of the game
     * @return true if the player has won, false otherwise
     */


    public boolean win (Cell workerCell, Cell destinationCell, Board board) {
        boolean win = false;

        //checks if one of the player has won with one of the win conditions
        //that they have available with their god power
        for (int i =0; i<positiveWinConditions.size(); i++) {
           if (positiveWinConditions.get(i).win(workerCell, destinationCell, board))
               win = true;
        }

        //checks if one of the conditions that block the player possibility to win
        //was set to false

        for (int j=0; j<blockingWinConditions.size(); j++) {
            if(blockingWinConditions.get(j).win(workerCell, destinationCell, board)){
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
        GodPower tempGodPower = new GodPower();
        tempGodPower.setMove(this.move);
        tempGodPower.setBuild(this.build);
        tempGodPower.setConsolidateBuild(this.consolidateBuild);
        tempGodPower.setConsolidateMove(this.consolidateMove);
        tempGodPower.setPositiveWinConditions(this.positiveWinConditions);
        tempGodPower.setNegativeWinConditions(this.blockingWinConditions);
        //tempGodPower.setMove(this.move);
        return tempGodPower;
    }


    //Setter and Getter

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

    public List<StandardWinCondition> getNegativeWinConditions() {
        return blockingWinConditions;
    }

    public void setNegativeWinConditions(List<StandardWinCondition> negativeWinConditions) {
        this.blockingWinConditions = negativeWinConditions;
    }

    public void setNewTurn(NewTurn newTurn) { this.newTurn = newTurn; }

    public NewTurn getNewTurn() { return newTurn; }

    public void setAskToBuildDomes(boolean askToBuildDomes) {
        this.askToBuildDomes = askToBuildDomes;
    }

    public void setAskToBuildBeforeMoveAndNotMoveUp(boolean askToBuildBeforeMoveAndNotMoveUp) {
        this.askToBuildBeforeMoveAndNotMoveUp = askToBuildBeforeMoveAndNotMoveUp;
    }

    public boolean isAskToBuildDomes() {return askToBuildDomes; }

    public boolean isAskToBuildBeforeMoveAndNotMoveUp() {return askToBuildBeforeMoveAndNotMoveUp; }
}
