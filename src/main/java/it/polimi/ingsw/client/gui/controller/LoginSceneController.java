package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.client.ViewObserver;
import it.polimi.ingsw.client.gui.MainStage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class LoginSceneController implements Initializable {

    @FXML
    private Label errorLabel;
    @FXML
    private Button startMatchButton;
    @FXML
    private TextField playerNameTextField;
    @FXML
    private ChoiceBox numberOfPlayersChoiceBox;


    private int numberOfPlayers = 0;
    private String playerName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.setVisible(false);
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
            ArrayList<Object> playerData = MainStage.getPlayerData();
            playerData.add(playerName);
            playerData.add(numberOfPlayers);
            List<ViewObserver> observerList = MainStage.getObserverList();
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onCompletedStartup(playerName, numberOfPlayers);
        });

    }

    //Enables the play button only when the player has given name and number of players
    public void keyReleasedProperty(){
        String name = playerNameTextField.getText();
        numberOfPlayersChoiceBox.setOnAction(actionEvent -> {
            numberOfPlayers = Integer.valueOf((String) numberOfPlayersChoiceBox.getValue());
            boolean isDisabled = (name.isEmpty() || name.trim().isEmpty()) || (numberOfPlayers == 0);
            startMatchButton.setDisable(isDisabled);
        });
    }

    public void updateErrorLabel(){
        errorLabel.setVisible(true);
    }
}

