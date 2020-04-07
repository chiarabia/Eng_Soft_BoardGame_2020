package it.polimi.ingsw.Effects.WinCondition;


import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;


    public class StandardWinCondition {

        /** This is the general method for the winning condition
         * @return true if the Player has won
         * @return false if the win condition is not met yet
         * @param workerCell the worker's Cell before the move
         * @param destinationCell the worker's Cell after the move
         */

        public boolean win (Cell workerCell, Cell destinationCell, Board board) {
            return (workerCell.getZ() == 2 && destinationCell.getZ() == 3);
        }
    }
