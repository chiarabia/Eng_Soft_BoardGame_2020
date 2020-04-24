package it.polimi.ingsw.effects.consolidateBuild;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import it.polimi.ingsw.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class StandardConsolidateBuildTest {
    StandardConsolidateBuild standardConsolidateBuild = new StandardConsolidateBuild();
    Cell buildingCell;
    Board board;
    boolean forceDome;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);

    @BeforeEach
    void setUp(){
        board = new Board();
    }

    //positive
    @Test
    void withForceDomeTrueShouldBeAbleToBuildADomeOnTheCell(){
        forceDome = true;
        buildingCell = board.getCell(0,0,0);
        standardConsolidateBuild.buildUp(buildingCell.getPosition(),board,forceDome);
        assertTrue(buildingCell.isDome());
    }

    //positive
    @Test
    void shouldNotBuildADomeWithForceDomeFalseAndNotOnThirdLevel(){
        forceDome = false;
        buildingCell = board.getCell(0,0,0);
        standardConsolidateBuild.buildUp(buildingCell.getPosition(),board,forceDome);
        assertFalse(buildingCell.isDome());
    }

    //positive
    @Test
    void shouldBuildABuildingWhenNotOnThirdLevelAndWithForceDomeFalse(){
        forceDome = false;
        buildingCell = board.getCell(0,0,0);
        standardConsolidateBuild.buildUp(buildingCell.getPosition(),board,forceDome);
        assertTrue(buildingCell.isBuilding());
    }

    //positive
    @Test
    void shouldBuildADomeThenOnThirdLevelAndWithForceDomeFalse(){
        forceDome = false;
        board.getCell(0,0,0).setBuilding(true);
        board.newCell(1,1,1);
        board.getCell(1,1,1).setBuilding(true);
        board.newCell(2,2,2);
        board.getCell(2,2,2).setBuilding(true);
        board.newCell(3,3,3);
        buildingCell = board.getCell(3,3,3);
        standardConsolidateBuild.buildUp(buildingCell.getPosition(),board,forceDome);
        assertTrue(buildingCell.isDome());
    }

    //positive
    @Test
    void buildUpShouldThrowExceptionWithNullParametersAndForceDomeFalse () {
        assertThrows(NullPointerException.class, () -> {
            standardConsolidateBuild.buildUp(null, null, false);
        });
    }

    //positive
    @Test
    void buildUpShouldThrowExceptionWithNullParametersAndForceDomeTrue () {
        assertThrows(NullPointerException.class, () -> {
            standardConsolidateBuild.buildUp(null, null, true);
        });
    }

}


