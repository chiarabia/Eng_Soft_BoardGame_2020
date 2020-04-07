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
    private List<StandardWinCondition> negativeWinConditions;
    private NewTurn newTurn;
    private boolean askToBuildDomes = false;
    private boolean askToBuildBeforeMoveAndNotMoveUp = false;

    public Set<Cell> move (Cell workerCell, Board board, Turn turn)  {
        return move.move(workerCell, board, turn);
    }

    public Set<Cell> build(Cell workerCell, Board board, Turn turn) {
        return build.build(workerCell, board, turn);
    }

    public void moveInto (Board board, Cell workerCell, Cell destinationCell) {
        consolidateMove.moveInto(board, workerCell, destinationCell);
    }

    public void BuildUp (Position buildingcell, Board board, boolean god_power) {
        consolidateBuild.BuildUp(buildingcell, board, god_power);
    }

    public boolean lose (Set<Cell> collectMove, Set<Cell> collectBuild){
        return  loseCondition.lose(collectMove, collectBuild);
    }

    public boolean win (Cell workerCell, Cell destinationCell, Board board) {
        boolean temp = false; //parto dall'idea di non aver vinto

        for (int i =0; i<positiveWinConditions.size(); i++) { //se una delle mie win condition, mi dice che ho vinto, setto a true
           if (positiveWinConditions.get(i).win(workerCell, destinationCell, board))
               temp = true;
        }

        for (int j=0; j<negativeWinConditions.size(); j++) {//porto a false temp, se ho un malus alla win condition
            if(negativeWinConditions.get(j).win(workerCell, destinationCell, board)){
                temp = false;
            }
        }
        return temp;
    }


    //Da usare nell'effetto di Athena
    public GodPower copyGodPower (GodPower godPower) {
        GodPower tempGodPower = new GodPower();
        tempGodPower.setMove(this.move);
        tempGodPower.setBuild(this.build);
        tempGodPower.setConsolidateBuild(this.consolidateBuild);
        tempGodPower.setConsolidateMove(this.consolidateMove);
        tempGodPower.setPositiveWinConditions(this.positiveWinConditions);
        tempGodPower.setNegativeWinConditions(this.negativeWinConditions);
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
        return negativeWinConditions;
    }

    public void setNegativeWinConditions(List<StandardWinCondition> negativeWinConditions) {
        this.negativeWinConditions = negativeWinConditions;
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
