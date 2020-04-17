package it.polimi.ingsw.effects.move;

    import static org.junit.jupiter.api.Assertions.assertTrue;
    import static org.junit.jupiter.api.Assertions.assertFalse;
    import static org.junit.jupiter.api.Assertions.assertThrows;
    import static org.junit.jupiter.api.Assertions.assertNotNull;
    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertNotEquals;
    import it.polimi.ingsw.Board;
    import it.polimi.ingsw.Cell;
    import it.polimi.ingsw.Player;
    import it.polimi.ingsw.Worker;
    import it.polimi.ingsw.Turn;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;

    import java.util.HashSet;
    import java.util.Set;

public class PushForwardTest {
    int moves;
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
        workerOneCell = board.getCell(1,2,0);
        workerOneCell.setWorker(workerPlayerOne);
        workerTwoCell = board.getCell(1,1,0);
        workerTwoCell.setWorker(workerPlayerTwo);
        Set<Cell> collect = new HashSet<>();
        collect.add(new Cell(1,0,0));
        assertEquals(collect,pushForward.move(workerOneCell,board,turn));
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
