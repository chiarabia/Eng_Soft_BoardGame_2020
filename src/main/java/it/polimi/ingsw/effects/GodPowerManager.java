package it.polimi.ingsw.effects;

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

    /*JSON files' path*/
    private static String getRoot(){
        String root = ClassLoader.getSystemClassLoader().getResource("configurations/Configuration.json").getPath();
        if (root.substring(0,5).equals("file:")) root = root.substring(5, root.length());
        if (root.substring(2,3).equals(":")) root = root.substring(3, root.length());
        //return root.substring(0, root.length()-18) + "cards/";
        return "src/main/resources/configurations/cards/";
    }

    /**
     * This method randomly extracts different 'numOfPlayers' cards in the form of List <String>,
     * regardless of names and/or number of cards in the Cards folder
     * @param numOfPlayers number of players
     * @return List</String>
     * @throws IOException IO Exception
     */

    private static List <String> chooseGodFiles (int numOfPlayers) throws IOException {
        List <String> cards = new ArrayList<>();
        Stream<Path> paths = Files.walk(Paths.get(getRoot()));
        paths.filter(Files::isRegularFile).forEach(x->{cards.add(x.toString().substring(getRoot().length()));});
        // the card list contains all 14 strings of JSON file names (eg "ApolloCard.json")

        Random rand = new Random();
        int numOfAvailableCards = cards.size();
        for (int i = numOfAvailableCards; i > numOfPlayers; i--) cards.remove(rand.nextInt(i));
        // only 'numOfPlayers' random strings are left in the card list

        return cards;
    }

    /**
     * This method creates a GodPower Object corresponding to the nameOfFile string. The appropriate effects are read from the JSON file.
     * @param nameOfFile JSON file's name
     * @param numOfPlayer Player ID
     */

    public static GodPower power (String nameOfFile, int numOfPlayer) throws IOException, ParseException {
        FileReader fileReader = new FileReader(getRoot()+nameOfFile);
        JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(fileReader);
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

        GodPower godPower = new GodPower(numOfPlayer, (String) jsonObject.get("name"));

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
                godPower.setBuild(new BuildBeforeMove(numOfBuilds)); break; //todo:dovrebbe essere giusto ma boh
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
                opponentsCantWinOnPerimeterPlayer = numOfPlayer; break;
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
     * Creates a list of godPowers based on the number of players: it must generate two / three different random cards,
     * then, after building them, changes the functions in case of presence of godPowers that modify the behaviors of the adversaries
     * @param numOfPlayers player ID
     * @return List</GodPower>
     * @throws ParseException
     * @throws IOException
     */

    public static List<GodPower> createGodPowers (int numOfPlayers) throws ParseException, IOException {
        opponentsCantWinOnPerimeterPlayer = 0;
        List <GodPower> godPowerList = new ArrayList<>();
        List <String> godFiles = chooseGodFiles(numOfPlayers);

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
