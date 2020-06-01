package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.client.GodCard;
import it.polimi.ingsw.client.gui.GUICache;
import it.polimi.ingsw.effects.GodPower;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChoosingGodSceneController implements Initializable {

    GUICache cache = new GUICache();

    List<GodCard> godPowers;
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

    public ChoosingGodSceneController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //setFirstGod("01.png", "Your move: Your Worker may move into an opponent Worker's space (using normal movement rules) and force their Worker to the space yours just vacated (swapping their positions)", "Apollo");
        godPowers = cache.getGodPowers();
        numberOfPlayers = cache.getNumberOfPlayers();
        GodCard firstGod = godPowers.get(0);
        setFirstGod(firstGod);
        GodCard secondGod = godPowers.get(1);
        setSecondGod(secondGod);
        if (numberOfPlayers == 3) {
            GodCard thirdGod = godPowers.get(2);

        }

    }



    void setFirstGod(GodCard firstGod){
        //set God Name
        String name = firstGod.getGodName();
        Text godName = setGodNameProperties(name);
        Text newLine = new Text("\n");
        //Set God Description
        String text = firstGod.getGodDescription();
        Text godDescrip1 = new Text(text);
        godDescription1.getChildren().add(godName);
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
        String name = secondGod.getGodName();
        Text godName = setGodNameProperties(name);
        Text newLine = new Text("\n");
        //Set God Description
        String text = secondGod.getGodDescription();
        Text godDescrip2 = new Text(text);
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
        String name = thirdGod.getGodName();
        Text godName = setGodNameProperties(name);
        Text newLine = new Text("\n");
        //set God Description
        String text = thirdGod.getGodDescription();
        Text godDescrip3 = new Text(text);
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
}

