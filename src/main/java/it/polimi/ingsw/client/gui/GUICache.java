package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GodCard;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GUICache {

    Stage primaryStage;
    int numberOfPlayers = 2;
    String playerName;
    List<GodCard> godPowers;


    public int getNumberOfPlayers(){return numberOfPlayers;}
    public String getPlayerName(){return playerName;}
    public List<GodCard> getGodPowers(){return godPowers;}

    public void setGodPowers(List<GodCard> godsNames){godPowers = godsNames;}

    public void setStage(Stage primaryStage){
        primaryStage.setTitle("Santorini");
        primaryStage.setMinHeight(774);
        primaryStage.setMinWidth(1386);
    }

    public Stage getPrimaryStage(){return primaryStage;}
}
