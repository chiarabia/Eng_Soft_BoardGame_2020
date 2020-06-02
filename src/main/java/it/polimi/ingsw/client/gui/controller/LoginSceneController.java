package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ViewObserver;
import it.polimi.ingsw.client.gui.GUICache;
import it.polimi.ingsw.client.gui.MainStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentSkipListMap;

public class LoginSceneController implements Initializable {

    @FXML
    private Button startMatchButton;
    @FXML
    private TextField playerNameTextField;
    @FXML
    private ChoiceBox numberOfPlayersChoiceBox;


    private int numberOfPlayers = 0;
    private String playerName;
    GUICache cache = new GUICache();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        startMatchButton.setDisable(true);

        //Sets the options for the Choice Box to 2 or 3 players
        numberOfPlayersChoiceBox.getItems().addAll("2", "3");
        numberOfPlayersChoiceBox.setOnAction(actionEvent -> {
            numberOfPlayers = Integer.valueOf((String) numberOfPlayersChoiceBox.getValue());
        });

        //Sends the name and number of player to the client when the play button is clicked
        startMatchButton.setOnAction(actionEvent -> {
            System.out.print(numberOfPlayers);
            playerName = playerNameTextField.getText();
            cache.setNumberOfPlayers(numberOfPlayers);
            cache.setPlayerName(playerName);
            //Client client = new Client();
            //client.onCompletedStartup(playerName,numberOfPlayers);
            List<ViewObserver> observerList = MainStage.getObserverList();
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onCompletedStartup(playerName, numberOfPlayers);
        });

    }

    //Enables the play button only when the player has given name and number of player
    public void keyReleasedProperty(){
        String name = playerNameTextField.getText();
        numberOfPlayersChoiceBox.setOnAction(actionEvent -> {
            numberOfPlayers = Integer.valueOf((String) numberOfPlayersChoiceBox.getValue());
            boolean isDisabled = (name.isEmpty() || name.trim().isEmpty()) || (numberOfPlayers == 0);
            startMatchButton.setDisable(isDisabled);
        });
    }
}

