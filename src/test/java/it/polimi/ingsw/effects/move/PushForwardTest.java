package it.polimi.ingsw.effects.move;

    import static org.junit.jupiter.api.Assertions.assertTrue;
    import static org.junit.jupiter.api.Assertions.assertFalse;
    import static org.junit.jupiter.api.Assertions.assertThrows;
    import static org.junit.jupiter.api.Assertions.assertNotNull;
    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertNotEquals;

    import it.polimi.ingsw.*;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;

    import java.util.HashSet;
    import java.util.Set;

public class PushForwardTest {
    int moves = 1;
    PushForward pushForward = new PushForward(moves);
    Board board;
    Cell workerOneCell;
    Cell workerTwoCell;
    Turn turn;
    Player playerOne = new Player("pippo",12);
    Worker workerPlayerOne = new Worker(playerOne, 12);
    Player playerTwo = new Player("ciccio",1);
    Worker workerPlayerTwo = new Worker(playerTwo,1);

    @BeforeEach
    void setUp(){
        turn = new Turn(playerOne);
        board = new Board();
    }

    //negative
    @Test
    void playerShouldBePushedForward(){
        assert board.getStream().count()==25.0;
        workerOneCell = board.getCell(1,2,0);
        workerOneCell.setWorker(workerPlayerOne);
        workerTwoCell = board.getCell(1,1,0);
        workerTwoCell.setWorker(workerPlayerTwo);
        Set<Position> collect = new HashSet<>();
        collect.add(new Position(0,1,0));
        collect.add(new Position(0,2,0));
        collect.add(new Position(0,3,0));
        collect.add(new Position(1,3,0));
        collect.add(new Position(2,3,0));
        collect.add(new Position(2,2,0));
        collect.add(new Position(2,1,0));
        collect.add(new Position(1,1,0));
        assertEquals(collect, pushForward.move(workerOneCell.getPosition(),board,turn));
    }

    //positive
    @Test
    void moveShouldReturnANewHashSetIfConditionsAreNotMet(){
        Set collect = new HashSet<>();
        workerOneCell = board.getCell(0,0,0);
        assertEquals(collect,pushForward.move(workerOneCell.getPosition(),board,turn));
    }





    @Test
    void shouldReturnTheRightXCoordinateOfTheWorkerWhenPlayerAndOpponentWorkerHaveSameX(){
        assertEquals(1,pushForward.behindWorker_x(1,1));
    }

    @Test
    void shouldReturnTheRightXCoordinateOfTheWorkerWhenPlayerHasHigherXThanOpponent(){
        assertEquals(1,pushForward.behindWorker_x(3,2));
    }

    @Test
    void shouldReturnTheRightXCoordinateOfTheWorkerWhenPlayerHasMinorXThanOpponent(){
        assertEquals(3,pushForward.behindWorker_x(1,2));
    }

    @Test
    void shouldReturnTheRightXCoordinateOfTheWorkerWhenPlayerAndOpponentWorkerHaveSameY(){
        assertEquals(1,pushForward.behindWorker_y(1,1));
    }

    @Test
    void shouldReturnTheRightXCoordinateOfTheWorkerWhenPlayerHasHigherYThanOpponent(){
        assertEquals(1,pushForward.behindWorker_y(3,2));
    }

    @Test
    void shouldReturnTheRightXCoordinateOfTheWorkerWhenPlayerHasMinorYThanOpponent(){
        assertEquals(3,pushForward.behindWorker_y(1,2));
    }


}
