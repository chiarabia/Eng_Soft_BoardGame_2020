package it.polimi.ingsw.EffectsTest.WinConditionTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import it.polimi.ingsw.effects.winCondition.StandardLoseCondition;
import it.polimi.ingsw.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class StandardLoseConditionTest {


   final StandardLoseCondition loseCondition = new StandardLoseCondition();

   Set<Cell> collectMove;
   Set<Cell> collectBuild;

    @BeforeEach
    void setUp(){
        collectMove = new HashSet<>();
        collectBuild = new HashSet<>();
    }

    //positive
    @Test
    void playerShouldLoseAndReturnTrueWhenBothAreEmpty(){
        assertTrue(loseCondition.lose(collectMove, collectBuild));
    }

    //positive
    @Test
    void playerShouldLoseAndReturnTrueWhenOnlyMoveIsEmpty() {
        collectBuild.add(new Cell(0, 0, 0));
        assertTrue(loseCondition.lose(collectMove, collectBuild));
    }

    //positive
    @Test
    void playerShouldLoseAndReturnTrueWhenOnlyBuildIsEmpty() {
        collectMove.add(new Cell(0, 0, 0));
        assertTrue(loseCondition.lose(collectMove, collectBuild));
    }

    //positive
    @Test
    void playerShouldNotLoseWhenBothAreNotEmpty(){
        collectMove.add(new Cell(0, 0, 0));
        collectBuild.add(new Cell(0, 0, 0)); // you must be sure of this beforehand
        assertFalse(loseCondition.lose(collectMove, collectBuild));
    }

    //positive
    @Test
    void ThrowsExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            loseCondition.lose(null, null);
        });
    }

}


