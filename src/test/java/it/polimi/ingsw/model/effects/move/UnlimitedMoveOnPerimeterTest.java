package it.polimi.ingsw.model.effects.move;

    import static org.junit.jupiter.api.Assertions.assertTrue;
    import static org.junit.jupiter.api.Assertions.assertFalse;
    import static org.junit.jupiter.api.Assertions.assertThrows;
    import static org.junit.jupiter.api.Assertions.assertNotNull;
    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertNotEquals;

    import it.polimi.ingsw.model.*;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;

    import java.util.HashSet;
    import java.util.Set;

public class UnlimitedMoveOnPerimeterTest {
    int moves = 2;
    UnlimitedMoveOnPerimeter unlimitedMoveOnPerimeter = new UnlimitedMoveOnPerimeter(moves);
    Cell workerCell;
    Turn turn;
    Player player = new Player("pippo",12);
    Player player2 = new Player("ciccio",3);
    Worker worker = new Worker(player, 12);
    Worker worker2 = new Worker(player,13);
    Board board = new Board();

    @BeforeEach
    void SetUp(){
        turn = new Turn(player);
    }

    //positive
    @Test
    void moveShouldWorkLikeStandardMove(){
        Cell cellWorker=board.getCell(0,0,0);
        cellWorker.setWorker(worker);
        Set<Position> collect = new HashSet<>();
        collect.add(new Position(1,0,0));
        collect.add(new Position (0,1,0));
        collect.add(new Position (1,1,0));
        assertEquals(collect,unlimitedMoveOnPerimeter.move(cellWorker.getPosition(),board,turn));
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
    void moveConditionShouldBeFalseBecauseMissingWorker (){
        workerCell = board.getCell(0,0,0);
        assertFalse(unlimitedMoveOnPerimeter.checkMoveConditions(workerCell,turn));
    }

    //positive
    @Test
    void buildConditionShouldBeFalseBecauseTheWorkerMovingASecondTimeHasADifferentId(){
        workerCell = board.getCell(0,0,0);
        workerCell.setWorker(worker);
        Cell workerCellFirst = board.getCell(1,0,0);
        Cell workerStartingCell = board.getCell(2,0,0);
        workerCellFirst.setWorker(worker2);
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerCellFirst.getPosition(),board);
        assertFalse(unlimitedMoveOnPerimeter.checkMoveConditions(workerCell,turn));
    }


    //positive
    @Test
    void moveConditionsShouldBeFalseBecauseTurnPlayerIdIsNotTheSameOfThePlayerId(){
        workerCell = board.getCell(0,0,0);
        workerCell.setWorker(worker);
        Turn turn2 = new Turn(player2);
        assertFalse(unlimitedMoveOnPerimeter.checkMoveConditions(workerCell,turn2));
    }

    //positive
    @Test
    void moveConditionShouldReturnTrueIfTheWorkerHasAlreadyMovedAndTheCellIsNotPerimetral() {
        workerCell = board.getCell(1,1,0);
        Cell workerStartingCell = board.getCell(0,0,0);
        workerCell.setWorker(worker);
        turn.updateTurnInfoAfterMove(workerStartingCell.getPosition(),workerCell.getPosition(),board);
        assertTrue(unlimitedMoveOnPerimeter.checkMoveConditions(workerCell, turn));
    }

    //positive
    @Test
    void moveConditionShouldThrowExceptionWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            unlimitedMoveOnPerimeter.checkMoveConditions(null, null);
        });
    }

}
