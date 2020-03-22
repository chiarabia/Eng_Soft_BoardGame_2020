package it.polimi.ingsw;

import it.polimi.ingsw.Exceptions.WrongInputException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

public class Power {
    private int fromWitchLevelCanIBuildDomes;
    private Label howManyMoves;
    private Label chooseOpponentsCell;
    private Label winCondition;
    private Label limitOpponentsWinCondition;
    private Label canIBuildTwice;
    private Label canIBuildBeforeMove;
    private boolean canIBuildUnderMyself;
    private boolean canOpponentsMoveUpAfterIDid;

    public int getFromWitchLevelCanIBuildDomes() {
        return fromWitchLevelCanIBuildDomes;
    }

    public Label getHowManyMoves() {
        return howManyMoves;
    }

    public Label getChooseOpponentsCell() {
        return chooseOpponentsCell;
    }

    public Label getWinCondition() {
        return winCondition;
    }

    public Label getLimitOpponentsWinCondition() {
        return limitOpponentsWinCondition;
    }

    public Label getCanIBuildTwice() {
        return canIBuildTwice;
    }

    public Label getCanIBuildBeforeMove() {
        return canIBuildBeforeMove;
    }

    public boolean getCanIBuildUnderMyself() {
        return canIBuildUnderMyself;
    }

    public boolean getCanOpponentsMoveUpAfterIDid() {
        return canOpponentsMoveUpAfterIDid;
    }

    public Power(String nameOfDivinity) throws IOException, ParseException, WrongInputException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src\\main\\java\\it\\polimi\\ingsw\\Cards\\"+nameOfDivinity+"Card.json"));
        int fromWitchLevelCanIBuildDomes =  Math.toIntExact((Long) jsonObject.get("fromWhichLevelCanIBuildDomes"));
        int howManyMoves =  Math.toIntExact((Long)jsonObject.get("howManyMoves"));
        int chooseOpponentsCell =  Math.toIntExact((Long) jsonObject.get("chooseOpponentsCell"));
        int winCondition =  Math.toIntExact((Long) jsonObject.get("winCondition"));
        int limitOpponentsWinCondition =  Math.toIntExact((Long) jsonObject.get("limitOpponentsWinCondition"));
        int canIBuildTwice =  Math.toIntExact((Long) jsonObject.get("canIBuildTwice"));
        int canIBuildBeforeMove =  Math.toIntExact((Long) jsonObject.get("canIBuildBeforeMove"));
        boolean canIBuildUnderMyself= (Boolean) jsonObject.get("canIBuildUnderMyself");
        boolean canOpponentsMoveUpAfterIDid = (Boolean) jsonObject.get("canOpponentsMoveUpAfterIDid");

        if (!((fromWitchLevelCanIBuildDomes>=0)&&(fromWitchLevelCanIBuildDomes<=3)&&
            (howManyMoves>=1)&&(howManyMoves<=3)&&
            (chooseOpponentsCell>=1)&&(chooseOpponentsCell<=3)&&
            (winCondition>=1)&&(winCondition<=3)&&
            (limitOpponentsWinCondition>=1)&&(limitOpponentsWinCondition<=2)&&
            (canIBuildTwice>=1)&&(canIBuildTwice<=5)&&
            (canIBuildBeforeMove>=1)&&(canIBuildBeforeMove<=3))) throw new WrongInputException();

        this.canIBuildUnderMyself = canIBuildUnderMyself;
        this.canOpponentsMoveUpAfterIDid = canOpponentsMoveUpAfterIDid;
        this.fromWitchLevelCanIBuildDomes = fromWitchLevelCanIBuildDomes;
        switch(howManyMoves){
            case 1: this.howManyMoves = Label.ONE; break;
            case 2: this.howManyMoves = Label.TWO; break;
            case 3: this.howManyMoves = Label.AGAIN_IF_MOVE_ON_PERIMETER; break;
        }
        switch(chooseOpponentsCell){
            case 1: this.chooseOpponentsCell = Label.NEVER; break;
            case 2: this.chooseOpponentsCell = Label.SWAP; break;
            case 3: this.chooseOpponentsCell = Label.MOVE_OPPONENT_ON_SAME_DIRECTION; break;
        }
        switch(winCondition){
            case 1: this.winCondition = Label.REGULAR; break;
            case 2: this.winCondition = Label.IF_I_MOVE_DOWN_TWO_LEVELS_OR_MORE; break;
            case 3: this.winCondition = Label.FIVE_TOWERS_COMPLETED; break;
        }
        switch(limitOpponentsWinCondition){
            case 1: this.limitOpponentsWinCondition = Label.NEVER; break;
            case 2: this.limitOpponentsWinCondition = Label.MOVE_ON_PERIMETER; break;
        }
        switch(canIBuildTwice){
            case 1: this.canIBuildTwice = Label.NEVER; break;
            case 2: this.canIBuildTwice = Label.NOT_ON_SAME_CELL; break;
            case 3: this.canIBuildTwice = Label.ON_SAME_CELL_BLOCK_ONLY; break;
            case 4: this.canIBuildTwice = Label.NOT_ON_PERIMETER; break;
            case 5: this.canIBuildTwice = Label.ALWAYS; break;
        }
        switch(canIBuildBeforeMove){
            case 1: this.canIBuildBeforeMove = Label.NEVER; break;
            case 2: this.canIBuildBeforeMove = Label.IF_I_DONT_MOVE_UP; break;
            case 3: this.canIBuildBeforeMove = Label.ALWAYS; break;
        }
    }
}