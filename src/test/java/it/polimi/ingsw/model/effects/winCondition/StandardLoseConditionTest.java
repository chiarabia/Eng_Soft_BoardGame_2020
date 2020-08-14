package it.polimi.ingsw.model.effects.winCondition;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.effects.build.StandardBuild;
import it.polimi.ingsw.model.effects.move.StandardMove;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class StandardLoseConditionTest {


   final StandardLoseCondition loseCondition = new StandardLoseCondition();

   Set<Position> collectMove;
   Set<Position> collectBuild;

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
    void playerShouldNotLoseAndReturnFalseWhenOnlyMoveIsEmpty() {
        collectBuild.add(new Position(0, 0, 0));
        assertFalse(loseCondition.lose(collectMove, collectBuild));
    }

    //positive
    @Test
    void playerShouldNotLoseAndReturnFalseWhenOnlyBuildIsEmpty() {
        collectMove.add(new Position(0, 0, 0));
        assertFalse(loseCondition.lose(collectMove, collectBuild));
    }

    //positive
    @Test
    void playerShouldNotLoseWhenBothAreNotEmpty(){
        collectMove.add(new Position(0, 0, 0));
        collectBuild.add(new Position(0, 0, 0)); // you must be sure of this beforehand
        assertFalse(loseCondition.lose(collectMove, collectBuild));
    }

    //positive
    @Test
    void throwsExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            loseCondition.lose(null, null);
        });
    }

    @Test
    void playerShouldNotPlayASingleTurn() {
        Player player1 = new Player("Pippo",1);
        Player player2 = new Player("Pluto", 2);
        Player player3 = new Player("Pluto", 3);
        Worker worker1player1 = new Worker(player1, 1);
        Worker worker2player1 = new Worker(player1, 2);
        Worker worker1player2 = new Worker(player2, 1);
        Worker worker2player2 = new Worker(player2, 2);
        Worker worker1player3 = new Worker(player3, 1);
        Worker worker2player3 = new Worker(player3, 2);
        Board board = new Board();
        board.getCell(0,0,0).setWorker(worker1player1);
        board.getCell(0,1,0).setWorker(worker2player1);
        board.getCell(1,1,0).setWorker(worker1player2);
        board.getCell(1,0,0).setWorker(worker2player2);
        board.getCell(0,2,0).setWorker(worker1player3);
        board.getCell(1,2,0).setWorker(worker2player3);

        Cell worker1Cell = board.getCell(0,0,0);
        Cell worker2Cell = board.getCell(0,1,0);

        StandardMove standardMove = new StandardMove(1);
        StandardBuild standardBuild = new StandardBuild (1);

        Turn turn = new Turn(player1);

        Set<Position> worker1moves = standardMove.move(worker1Cell.getPosition(), board, turn);
        Set<Position> worker2moves = standardMove.move(worker2Cell.getPosition(), board, turn);
        Set<Position> worker1builds = standardBuild.build(worker1Cell.getPosition(), board, turn);
        Set<Position> worker2builds = standardBuild.build(worker2Cell.getPosition(), board, turn);


        StandardLoseCondition standardLoseCondition = new StandardLoseCondition();
        Assert.assertTrue(standardLoseCondition.lose(worker1moves, worker2builds)&&standardLoseCondition.lose(worker2moves, worker2builds));




    }


}


