package it.polimi.ingsw.Effects.WinCondition;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;


public class CantWinMovingOnPerimeter extends StandardWinCondition {

    /** This method is used for the win condition that blocks the worker to win
     * on a perimeter cell of the board
     * @return false if the condition is met, the player cannot win
     * @return true in this case, true does not mean the player has won, but it has the possibility
     * to do so with the standard win conditions.
     * @param workerCell the worker's Cell before the move
     * @param destinationCell the worker's Cell after the move
     */

    @Override
    public boolean win(Cell workerCell, Cell destinationCell, Board board) {
        return (!destinationCell.isPerimetral());
    }
}
