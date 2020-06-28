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

/**
 * This class handles the controls of the scene with the Login information phase.
 *
 * <p><p>The LoginSceneController is loaded from the <code>LoginScene.fxml</code> file.
 * <p>The player can inserts its name and the number of players they can play with. Once, and only once, they have
 * done both, they can send the information to the Client by pressing the <code>Button startMatchButton</code>.
 * <p>If the players gives an invalid name they will be sent back to the LoginScene with an error message visible
 * from the <code>Label errorLabel</code>, and they can insert a new name and number of players again.
 */

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

    /**
     * Sets the LoginScene and handles the button event.
     * <p>The <code>startMatchButton</code> when clicked sends the player name and the number of players to the Client.
     * It also saves in a cache for just the GUI in the <code>ArrayList&lt;Object&gt; playerData</code> in the <code>MainStage</code> class
     * the same information.
     * @param url
     * @param resourceBundle
     */
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


    /**
     * Enables the play button only when the player has given name and number of players.
     */
    public void keyReleasedProperty(){
        String name = playerNameTextField.getText();
        numberOfPlayersChoiceBox.setOnAction(actionEvent -> {
            numberOfPlayers = Integer.valueOf((String) numberOfPlayersChoiceBox.getValue());
            boolean isDisabled = (name.isEmpty() || name.trim().isEmpty()) || (numberOfPlayers == 0);
            startMatchButton.setDisable(isDisabled);
            return;
        });
        boolean isDisabled = (name.isEmpty() || name.trim().isEmpty()) || (numberOfPlayers == 0);
        startMatchButton.setDisable(isDisabled);
    }

    /**
     * Makes the <code>errorLabel</code> visible
     */
    public void updateErrorLabel(){
        errorLabel.setVisible(true);
    }
}

