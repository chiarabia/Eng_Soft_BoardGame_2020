package it.polimi.ingsw;

import it.polimi.ingsw.effects.winCondition.StandardLoseCondition;
import it.polimi.ingsw.server.ServerView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ControllerTest {
    ArrayList<String> nameList;
    Game game;
    ServerView mockedServerView = mock(ServerView.class);
    Controller controller;


    @BeforeEach
    void setUp () {
        nameList = new ArrayList<>();
        game = new Game(3, nameList);
        mockedServerView = mock(ServerView.class);
        game.addObserver(mockedServerView);
        controller = new Controller(game, mockedServerView);
    }

    @Test
    void threeGodsPowersAreCorrectlyCreateIn3PlayerMatch () {
        setUp3PlayerMatch();
        controller.onInitialization();
        assertEquals(3, controller.getGodPowersLeft().size());
    }
    @Test
    void twoGodsPowersAreCorrectlyCreateIn3PlayerMatch () {
        setUp2PlayerMatch();
        controller.onInitialization();
        assertEquals(2, controller.getGodPowersLeft().size());
    }
    @Test
    void godPowersAreCorrectlyAddedInA3PlayersMatch () {
        setUp3PlayerMatch();
        selectAutomaticallyGodPowers();

        assertAll ("3playersGodPowers", () -> assertEquals(3, game.getGodPowers().size()),
            () -> assertNotEquals(game.getGodPowers().get(0).getGodName(), game.getGodPowers().get(1).getGodName()),
            () -> assertNotEquals(game.getGodPowers().get(1).getGodName(), game.getGodPowers().get(2).getGodName()),
            ()->  assertNotEquals(game.getGodPowers().get(0).getGodName(), game.getGodPowers().get(2).getGodName()));

    }
    @Test
    void godPowersAreCorrectlyAddedInA2PlayersMatch () {
        setUp2PlayerMatch();
        selectAutomaticallyGodPowers();

        assertAll ("2playersGodPowers", () -> assertEquals(2, game.getGodPowers().size()),
            () -> assertNotEquals(game.getGodPowers().get(0).getGodName(), game.getGodPowers().get(1).getGodName())
        );

    }

    @Test
    void workersAreCorrectlySettedInA3PlayerMatch () {
        setUp3PlayerMatch();
        selectAutomaticallyGodPowers();
        Position position11 = new Position (0,0, 0);
        Position position12 = new Position (1,1, 0);
        ArrayList<Position> player1WorkerPositions = new ArrayList<>();
        player1WorkerPositions.add(position11);
        player1WorkerPositions.add(position12);

        Position position21 = new Position (0,2, 0);
        Position position22 = new Position (2,0, 0);
        ArrayList<Position> player2WorkerPositions = new ArrayList<>();
        player2WorkerPositions.add(position21);
        player2WorkerPositions.add(position22);

        Position position31 = new Position (3,0, 0);
        Position position32 = new Position (0,3, 0);
        ArrayList<Position> player3WorkerPositions = new ArrayList<>();
        player3WorkerPositions.add(position31);
        player3WorkerPositions.add(position32);

        controller.onWorkerPositionsInitialization(1, player1WorkerPositions);
        controller.onWorkerPositionsInitialization(2, player2WorkerPositions);
        controller.onWorkerPositionsInitialization(3, player3WorkerPositions);

        assertAll("workerInitialization", () -> assertEquals(6, game.getBoard().getStream().filter(Cell::isWorker).count()),
            () -> assertTrue(game.getBoard().getCell(0,3,0).isWorker()),
            () -> assertTrue(game.getBoard().getCell(3,0,0).isWorker()),
            () -> assertTrue(game.getBoard().getCell(0,0,0).isWorker()),
            () -> assertTrue(game.getBoard().getCell(0,0,0).getPlayerId()!=game.getBoard().getCell(0,3,0).getPlayerId()));

    }

    @Test
    void onDisconnectionAllThreadsShouldBeStopped () {
        setUp3PlayerMatch();
        selectAutomaticallyGodPowers();
        controller.onPlayerDisconnection(1);
        verify(mockedServerView).stopAllEventGenerators();
    }

    @Test
    void FirstPlayerShouldLoseIstantly () {
        setUp3PlayerMatch();
        selectAutomaticallyGodPowers();
        Board board =game.getBoard();

        StandardLoseCondition playerLoseCondition = game.getPlayerGodPower(1).getLoseCondition();

        board.getCell(1,0,0).setDome(true);
        board.getCell(1,1,0).setDome(true);
        board.getCell(0,1,0).setDome(true);

        board.getCell(0,3,0).setDome(true);
        board.getCell(1,3,0).setDome(true);
        board.getCell(1,4,0).setDome(true);

        addworkers(0, 0, 0, 4, 1);

        Turn turn = new Turn(game.getPlayer(1));
        Set<Position> worker1Moves  =  game.getPlayerGodPower(1).move(game.getWorkerPosition(1,1) , board, turn );
        Set<Position> worker2Moves  =  game.getPlayerGodPower(1).move(game.getWorkerPosition(1,2) , board, turn);
        Set<Position> worker1Builds =  game.getPlayerGodPower(1).build(game.getWorkerPosition(1,1), board, turn);
        Set<Position> worker2Builds =  game.getPlayerGodPower(1).build(game.getWorkerPosition(1,2), board, turn);

        addworkers(4, 0, 4, 1, 2);
        addworkers(4, 2, 4, 3, 3);

        assertAll("player1ShouldLose", () ->  assertTrue(worker2Builds.isEmpty()),
            () -> assertTrue( worker1Moves.isEmpty()),
            () -> assertTrue( worker2Moves.isEmpty()),
            () -> assertTrue(worker1Builds.isEmpty()),
            () -> assertTrue(!game.getTurn().canDecline()
                    && playerLoseCondition.lose(worker1Moves, worker1Builds) &&
                    playerLoseCondition.lose(worker2Moves, worker2Builds)),
            () -> assertFalse(game.getTurn().canDecline()),
            () -> assertNull(game.getPlayer(1)));
    }

    @Test
    void TheLastPlayerWins() {
        setUp3PlayerMatch();
        selectAutomaticallyGodPowers();
        Board board =game.getBoard();

        StandardLoseCondition playerLoseCondition = game.getPlayerGodPower(1).getLoseCondition();

        board.getCell(1,0,0).setDome(true);
        board.getCell(1,1,0).setDome(true);
        board.getCell(0,1,0).setDome(true);

        board.getCell(0,3,0).setDome(true);
        board.getCell(1,3,0).setDome(true);
        board.getCell(1,4,0).setDome(true);

        board.getCell(3,0,0).setDome(true);
        board.getCell(3,1,0).setDome(true);
        board.getCell(4,1,0).setDome(true);

        board.getCell(3,3,0).setDome(true);
        board.getCell(3,4,0).setDome(true);
        board.getCell(4,3,0).setDome(true);

        addworkers(0, 0, 0, 4, 1);
        addworkers(4, 0, 4, 4, 2);
        addworkers(4, 2, 4, 1, 3);

        verify(mockedServerView).stopAllEventGenerators();

        assertAll("player1ShouldLose", () ->  assertNull(game.getPlayer(1)),
                () -> assertNull(game.getPlayer(2)));
    }

    @Test
    void FirstPlayerWins () {
        setUp3PlayerMatch();
        selectAutomaticallyGodPowers();
        Board board = game.getBoard();

        System.out.println("the player1 has " + game.getGodPowers().get(0).getGodName()+ " as godpower");
        System.out.println("the player2 has " + game.getGodPowers().get(1).getGodName()+ " as godpower");
        System.out.println("the player3 has " + game.getGodPowers().get(2).getGodName()+ " as godpower");

        board.getCell(0,1,0).setBuilding(true);
        board.getCell(0,2,0).setBuilding(true);
        board.getCell(0,3,0).setBuilding(true);

        board.newCell(0,1,1);
        board.newCell(0,2,1);
        board.newCell(0,3,1);
        board.getCell(0,2,1).setBuilding(true);
        board.getCell(0,3,1).setBuilding(true);

        board.newCell(0,2,2);
        board.newCell(0,3,2);

        board.getCell(0,2,2).setBuilding(true);
        board.getCell(0,3,2).setBuilding(true);
        board.newCell(0,3,3);


        addworkers(0, 0, 1, 0, 1);
        addworkers(4, 0, 4, 1, 2);
        addworkers(4, 2, 4, 3, 3);

        // First Turn
        controller.onConsolidateMove(1, 1, new Position(0, 1, 1));
        controller.onConsolidateBuild(1, new Position(0,0,0), false);
        controller.onEndedTurn(1);

        controller.onConsolidateMove(2, 1, new Position(3, 0, 0));
        controller.onConsolidateBuild(2, new Position(4,0,0), false);
        controller.onEndedTurn(2);

        controller.onConsolidateMove(3, 1, new Position(3, 2, 0));
        controller.onConsolidateBuild(3, new Position(4,2,0), false);
        controller.onEndedTurn(3);

        //Second turn
        controller.onConsolidateMove(1, 1, new Position(0, 2, 2));
        controller.onConsolidateBuild(1, new Position(0,1,1), false);
        controller.onEndedTurn(1);

        controller.onConsolidateMove(2, 1, new Position(2, 0, 0));
        controller.onConsolidateBuild(2, new Position(3,0,0), false);
        controller.onEndedTurn(2);

        controller.onConsolidateMove(3, 1, new Position(2, 2, 0));
        controller.onConsolidateBuild(3, new Position(3,2,0), false);
        controller.onEndedTurn(3);

        //third turn
        controller.onConsolidateMove(1, 1, new Position(0, 3, 3));
        controller.onConsolidateBuild(1, new Position(0,2,2), false);
        controller.onEndedTurn(1);

        verify(mockedServerView).stopAllEventGenerators();
    }

    @Test
    void workersAreCorrectlySettedInA2PlayerMatch () {
        setUp2PlayerMatch();
        selectAutomaticallyGodPowers();
        addworkers(0,0, 1,1 , 1);
        addworkers(2,0, 1,0 , 2);

        assertAll("workerInitialization", () -> assertEquals(4, game.getBoard().getStream().filter(Cell::isWorker).count()),
            () -> assertTrue(game.getBoard().getCell(0,0,0).isWorker()),
            () -> assertTrue(game.getBoard().getCell(1,1,0).isWorker()),
            () -> assertTrue(game.getBoard().getCell(2,0,0).isWorker()),
            () -> assertTrue(game.getBoard().getCell(2,0,0).getPlayerId()!=game.getBoard().getCell(0,0,0).getPlayerId()));

    }

    @Test
    void movesAreCorrectlyConsolidated () {
        setUp3PlayerMatch();
        selectAutomaticallyGodPowers();
        System.out.println("the player1 has " + game.getGodPowers().get(0).getGodName()+ " as godpower");
        System.out.println("the player2 has " + game.getGodPowers().get(1).getGodName()+ " as godpower");
        System.out.println("the player3 has " + game.getGodPowers().get(2).getGodName()+ " as godpower");
        addworkers(0, 0, 3, 1, 1);
        addworkers(0, 3, 1, 2, 2);
        addworkers(1, 4, 1, 0, 3);

        controller.onConsolidateMove(1, 1, new Position(1, 1, 0));
        assertAll ("correctMove", () -> assertTrue(game.getBoard().getCell(1,1,0).isWorker()),
            () -> assertFalse(game.getBoard().getCell(0,0,0).isWorker()));
    }

    @Test
    void buildsAreCorrectlyConsolidated () {
        setUp3PlayerMatch();
        selectAutomaticallyGodPowers();
        addworkers(0, 0, 3, 1, 1);
        addworkers(0, 3, 1, 2, 2);
        addworkers(1, 4, 1, 1, 3);

        controller.onConsolidateMove(1, 1, new Position(1, 0, 0));
        controller.onConsolidateBuild(1,new Position(2,0,0), false);
        assertAll ("correctBuild", () -> assertTrue(game.getBoard().getCell(2,0,0).isBuilding()),
                () -> assertTrue(game.getBoard().getCell(2,0,1).isFree()));
    }

    //This method setup a 3 player match
    private void setUp3PlayerMatch () {
        nameList = new ArrayList<>();
        nameList.add(0, "a");
        nameList.add(1, "b");
        nameList.add(2, "c");
        game = new Game(3, nameList);
        mockedServerView = mock(ServerView.class);
        game.addObserver(mockedServerView);
        controller = new Controller(game, mockedServerView);
    }

    //This method SetUp a 2 player match
    private void setUp2PlayerMatch () {
        nameList = new ArrayList<>();
        nameList.add(0, "a");
        nameList.add(1, "b");
        game = new Game(2, nameList);
        mockedServerView = mock(ServerView.class);
        game.addObserver(mockedServerView);
        controller = new Controller(game, mockedServerView);
    }

    private void selectAutomaticallyGodPowers () {
        controller.onInitialization();
        for (int i = 0; i < game.getPlayers().size(); i++) {
            String firstGodPower = controller.getGodPowersLeft().get(0).getGodName();
            controller.onGodPowerInitialization(i + 1, firstGodPower);
        }
    }

    private void addworkers (int worker1x, int worker1y, int worker2x, int worker2y, int playerId) {
        Position position11 = new Position (worker1x,worker1y, 0);
        Position position12 = new Position (worker2x,worker2y, 0);
        ArrayList<Position> WorkerPositions = new ArrayList<>();
        WorkerPositions.add(position11);
        WorkerPositions.add(position12);
        controller.onWorkerPositionsInitialization(playerId, WorkerPositions);
    }
}