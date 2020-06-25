package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.client.gui.MainStage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitingSceneController implements Initializable {

    @FXML
    private Label waitingLabel;
    @FXML
    private ImageView waitingImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        waitingLabel.setAlignment(Pos.CENTER);
    }

    public void updateWaitingLabel(){
        waitingLabel.setText("Waiting for other players to choose their God Power");

    }
}
