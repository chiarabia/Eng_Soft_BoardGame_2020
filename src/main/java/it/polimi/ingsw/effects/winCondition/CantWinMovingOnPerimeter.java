package it.polimi.ingsw.effects.winCondition;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;

/**
 * This class handles the block of a victory on a perimeter
 */

public class CantWinMovingOnPerimeter extends StandardWinCondition {

    /** This method is used for the win condition that blocks the worker possibility to win
     * on a perimeter cell of the board
     * @param workerPosition the worker's Cell before the move
     * @param destinationPosition the worker's Cell after the move
     * @param board the board of the game
     * @return false if the condition is met, the player cannot win, true if the player has the possibility to win
     * with the standard win conditions.
     */

    @Override
    public boolean win(Position workerPosition, Position destinationPosition, Board board) {
        if (workerPosition==null &&  destinationPosition==null) return true;
        Cell destinationCell = board.getCell(destinationPosition);
        return (!destinationCell.isPerimetral());
    }
}
