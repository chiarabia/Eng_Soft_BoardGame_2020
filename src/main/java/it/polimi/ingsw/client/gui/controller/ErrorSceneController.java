package it.polimi.ingsw.client.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class handles the controls for the error scene.
 * <p><p>The ErrorSceneController is loaded from the <code>ErrorScene.fxml</code> file.
 * <p>The error scene is shown when a player disconnects or when a fatal error takes place
 */
public class ErrorSceneController implements Initializable {

    @FXML
    Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.setAlignment(Pos.CENTER);
    }

    /**
     * Prints on the screen the error for the player
     * @param notification a <code>String</code> that explains the error
     */
    public void updateErorrLabel(String notification){
        errorLabel.setText(notification);
    }
}
