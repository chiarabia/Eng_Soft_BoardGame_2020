package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.client.ViewObserver;
import it.polimi.ingsw.client.gui.GUICache;
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
    private List<ViewObserver> observerList = new ArrayList<>();

    private int numberOfPlayers = 0;
    private String playerName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Sets the options for the Choice Box to 2 or 3 players
        numberOfPlayersChoiceBox.getItems().addAll("2", "3");
        numberOfPlayersChoiceBox.setOnAction(actionEvent -> {
            numberOfPlayers = Integer.valueOf((String) numberOfPlayersChoiceBox.getValue());
        });

        startMatchButton.setOnAction(actionEvent -> {
            playerName = playerNameTextField.getText();
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedStartup(playerName, numberOfPlayers);
            // Devi avere una instance di client
            // e poi fai Client.onCompletedStartup(.ò)
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