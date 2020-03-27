package it.polimi.ingsw.Effects;

import it.polimi.ingsw.Cell;


public class WinMovingDownTwoOrMoreLevel extends StandardWinCondition {

    /** This method is used for the win condition that enables the worker to also win
     *  by going down two levels
     * @return true if the Player has won
     * @return false if the win condition is not met yet
     * @param workerCell the worker's Cell before the move
     * @param destinationCell the worker's Cell after the move
     */

    @Override
    public boolean win (Cell workerCell, Cell destinationCell){
        if((workerCell.getZ() - destinationCell.getZ()) == 2 ) return true;
        else  return false;
    }
}
