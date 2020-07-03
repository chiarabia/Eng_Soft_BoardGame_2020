package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.client.Textfields;
import it.polimi.ingsw.client.gui.MainStage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import org.json.simple.parser.ParseException;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class handles the controls for the waiting scene.
 * <p><p>The WaitingSceneController is loaded from the <code>WaitingScene.fxml</code> file.
 * <p>The waiting scene is shown when a player is waiting for the match to start. The <code>Label waitingLabel</code>
 * changes depending from waiting for a match or waiting for a player to choose their <code>GodCard</code>
 */
public class WaitingSceneController implements Initializable {
    private Textfields textfields = new Textfields();

    @FXML
    private Label waitingLabel;

    public WaitingSceneController() throws ParseException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        waitingLabel.setAlignment(Pos.CENTER);
    }

    /**
     * Changes the text on the <code>waitingLabel</code> when a match has been found and
     * the other players are choosing their <code>GodCard</code>
     */
    public void updateWaitingLabel(){
        waitingLabel.setText(textfields.getWaitgodpowers());

    }
}
