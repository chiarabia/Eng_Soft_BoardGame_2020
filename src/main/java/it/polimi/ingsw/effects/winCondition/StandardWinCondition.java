package it.polimi.ingsw.effects.winCondition;


import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;


    public class StandardWinCondition {

        /** This is the general method for the winning condition
         * @return true if the Player has won
         * @return false if the win condition is not met yet
         * @param workerCell the worker's Cell before the move
         * @param destinationCell the worker's Cell after the move
         * @param board the board of the game
         */

        public boolean win (Cell workerCell, Cell destinationCell, Board board) {
            if (workerCell==null && destinationCell==null) return false; //condizione generalizzata
            return (workerCell.getZ() == 2 && destinationCell.getZ() == 3);
        }
    }
