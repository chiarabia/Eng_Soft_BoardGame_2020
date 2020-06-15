package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.client.GodCard;
import it.polimi.ingsw.client.ViewObserver;
import it.polimi.ingsw.client.gui.MainStage;
import it.polimi.ingsw.effects.GodPower;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChoosingGodSceneController implements Initializable {

    @FXML
    HBox HBox1;
    @FXML
    HBox HBox2;
    @FXML
    HBox HBox3;

    int numberOfPlayers;

    @FXML
    TextFlow godDescription1;
    @FXML
    TextFlow godDescription2;
    @FXML
    TextFlow godDescription3;
    @FXML
    ImageView godPortrait1;
    @FXML
    ImageView godPortrait2;
    @FXML
    ImageView godPortrait3;
    @FXML
    ImageView godPower1;
    @FXML
    ImageView godPower2;
    @FXML
    ImageView godPower3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HBox3.setVisible(false);
        HBox2.setVisible(false);
        ArrayList<Object> playerData = MainStage.getPlayerData();
        List<GodCard> godPowers = MainStage.getGodPowers();
        numberOfPlayers = (Integer)playerData.get(1);
        GodCard firstGod = godPowers.get(0);
        setFirstGod(firstGod);
        int numberOfGods = godPowers.size();
        if(numberOfGods == 2){
            GodCard secondGod = godPowers.get(1);
            setSecondGod(secondGod);
            HBox2.setVisible(true);
        }
        if (numberOfGods == 3){
            GodCard thirdGod = godPowers.get(2);
            setThirdGod(thirdGod);
            HBox3.setVisible(true);
        }
    }

    void setFirstGod(GodCard firstGod){
        //set God Name
        String name1 = firstGod.getGodName();
        Text godName1 = setGodNameProperties(name1);
        Text newLine = new Text("\n");
        //Set God Description
        String text = firstGod.getGodDescription();
        Text godDescrip1 = new Text(text);
        godDescription1.getChildren().add(godName1);
        godDescription1.getChildren().add(newLine);
        godDescription1.getChildren().add(godDescrip1);
        //set Portrait
        String godCode = firstGod.getGodImage();
        Image godPort1 = new Image("godPortraits/" + godCode );
        godPortrait1.setImage(godPort1);
        //set godPower Image
        Image godPow1 = new Image("godPowers/" + godCode);
        godPower1.setImage(godPow1);
    }

    void setSecondGod(GodCard secondGod){
        //set God Name
        String name2 = secondGod.getGodName();
        Text godName2 = setGodNameProperties(name2);
        Text newLine = new Text("\n");
        //Set God Description
        String text = secondGod.getGodDescription();
        Text godDescrip2 = new Text(text);
        godDescription2.getChildren().add(godName2);
        godDescription2.getChildren().add(newLine);
        godDescription2.getChildren().add(godDescrip2);
        //set Portrait
        String godCode = secondGod.getGodImage();
        Image godPort2 = new Image("godPortraits/" + godCode );
        godPortrait2.setImage(godPort2);
        //set godPower Image
        Image godPow2 = new Image("godPowers/" + godCode);
        godPower2.setImage(godPow2);
    }

    void setThirdGod(GodCard thirdGod){
        //set God Name
        String name3 = thirdGod.getGodName();
        Text godName3 = setGodNameProperties(name3);
        Text newLine = new Text("\n");
        //set God Description
        String text = thirdGod.getGodDescription();
        Text godDescrip3 = new Text(text);
        godDescription3.getChildren().add(godName3);
        godDescription3.getChildren().add(newLine);
        godDescription3.getChildren().add(godDescrip3);
        //set Portrait
        String godCode = thirdGod.getGodImage();
        Image godPort3 = new Image("godPortraits/" + godCode );
        godPortrait3.setImage(godPort3);
        //set godPower Image
        Image godPow3 = new Image("godPowers/" + godCode);
        godPower3.setImage(godPow3);
    }

    Text setGodNameProperties(String name){
        Text godName = new Text(name);
        godName.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        return godName;
    }

    public void clickHBox(javafx.scene.input.MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        List<GodCard> godPowers = MainStage.getGodPowers();
        List<ViewObserver> observerList = MainStage.getObserverList();
        int numberOfGods = godPowers.size();
        if (clickedNode == HBox1 || clickedNode == godDescription1 || clickedNode == godPortrait1 || clickedNode == godPower1) {
            GodCard firstGod = godPowers.get(0);
            String chosenGodPower = firstGod.getGodName();
            System.out.print("clicked on 1 \n");
            godPowers.clear();
            godPowers.add(firstGod);
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onCompletedInitializeGodPower(chosenGodPower);
        }
        else if(numberOfGods == 2 && (clickedNode == HBox2 || clickedNode == godDescription2 || clickedNode == godPortrait2 || clickedNode == godPower2)){
            GodCard secondGod = godPowers.get(1);
            String chosenGodPower = secondGod.getGodName();
            System.out.print("clicked on 2 \n");
            godPowers.clear();
            godPowers.add(secondGod);
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onCompletedInitializeGodPower(chosenGodPower);
        }
        else if(numberOfGods == 3 && (clickedNode == HBox3 || clickedNode == godDescription3 || clickedNode == godPortrait3 || clickedNode == godPower3)){
            GodCard thirdGod = godPowers.get(2);
            String chosenGodPower = thirdGod.getGodName();
            godPowers.clear();
            godPowers.add(thirdGod);
                    System.out.print("clicked on 3 \n");
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onCompletedInitializeGodPower(chosenGodPower);
        }
    }
}

