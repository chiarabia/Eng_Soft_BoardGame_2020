package it.polimi.ingsw.client.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardSceneController implements Initializable {

    @FXML
    private Label playersName;
    @FXML
    private ImageView godCard;
    @FXML
    private TextFlow godDescription;

    //sets the name of the player in the Label
    public void transferMessage(String message){
        playersName.setText(message);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setGodDetails(String godCode, String text){
        //set God Description
        Text godDescrp = new Text(text);
        godDescription.getChildren().add(godDescrp);
        //set God Card
        Image godCardImage = new Image("godCards/" + godCode);
        godCard.setImage(godCardImage);
    }
}
