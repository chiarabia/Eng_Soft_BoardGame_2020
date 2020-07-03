package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.JSONManager;
import it.polimi.ingsw.model.effects.GodPower;
import it.polimi.ingsw.model.effects.GodPowerManager;
import it.polimi.ingsw.model.effects.build.*;
import it.polimi.ingsw.model.effects.consolidateBuild.StandardConsolidateBuild;
import it.polimi.ingsw.model.effects.consolidateBuild.UnderWorker;
import it.polimi.ingsw.model.effects.consolidateMove.PushWorker;
import it.polimi.ingsw.model.effects.consolidateMove.StandardConsolidateMove;
import it.polimi.ingsw.model.effects.consolidateMove.SwapWorker;
import it.polimi.ingsw.model.effects.move.*;
import it.polimi.ingsw.model.effects.turn.NewNoMoveUpTurn;
import it.polimi.ingsw.model.effects.turn.NewTurn;
import it.polimi.ingsw.model.effects.winCondition.FiveCompletedTowers;
import it.polimi.ingsw.model.effects.winCondition.StandardLoseCondition;
import it.polimi.ingsw.model.effects.winCondition.StandardWinCondition;
import it.polimi.ingsw.model.effects.winCondition.WinMovingDownTwoOrMoreLevels;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GodPowerManagerTest {
    GodPowerManager godPowerManager;
    GodPower godPowerLoadByCard;
    GodPower godPowerCorrect;
    String fileName;

    @BeforeEach
    void setup() {
        godPowerManager = new GodPowerManager();
        fileName = new String();
        godPowerCorrect = new GodPower(1, "Apollo");
        godPowerCorrect.setMove(new StandardMove(1));
        godPowerCorrect.setBuild(new StandardBuild(1));
        godPowerCorrect.setConsolidateBuild(new StandardConsolidateBuild());
        godPowerCorrect.setConsolidateMove(new StandardConsolidateMove());
        godPowerCorrect.addPositiveWinConditions(new StandardWinCondition());
        godPowerCorrect.setLoseCondition(new StandardLoseCondition());
        godPowerCorrect.setNewTurn(new NewTurn());
    }

    @Test
    public void Test() throws ParseException, IOException {
        List<GodPower> godPowers = GodPowerManager.createGodPowers(3);
        assert (godPowers.size() == 3);
        assert (godPowers.stream().map(x -> x != null).reduce(true, (a, b) -> a && b)); //Todo: ci sono valori per cui questa istruzione non è sempre vera
        assert (godPowers.stream().map(x -> x.getMove() != null).reduce(true, (a, b) -> a && b));
        assert (godPowers.stream().map(x -> x.getBuild() != null).reduce(true, (a, b) -> a && b));
        assert (godPowers.stream().map(x -> x.getConsolidateMove() != null).reduce(true, (a, b) -> a && b));
        assert (godPowers.stream().map(x -> x.getConsolidateBuild() != null).reduce(true, (a, b) -> a && b));
        assert (godPowers.stream().map(x -> x.getBlockingWinConditions() != null).reduce(true, (a, b) -> a && b));
        assert (godPowers.stream().map(x -> x.getPositiveWinConditions() != null).reduce(true, (a, b) -> a && b));
        assert (godPowers.stream().map(x -> x.getLoseCondition() != null).reduce(true, (a, b) -> a && b));
        assert (godPowers.stream().map(x -> x.getNewTurn() != null).reduce(true, (a, b) -> a && b));
    }

    @Test
    void ApolloShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard1.json"), 1);
        godPowerCorrect.setMove(new SwapMove(1));
        godPowerCorrect.setConsolidateMove(new SwapWorker());

        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }

    @Test
    void ArtemisShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard2.json"), 1);
        godPowerCorrect.setMove(new MoveNotOnInitialPosition(2));

        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }

    @Test
    void AthenaShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard3.json"), 1);
        godPowerCorrect.setNewTurn(new NewNoMoveUpTurn());

        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }

    @Test
    void AtlasShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard4.json"), 1);


        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()),
                () -> assertTrue(godPowerLoadByCard.isAskToBuildDomes()));
    }

    @Test
    void ChronusShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard5.json"), 1);

        godPowerCorrect.addPositiveWinConditions(new FiveCompletedTowers());

        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }

    @Test
    void DemeterShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard6.json"), 1);

        godPowerCorrect.setBuild(new NotOnSamePosition(2));

        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }

    @Test
    void HephaestusShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard7.json"), 1);

        godPowerCorrect.setBuild(new OnSamePositionBlockOnly(2));

        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }

    @Test
    void HeraShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard8.json"), 1);


        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }

    @Test
    void HestiaShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard9.json"), 1);

        godPowerCorrect.setBuild(new NotOnPerimeter(2));

        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }

    @Test
    void MinotaurShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard10.json"), 1);

        godPowerCorrect.setMove(new PushForward(1));
        godPowerCorrect.setConsolidateMove(new PushWorker());

        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }

    @Test
    void PanShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard11.json"), 1);

        godPowerCorrect.addPositiveWinConditions(new WinMovingDownTwoOrMoreLevels());

        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }

    @Test
    void PrometheusShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard12.json"), 1);

        godPowerCorrect.setMove(new NoMoveUpAfterBuild(1));
        godPowerCorrect.setBuild(new BuildBeforeMove(1));

        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }

    @Test
    void TritonShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard13.json"), 1);

        godPowerCorrect.setMove(new UnlimitedMoveOnPerimeter(1));

        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }

    @Test
    void ZeusShouldBeCorrectlyCreated() throws ParseException, IOException {
        godPowerLoadByCard = GodPowerManager.power(JSONManager.readMyJSONAsText("configurations/cards/GodCard14.json"), 1);

        godPowerCorrect.setBuild(new UnderMyself(1));
        godPowerCorrect.setConsolidateBuild(new UnderWorker());


        assertAll("Athena", () -> assertEquals(godPowerLoadByCard.getMove().getClass(), godPowerCorrect.getMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBuild().getClass(), godPowerCorrect.getBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateBuild().getClass(), godPowerCorrect.getConsolidateBuild().getClass()),
                () -> assertEquals(godPowerLoadByCard.getConsolidateMove().getClass(), godPowerCorrect.getConsolidateMove().getClass()),
                () -> assertEquals(godPowerLoadByCard.getPositiveWinConditions().getClass(), godPowerCorrect.getPositiveWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getBlockingWinConditions().getClass(), godPowerCorrect.getBlockingWinConditions().getClass()),
                () -> assertEquals(godPowerLoadByCard.getLoseCondition().getClass(), godPowerCorrect.getLoseCondition().getClass()),
                () -> assertEquals(godPowerLoadByCard.getNewTurn().getClass(), godPowerCorrect.getNewTurn().getClass()));
    }


    //TODO: decommentare
    /*
    @Test
    void AllEnemiesShouldHaveAblockingConditionBecauseofHera () throws IOException, ParseException {
        List<GodPower> godPowerList = godPowerManager.createGodPowers(14);
        List<StandardWinCondition> correctBlockingCondition = new ArrayList<>();
        correctBlockingCondition.add(new CantWinMovingOnPerimeter());
        int count = 0;

        for (int i = 0; i<godPowerList.size(); i++) {
            if (godPowerList.get(i).getBlockingWinConditions().size()!=0) {
                assertEquals(godPowerList.get(i).getBlockingWinConditions().get(0).getClass(), CantWinMovingOnPerimeter.class);
                count++;
            }
        }
        assertEquals(count, 13);
        }*/
}

