package it.polimi.ingsw.effects.winCondition;

import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;

public class FiveCompletedTowers extends StandardWinCondition {

    /** This method is used for the win condition that enables the worker to also win
     *  if there are at least five completed towers on the board
     *
     * @param workerCell the worker's Cell before the move
     * @param destinationCell the worker's Cell after the move
     * @param board the board of the game
     * @return true if the player has won, false otherwise
     */
    public boolean win (Cell workerCell, Cell destinationCell, Board board) {
        //condizione generalizzata
        return board.getStream().filter(x->x.getZ()==3)
                                .filter(x->x.isDome())
                                .count() >= 5.0;
    }
}
