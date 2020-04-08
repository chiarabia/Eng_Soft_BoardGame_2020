package it.polimi.ingsw.effects.winCondition;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;


public class WinMovingDownTwoOrMoreLevels extends StandardWinCondition {

    /** This method is used for the win condition that enables the worker to also win
     *  by going down two levels
     * @return true if the Player has won
     * @return false if the win condition is not met yet
     * @param workerCell the worker's Cell before the move
     * @param board the board of the game
     * @param destinationCell the worker's Cell after the move
     */

    @Override
    public boolean win (Cell workerCell, Cell destinationCell, Board board){
        if (workerCell==null && destinationCell==null) return false; //condizione generalizzata
        return ((workerCell.getZ() - destinationCell.getZ()) >= 2);
    }
}
