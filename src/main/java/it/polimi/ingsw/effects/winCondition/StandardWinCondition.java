package it.polimi.ingsw.effects.winCondition;


import it.polimi.ingsw.Board;
import it.polimi.ingsw.Cell;
import it.polimi.ingsw.Position;


public class StandardWinCondition {

        /** This is the general method for the winning condition
         * @return true if the Player has won
         * @return false if the win condition is not met yet
         * @param workerPosition the worker's Cell before the move
         * @param destinationPosition the worker's Cell after the move
         * @param board the board of the game
         */

        public boolean win (Position workerPosition, Position destinationPosition, Board board) {
            if (workerPosition==null &&  destinationPosition==null) return false;
            Cell workerCell = board.getCell(workerPosition);
            Cell destinationCell = board.getCell(destinationPosition);
            return ((workerCell.getZ() == 2 && destinationCell.getZ() == 3));
        }
    }
