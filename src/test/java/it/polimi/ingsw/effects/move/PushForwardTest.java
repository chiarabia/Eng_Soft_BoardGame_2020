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

public class PushForwardTest {
    int moves;
    PushForward pushForward = new PushForward(moves);

    Cell workerCell;
    Turn turn;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);

    @BeforeEach
    void setUp(){
        turn = new Turn(player);
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
