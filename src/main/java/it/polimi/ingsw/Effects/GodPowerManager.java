package it.polimi.ingsw.Effects;

import it.polimi.ingsw.Effects.Build.*;
import it.polimi.ingsw.Effects.ConsolidateBuild.StandardConsolidateBuild;
import it.polimi.ingsw.Effects.ConsolidateMove.PushWorker;
import it.polimi.ingsw.Effects.ConsolidateMove.StandardConsolidateMove;
import it.polimi.ingsw.Effects.ConsolidateMove.SwapWorker;
import it.polimi.ingsw.Effects.Move.MoveNotOnInitialPosition;
import it.polimi.ingsw.Effects.Move.PushForward;
import it.polimi.ingsw.Effects.Move.StandardMove;
import it.polimi.ingsw.Effects.Move.SwapMove;
import it.polimi.ingsw.Effects.Turn.NewTurn;
import it.polimi.ingsw.Effects.WinCondition.CantWinMovingOnPerimeter;
import it.polimi.ingsw.Effects.WinCondition.StandardLoseCondition;
import it.polimi.ingsw.Effects.WinCondition.StandardWinCondition;
import it.polimi.ingsw.Effects.WinCondition.WinMovingDownTwoOrMoreLevel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GodPowerManager {
    /*valori per tenere memoria dell'eventuale giocatore che modifica il gioco avversario (Hera ed Athena)
    * 0 significa nessuno, altrimenti 1, 2 o eventualmente 3*/
    private static int opponentsCantMoveUpAfterIDidPlayer;
    private static int opponentsCantWinOnPerimeterPlayer;

    /*Restituisce un GodPower completo di funzioni leggendo dai file JSON*/
    private static GodPower power (String nameOfDivinity, int numOfPlayer) throws IOException, ParseException {
        GodPower godPower = new GodPower();
        FileReader fileReader = new FileReader("src\\main\\java\\it\\polimi\\ingsw\\Cards\\" + nameOfDivinity + "Card.json");
        JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(fileReader);
        String move = (String) jsonObject.get("move");
        String build = (String) jsonObject.get("build");
        String consolidateMove = (String) jsonObject.get("consolidateMove");
        String consolidateBuild = (String) jsonObject.get("consolidateBuild");
        String positiveWinConditions = (String) jsonObject.get("positiveWinConditions");
        String negativeWinConditions = (String) jsonObject.get("negativeWinConditions");
        String loseConditions = (String) jsonObject.get("loseConditions");
        String newTurn = (String) jsonObject.get("newTurn");
        int numOfBuilds = Math.toIntExact((Long) jsonObject.get("numOfBuilds"));
        int numOfMoves = Math.toIntExact((Long) jsonObject.get("numOfMoves"));

        switch (move) {
            case "opponentsCantMoveUpAfterIDid":
                opponentsCantMoveUpAfterIDidPlayer = numOfPlayer;
                godPower.setMove(new StandardMove(numOfMoves)); break;
            case "unlimitedPerimetralMove":
                // todo: sistemare
            case "pushForward":
                godPower.setMove(new PushForward(numOfMoves)); break;
            case "moveNotOnInitialPosition":
                godPower.setMove(new MoveNotOnInitialPosition(numOfMoves)); break;
            case "swap":
                godPower.setMove(new SwapMove(numOfMoves)); break;
            case "":
                godPower.setMove(new StandardMove(numOfMoves)); break;
        }

        switch (build) {
            case "askToBuildBeforeMoveAndNotMoveUp":
                godPower.setAskToBuildBeforeMoveAndNotMoveUp(true);
                godPower.setBuild(new StandardBuild(numOfBuilds)); break;
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

        godPower.setConsolidateBuild(new StandardConsolidateBuild());

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
                godPower.getPositiveWinConditions().add(new WinMovingDownTwoOrMoreLevel()); break;
            case "fiveTowersCompleted":
                // todo: sistemare
            case "": break;
        }

        godPower.setNegativeWinConditions(new ArrayList());
        switch (negativeWinConditions) {
            case "opponentsCantWinOnPerimeter":
                opponentsCantWinOnPerimeterPlayer = numOfPlayer; break;
            case "": break;
        }

        godPower.setLoseCondition(new StandardLoseCondition());

        godPower.setNewTurn(new NewTurn());

        return godPower;
    }


    /* crea una lista di godPowers in base al numero di giocatori: deve generare due/tre carte casuali differenti,
       dopo averle costruite modifica alcune funzioni in presenza di divinità che modificano il comportamento degli avversari */
    public static List<GodPower> createGodPowers (int numOfPlayers) throws ParseException, IOException {
        opponentsCantMoveUpAfterIDidPlayer = 0;
        opponentsCantWinOnPerimeterPlayer = 0;
        List <GodPower> godPowerList = new ArrayList();

        // todo: variare casualmente nomi divinità per le carte da scegliere
        for (int i = 1; i <= numOfPlayers; i++)
            godPowerList.add(power("Apollo", i));

        for (int i = 1; i <= numOfPlayers; i++) {
            if (opponentsCantWinOnPerimeterPlayer!=0 && numOfPlayers!=opponentsCantWinOnPerimeterPlayer){
                godPowerList.get(i-1).getNegativeWinConditions().add(new CantWinMovingOnPerimeter()); // modifica il potere degli avversari di Hera
            }
            if (opponentsCantMoveUpAfterIDidPlayer!=0 && numOfPlayers!=opponentsCantMoveUpAfterIDidPlayer){
                //todo: modificare il potere degli avversari di Athena
            }
        }
        return godPowerList;
    }

}
