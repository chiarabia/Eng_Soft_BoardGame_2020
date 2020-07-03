package it.polimi.ingsw.effects.winCondition;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;

/**
 * This class handles the victory when five completed towers are situated on the board
 */
public class FiveCompletedTowers extends StandardWinCondition {
    private static int LAST_BUILDING = 3;
    private static double NUM_VICTORY_TOWERS = 5.0;

    /** This method is used for the win condition that enables the worker to also win
     *  if there are at least five completed towers on the board
     *
     * @param workerPosition the worker's Cell before the move
     * @param destinationPosition the worker's Cell after the move
     * @param board the board of the game
     * @return true if the player has won, false otherwise
     */
    public boolean win (Position workerPosition, Position destinationPosition, Board board) {
        return board.getStream().filter(x->x.getZ()== LAST_BUILDING)
                                .filter(x->x.isDome())
                                .count() >= NUM_VICTORY_TOWERS;
    }
}
