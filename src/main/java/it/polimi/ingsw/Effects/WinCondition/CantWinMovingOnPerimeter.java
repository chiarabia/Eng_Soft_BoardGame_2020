package it.polimi.ingsw.Effects.WinCondition;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;


public class CantWinMovingOnPerimeter extends StandardWinCondition {

    /** This method is used for the win condition that blocks the worker possibility to win
     * on a perimeter cell of the board
     * @param workerCell the worker's Cell before the move
     * @param destinationCell the worker's Cell after the move
     * @param board the board of the game
     * @return false if the condition is met, the player cannot win
     * @return true if the player has the possibility to win
     * with the standard win conditions.
     */

    @Override
    public boolean win(Cell workerCell, Cell destinationCell, Board board) {
        return (!destinationCell.isPerimetral());
    }
}
