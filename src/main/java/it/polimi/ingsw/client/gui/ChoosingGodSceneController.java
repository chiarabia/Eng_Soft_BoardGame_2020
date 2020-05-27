package it.polimi.ingsw.client.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class ChoosingGodSceneController implements Initializable {
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
        setFirstGod("01.png", "Your move: Your Worker may move into an opponent Worker's space (using normal movement rules) and force their Worker to the space yours just vacated (swapping their positions)", "Apollo");
    }

    void setFirstGod(String godCode, String text, String name){
        //set God Description
        Text godName = setGodNameProperties(name);
        Text newLine = new Text("\n");
        Text godDescrip1 = new Text(text);
        godDescription1.getChildren().add(godName);
        godDescription1.getChildren().add(newLine);
        godDescription1.getChildren().add(godDescrip1);
        //set Portrait
        Image godPort1 = new Image("godPortraits/" + godCode );
        godPortrait1.setImage(godPort1);
        //set godPower
        Image godPow1 = new Image("godPowers/" + godCode);
        godPower1.setImage(godPow1);
    }

    void setSecondGod(String godCode, String text, String name){
        //set God Description
        Text godName = setGodNameProperties(name);
        Text newLine = new Text("\n");
        Text godDescrip2 = new Text(text);
        godDescription2.getChildren().add(godDescrip2);
        //set Portrait
        Image godPort2 = new Image("godPortraits/" + godCode );
        godPortrait2.setImage(godPort2);
        //set godPower
        Image godPow2 = new Image("godPowers/" + godCode);
        godPower2.setImage(godPow2);
    }

    void setThirdGod(String godCode, String text, String name){
        //set God Description
        Text godName = setGodNameProperties(name);
        Text newLine = new Text("\n");
        Text godDescrip3 = new Text(text);
        godDescription3.getChildren().add(godDescrip3);
        //set Portrait
        Image godPort3 = new Image("godPortraits/" + godCode );
        godPortrait3.setImage(godPort3);
        //set godPower
        Image godPow3 = new Image("godPowers/" + godCode);
        godPower3.setImage(godPow3);
    }

    Text setGodNameProperties(String name){
        Text godName = new Text(name);
        godName.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        return godName;
    }
}

