package it.polimi.ingsw.effects;

import it.polimi.ingsw.JSONManager;
import it.polimi.ingsw.effects.build.*;
import it.polimi.ingsw.effects.consolidateBuild.StandardConsolidateBuild;
import it.polimi.ingsw.effects.consolidateBuild.UnderWorker;
import it.polimi.ingsw.effects.consolidateMove.PushWorker;
import it.polimi.ingsw.effects.consolidateMove.StandardConsolidateMove;
import it.polimi.ingsw.effects.consolidateMove.SwapWorker;
import it.polimi.ingsw.effects.move.*;
import it.polimi.ingsw.effects.turn.NewNoMoveUpTurn;
import it.polimi.ingsw.effects.turn.NewTurn;
import it.polimi.ingsw.effects.winCondition.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class GodPowerManager {
    /*values​to keep memory of the player who changes the powers of the opponents (e.g: Hera, Athena, etc...)
     * 0 means nobody, otherwise 1, 2 or possibly 3
     */
    private static int opponentsCantWinOnPerimeterPlayer;

    /**
     * Randomly extracts different 'numOfPlayers' cards in the form of <code>List&lt;JSONObject&gt;</code>,
     * regardless of the number of cards in the Cards folder.
     * @param numOfPlayers number of players
     * @return <code>List&lt;JSONObject&gt;</code>
     * @throws ParseException ParseException
     */

    private static List <JSONObject> chooseGodFiles (int numOfPlayers) throws ParseException {
        List <JSONObject> cards = new ArrayList<>();
        int counter = 1;
        while (true){
            JSONObject card = JSONManager.readMyJSONAsText("configurations/cards/GodCard"+counter+".json");
            if (card==null) break;
            cards.add(card);
            counter++;
        }
        // the card list contains all 14 strings of JSON file names (eg "GodCard1.json")

        Random rand = new Random();
        int numOfAvailableCards = cards.size();
        for (int i = numOfAvailableCards; i > numOfPlayers; i--) cards.remove(rand.nextInt(i));
        // only 'numOfPlayers' random strings are left in the card list


        cards = new ArrayList<>();
        cards.add(JSONManager.readMyJSONAsText("configurations/cards/GodCard1.json"));
        cards.add(JSONManager.readMyJSONAsText("configurations/cards/GodCard2.json"));
        cards.add(JSONManager.readMyJSONAsText("configurations/cards/GodCard12.json"));

        return cards;
    }

    /**
     * Creates a GodPower object corresponding to a JSONObject.
     * @param jsonObject JSONObject
     * @param playerId playerId
     * @return //TODO add return
     */

    public static GodPower power (JSONObject jsonObject, int playerId) {
        //Effects' strings
        String move = (String) jsonObject.get("move");
        String build = (String) jsonObject.get("build");
        String consolidateMove = (String) jsonObject.get("consolidateMove");
        String consolidateBuild = (String) jsonObject.get("consolidateBuild");
        String positiveWinConditions = (String) jsonObject.get("positiveWinConditions");
        String blockingWinConditions = (String) jsonObject.get("negativeWinConditions");
        String loseConditions = (String) jsonObject.get("loseConditions");
        String newTurn = (String) jsonObject.get("newTurn");

        int numOfBuilds = Math.toIntExact((Long) jsonObject.get("numOfBuilds"));
        int numOfMoves = Math.toIntExact((Long) jsonObject.get("numOfMoves"));

        GodPower godPower = new GodPower(playerId, (String) jsonObject.get("name"));

        switch (move) {
            case "unlimitedPerimetralMove":
                godPower.setMove(new UnlimitedMoveOnPerimeter(numOfMoves)); break;
            case "pushForward":
                godPower.setMove(new PushForward(numOfMoves)); break;
            case "moveNotOnInitialPosition":
                godPower.setMove(new MoveNotOnInitialPosition(numOfMoves)); break;
            case "swap":
                godPower.setMove(new SwapMove(numOfMoves)); break;
            case "noMoveUpAfterBuild":
                godPower.setMove(new NoMoveUpAfterBuild(1)); break;
            case "":
                godPower.setMove(new StandardMove(numOfMoves)); break;
        }

        switch (build) {
            case "askToBuildBeforeMoveAndNotMoveUp":
                godPower.setBuild(new BuildBeforeMove(numOfBuilds)); break;
            case "buildBeforeMove":
                godPower.setBuild(new BuildBeforeMove(1)); break;
            case "askToBuildDomes":
                godPower.setAskToBuildDomes(true);
                godPower.setBuild(new StandardBuild(numOfBuilds)); break;
            case "underMyself":
                godPower.setBuild(new UnderMyself(numOfBuilds)); break;
            case "notOnPerimeter":
                godPower.setBuild(new NotOnPerimeter(numOfBuilds)); break;
            case "onSamePositionBlockOnly":
                godPower.setBuild(new OnSamePositionBlockOnly(numOfBuilds)); break;
            case "notOnSamePosition":
                godPower.setBuild(new NotOnSamePosition(numOfBuilds)); break;
            case "":
                godPower.setBuild(new StandardBuild(numOfBuilds)); break;
        }

        switch (consolidateBuild) {
            case "underWorker" :
                godPower.setConsolidateBuild(new UnderWorker()); break;
            case "" :
                godPower.setConsolidateBuild(new StandardConsolidateBuild()); break;

        }

        switch (consolidateMove) {
            case "pushWorker":
                godPower.setConsolidateMove(new PushWorker()); break;
            case "swapWorker":
                godPower.setConsolidateMove(new SwapWorker()); break;
            case "":
                godPower.setConsolidateMove(new StandardConsolidateMove()); break;
        }

        godPower.setPositiveWinConditions(new ArrayList());
        godPower.getPositiveWinConditions().add(new StandardWinCondition());

        switch (positiveWinConditions) {
            case "winMovingDownTwoOrMoreLevels":
                godPower.getPositiveWinConditions().add(new WinMovingDownTwoOrMoreLevels()); break;
            case "fiveCompletedTowers":
                godPower.getPositiveWinConditions().add(new FiveCompletedTowers()); break;
            case "": break;
        }

        godPower.setBlockingWinConditions(new ArrayList());
        switch (blockingWinConditions) {
            case "opponentsCantWinOnPerimeter":
                opponentsCantWinOnPerimeterPlayer = playerId; break;
            case "": break;
        }

        godPower.setLoseCondition(new StandardLoseCondition());

        switch (newTurn) {
            case "newNoMoveUpTurn":
                godPower.setNewTurn(new NewNoMoveUpTurn()); break;
            case "":
                godPower.setNewTurn(new NewTurn()); break;
        }

        return godPower;
    }

    /**
     * Creates a list of godPowers based on the number of players: it must generate two or three different random cards,
     * then, after building them, changes the functions in case of presence of godPowers that modify the behaviors of the adversaries
     * @param numOfPlayers number of players
     * @return <code>List&lt;GodPower&gt;</code>
     * @throws ParseException //TODO add descrption
     * @throws IOException  //TODO add description
     */

    public static List<GodPower> createGodPowers (int numOfPlayers) throws ParseException, IOException {
        opponentsCantWinOnPerimeterPlayer = 0;
        List <GodPower> godPowerList = new ArrayList<>();
        List <JSONObject> godFiles = chooseGodFiles(numOfPlayers);

        for (int i = 1; i <= numOfPlayers; i++)
            godPowerList.add(power(godFiles.get(i-1), i));

        for (int i = 1; i <= numOfPlayers; i++) {
            if (opponentsCantWinOnPerimeterPlayer!=0 && i!=opponentsCantWinOnPerimeterPlayer){
                godPowerList.get(i-1).getBlockingWinConditions().add(new CantWinMovingOnPerimeter()); // changes the power of Hera's opponents
            }
        }
        return godPowerList;
    }

}
