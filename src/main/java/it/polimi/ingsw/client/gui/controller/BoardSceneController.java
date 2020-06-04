package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.client.GodCard;
import it.polimi.ingsw.client.gui.MainStage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardSceneController implements Initializable {

    @FXML
    private Button moveButton;
    @FXML
    private Button buildButton;
    @FXML
    private Label playerNameLabel;
    @FXML
    private ImageView godCard;
    @FXML
    private TextFlow godDescription;

    GodCard chosenGodCard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<GodCard> godPowers = MainStage.getGodPowers();
        chosenGodCard = godPowers.get(0);
        setGodDetails(chosenGodCard);
        displayPlayerName();

        moveButton.setOnAction(actionEvent -> {
            moveButton.getStyleClass().removeAll("moveButton, focus");
            //moveButton.getStylesheets().add(getClass().getResource("/CSS/ButtonClicked.css").toExternalForm());
        });

        buildButton.setOnAction(actionEvent -> {
            buildButton.getStyleClass().removeAll("buildButton, focus");
            //moveButton.getStylesheets().add(getClass().getResource("/CSS/ButtonClicked.css").toExternalForm());
        });
    }

    public void setGodDetails(GodCard card){
        //set God Description
        String text = card.getGodDescription();
        Text godDescrp = new Text(text);
        godDescription.getChildren().add(godDescrp);
        //set God Card
        String godCode = card.getGodImage();
        Image godCardImage = new Image("godCards/" + godCode);
        godCard.setImage(godCardImage);
    }

    public void displayPlayerName(){
        ArrayList<Object> playerData = MainStage.getPlayerData();
        String playerName = (String)playerData.get(0);
        playerNameLabel.setText(playerName);
    }

}
