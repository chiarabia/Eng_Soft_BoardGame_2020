package it.polimi.ingsw.model.effects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.effects.winCondition.CantWinMovingOnPerimeter;
import it.polimi.ingsw.model.effects.winCondition.StandardLoseCondition;
import it.polimi.ingsw.model.effects.winCondition.StandardWinCondition;
import it.polimi.ingsw.model.effects.winCondition.WinMovingDownTwoOrMoreLevels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GodPowerTest {
    Player player = new Player("pippo",12);
    GodPower godPower = new GodPower(12,"ciccio");
    Cell workerCell;
    Cell workerDestinationCell;
    Turn turn;
    Board board;
    Worker worker = new Worker(player, 12);


    @BeforeEach
    void setUp(){
        board = new Board();
        turn = new Turn(player);
    }

    //positive
    @Test
    void playerShouldWin(){
        List<StandardWinCondition> positiveWinConditions = new ArrayList<>();
        WinMovingDownTwoOrMoreLevels winMovingDownTwoOrMoreLevels = new WinMovingDownTwoOrMoreLevels();
        StandardWinCondition standardWinCondition = new StandardWinCondition();
        positiveWinConditions.add(standardWinCondition);
        positiveWinConditions.add(winMovingDownTwoOrMoreLevels);
        godPower.setPositiveWinConditions(positiveWinConditions);
        board.newCell(0,0,3);
        workerCell = board.getCell(0,0,3);
        workerDestinationCell = board.getCell(1,1,0);
        assertTrue(godPower.win(workerCell.getPosition(),workerDestinationCell.getPosition(),board));
    }

    //positive
    @Test
    void playerVictoryShouldBeBlocked(){
        List<StandardWinCondition> positiveWinConditions = new ArrayList<>();
        StandardWinCondition standardWinCondition = new StandardWinCondition();
        positiveWinConditions.add(standardWinCondition);
        List<StandardWinCondition> blockingWinConditions = new ArrayList <>();
        CantWinMovingOnPerimeter cantWinMovingOnPerimeter = new CantWinMovingOnPerimeter();
        blockingWinConditions.add(cantWinMovingOnPerimeter);
        board.newCell(0,0,3);
        workerDestinationCell = board.getCell(0,0,3);
        board.newCell(1,1,2);
        workerCell = board.getCell(1,1,2);
        assertFalse(godPower.win(workerCell.getPosition(),workerDestinationCell.getPosition(),board));
    }

    //positive
    @Test
    void playerShouldLose(){
        StandardLoseCondition standardLoseCondition = new StandardLoseCondition();
        godPower.setLoseCondition(standardLoseCondition);
        Set <Position>collectMove = new HashSet<>();
        Set <Position> collectBuild = new HashSet<>();
        collectBuild.add(new Position(0, 0, 0));
        assertFalse(godPower.lose(collectMove, collectBuild));
    }



}
