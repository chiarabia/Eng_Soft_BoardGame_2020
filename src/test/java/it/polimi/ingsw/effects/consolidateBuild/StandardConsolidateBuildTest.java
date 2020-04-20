package it.polimi.ingsw.effects.consolidateBuild;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import it.polimi.ingsw.*;
import it.polimi.ingsw.effects.consolidateMove.StandardConsolidateMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class StandardConsolidateBuildTest {
    StandardConsolidateBuild standardConsolidateBuild = new StandardConsolidateBuild();
    Cell workerCell;
    Cell destinationCell;
    Board board;
    Turn turn;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);

    @BeforeEach
    void setUp(){
        turn = new Turn(player);
        board = new Board();
    }
    @Test
    void BuildingCreateNewFreeCellAboveTheBuilding() {
        board.getCell(0, 0, 0);

        //creation of a nearly complete board using standardConsolidateBuild
        for (int x=0; x<5; x++) {
            for (int y=0; y<5; y++) {
                for (int z=0; z<3; z++) {
                    assertTrue(board.getCell(x, y, z).isFree()); //free before

                    standardConsolidateBuild.BuildUp(new Position(x, y, z), board, false);
                    assertFalse(board.getCell(x, y, z).isFree());//NOT free after

                    assertNotNull(board.getCell(x,y,z+1)); //automatically created new free Cell above mine
                    assertTrue(board.getCell(x,y,z+1).isFree());
                }

            }
        }
        for (int x=0; x<5; x++) {
            for (int y=0; y<5; y++) {
                assertTrue(board.getCell(x,y,3).isFree());
            }
        }
        assertTrue(board.getStream().filter(a->a.isFree()).count()==25*1.0);
        assertTrue(board.getStream().filter(a->a.getZ()<=3).filter(a->a.isBuilding()).count()==25.0*3.0);
    }

    @Test
    void DomeCreation() {
    //creation of a nearly complete board
    for (int x=0; x<5; x++) {
        for (int y=0; y<5; y++) {
            for (int z=0; z<3; z++) {
                board.newCell(x,y,z);
                board.getCell(x,y,z).setBuilding(true);
            }
            board.newCell(x,y,3);
        }
    }
    for (int x=0; x<5; x++) {
        for (int y=0; y<5; y++) {
            standardConsolidateBuild.BuildUp(new Position(x,y,3), board, false);
        }
    }
    assertTrue(board.getStream().filter(a->a.isFree()).count()==25*0.0);
    assertTrue(board.getStream().filter(a->a.getZ()<=2).filter(a->a.isBuilding()).count()==25.0*3.0);
    assertTrue(board.getStream().filter(a->a.getZ()==3).filter(a->a.isDome()).count()==25.0*1.0);

}

    @Test
    void DomeAtAnyLevel () {
        for (int z=0; z<3; z++) {
            //creating an entire level of dome
            for (int y=0; y<5; y++) {
                for (int x=0; x<5; x++) {
                    assertTrue(board.getCell(x, y, z).isFree()); //free before

                    standardConsolidateBuild.BuildUp(new Position(x, y, z), board, true);
                    assertFalse(board.getCell(x, y, z).isFree());//NOT free after

                    assertTrue(board.getCell(x,y,z+1)==null); //This Method doesnot create domes
                    assertTrue(board.getCell(x,y,z).isDome());
                }
            }
            assertTrue(board.getStream().filter(a->a.isFree()).count()==25*0.0);
            assertTrue(board.getStream().filter(a->a.isDome()).count()==25.0*1.0);

            //resetting the previous floor of the board, and then starting to build dome in the next floor of the board
            for (int y=0; y<5; y++) {
                for (int x=0; x<5; x++) {
                    board.getCell(x, y, z).setDome(false); //free before
                    standardConsolidateBuild.BuildUp(new Position(x, y, z), board, false);
                    assertFalse(board.getCell(x, y, z).isFree());//NOT free after
                    assertNotNull(board.getCell(x,y,z+1)); //This Method doesnot create domes
                    assertTrue(board.getCell(x,y,z+1).isFree());
                }
            }

        }



    }

}


