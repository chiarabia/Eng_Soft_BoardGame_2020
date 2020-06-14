package it.polimi.ingsw.effects.consolidateBuild;

import it.polimi.ingsw.*;
import it.polimi.ingsw.server.serializable.SerializableUpdateInfos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class StandardConsolidateBuildTest {
    StandardConsolidateBuild standardConsolidateBuild = new StandardConsolidateBuild();
    Cell buildingCell;
    SerializableUpdateInfos updateInfos;
    Board board;
    boolean forceDome;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);

    @BeforeEach
    void setUp(){
        board = new Board();
        updateInfos = null;
    }

    //positive
    @Test
    void withForceDomeTrueShouldBeAbleToBuildADomeOnTheCell(){
        forceDome = true;
        buildingCell = board.getCell(0,0,0);
        updateInfos = standardConsolidateBuild.buildUp(buildingCell.getPosition(),board,forceDome);
        assertAll("DomeShouldbeCreated", () -> assertTrue(buildingCell.isDome()),
                () -> assertNotNull(updateInfos.getUpdateBuild().get(0)),
                () -> assertTrue(updateInfos.getUpdateBuild().get(0).isDome()),
                () -> assertEquals(updateInfos.getUpdateBuild().get(0).getNewPosition(), buildingCell.getPosition()));

    }

    //positive
    @Test
    void shouldNotBuildADomeWithForceDomeFalseAndNotOnThirdLevel(){
        forceDome = false;
        buildingCell = board.getCell(0,0,0);
        updateInfos = standardConsolidateBuild.buildUp(buildingCell.getPosition(),board,forceDome);
        assertAll("DomeShouldNotbeCreated", () -> assertFalse(buildingCell.isDome()),
                () -> assertNotNull(updateInfos.getUpdateBuild().get(0)),
                () -> assertFalse(updateInfos.getUpdateBuild().get(0).isDome()),
                () -> assertEquals(updateInfos.getUpdateBuild().get(0).getNewPosition(), buildingCell.getPosition()));
    }

    //positive
    @Test
    void shouldBuildABuildingWhenNotOnThirdLevelAndWithForceDomeFalse(){
        forceDome = false;
        buildingCell = board.getCell(0,0,0);
        updateInfos = standardConsolidateBuild.buildUp(buildingCell.getPosition(),board,forceDome);

        assertAll("ShouldBeBuildaBuilding", () -> assertTrue(buildingCell.isBuilding()),
                () -> assertNotNull(updateInfos.getUpdateBuild().get(0)),
                () -> assertFalse(updateInfos.getUpdateBuild().get(0).isDome()),
                () -> assertEquals(updateInfos.getUpdateBuild().get(0).getNewPosition(), buildingCell.getPosition()));
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
        updateInfos = standardConsolidateBuild.buildUp(buildingCell.getPosition(),board,forceDome);

        assertAll("DomeisCreated" , () -> assertTrue(buildingCell.isDome()),
                () -> assertNotNull(updateInfos.getUpdateBuild().get(0)),
                () -> assertTrue(updateInfos.getUpdateBuild().get(0).isDome()),
                () -> assertEquals(updateInfos.getUpdateBuild().get(0).getNewPosition(), buildingCell.getPosition()));

    }

    //positive
    @Test
    void buildUpShouldThrowExceptionWithNullParametersAndForceDomeFalse () {
        assertThrows(NullPointerException.class, () -> standardConsolidateBuild.buildUp(null, null, false));
    }

    //positive
    @Test
    void buildUpShouldThrowExceptionWithNullParametersAndForceDomeTrue () {
        assertThrows(NullPointerException.class, () -> standardConsolidateBuild.buildUp(null, null, true));
    }

}


