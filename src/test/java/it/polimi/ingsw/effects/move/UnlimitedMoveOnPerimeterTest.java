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

public class UnlimitedMoveOnPerimeterTest {
    int moves = 1;
    UnlimitedMoveOnPerimeter unlimitedMoveOnPerimeter = new UnlimitedMoveOnPerimeter(moves);
    Cell workerCell;
    Turn turn;
    Player player = new Player("pippo",12);
    Worker worker = new Worker(player, 12);
    Board board = new Board();

    @BeforeEach
    void SetUp(){
        turn = new Turn(player);
    }

    //positive
    @Test
    void moveConditionsShouldReturnTrueBecauseCellIsOnThePerimeter(){
        workerCell = new Cell(0,0,0);
        workerCell.setWorker(worker);
        assertTrue(unlimitedMoveOnPerimeter.checkMoveConditions(workerCell,turn));
    }

    //positive
    @Test
    void moveConditionsShouldReturnTrueBecauseCellIsOnThePerimeterEvenAfterAnotherMove(){
        board.newCell(1,0,0);
        board.newCell(0,0,0);
        Cell startingCell = board.getCell(1,0,0);
        workerCell = board.getCell(0,0,0);
        workerCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(startingCell.getPosition(),workerCell.getPosition(), board);
        workerCell.setWorker(worker);
        assertTrue(unlimitedMoveOnPerimeter.checkMoveConditions(workerCell,turn));
    }

    //positive
    @Test
    void moveConditionShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            unlimitedMoveOnPerimeter.checkMoveConditions(null, null);
        });
    }

}
