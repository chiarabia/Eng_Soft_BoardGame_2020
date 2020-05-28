package it.polimi.ingsw.client.gui;

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
import java.util.ResourceBundle;

public class LoginSceneController implements Initializable {

    MainStage main = new MainStage();

    @FXML
    private Button startMatchButton;
    @FXML
    private TextField playerNameTextField;
    @FXML
    private ChoiceBox numberOfPlayersChoiceBox;

    private int numberOfPlayers = 0;
    private String playerName;

    public LoginSceneController() throws IOException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Sets the options for the Choice Box to 2 or 3 players
        numberOfPlayersChoiceBox.getItems().addAll("2", "3");
        numberOfPlayersChoiceBox.setOnAction(actionEvent -> {
            numberOfPlayers = Integer.valueOf((String) numberOfPlayersChoiceBox.getValue());
        });

        startMatchButton.setOnAction(actionEvent -> {
            playerName = playerNameTextField.getText();
            main.setPlayerNameAndNumberOfPlayer(numberOfPlayers,playerName);
        });
    }

/*    // this method changes the scene to the Board Scene
    public void openSecondScene(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("BoardScene.fxml"));
            Parent root = loader.load();
            Scene boardScene = new Scene (root);

            BoardSceneController boardSceneController = loader.getController();
            boardSceneController.transferMessage(playerName.getText());

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene( boardScene);
            window.show();

        } catch (IOException ex){
            System.err.println(ex);
        }
    }*/
}