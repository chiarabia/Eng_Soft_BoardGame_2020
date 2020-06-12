package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.GodCard;
import it.polimi.ingsw.client.ViewObserver;
import it.polimi.ingsw.client.gui.MainStage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardSceneController implements Initializable {

    @FXML
    private GridPane gridPane;
    @FXML
    private Button moveButton;
    @FXML
    private Button buildButton;
    @FXML
    private Label playerNameLabel;
    @FXML
    private ImageView godCardImageView;
    @FXML
    private TextFlow godDescriptionTextFlow;
    @FXML
    private TextFlow notificationsTextFlow;

    GodCard chosenGodCard;
    List<String> notifications;
    List<Integer> actionsCodes;
    List<Position> workerPositions = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //get the godPower
        List<GodCard> godPowers = MainStage.getGodPowers();
        chosenGodCard = godPowers.get(0);
        //set the god card image and description
        setGodDetails(chosenGodCard);

        //get the game phase code
        actionsCodes = MainStage.getActionsCodes();
        int actionCode = actionsCodes.get(0);
        //displays the name of the player in the label on the top left
        displayPlayerName();

        //if we are in the askForWorkerInitialPositionPhase
        if (actionCode == 1){
            displayNotifications();
        }


        moveButton.setOnAction(actionEvent -> {
        });

        buildButton.setOnAction(actionEvent -> {
        });
    }

    /**
     * Sets the god image card and god description on the screen
     * @param card the God Card Object with the name of the god, the description and the image
     */
    public void setGodDetails(GodCard card){
        //set God Description
        String text = card.getGodDescription();
        Text godDescrp = new Text(text);
        godDescriptionTextFlow.getChildren().add(godDescrp);
        //set God Card
        String godCode = card.getGodImage();
        Image godCardImage = new Image("godCards/" + godCode);
        godCardImageView.setImage(godCardImage);
    }

    //displays the name of the player in the top left label
    public void displayPlayerName(){
        ArrayList<Object> playerData = MainStage.getPlayerData();
        String playerName = (String)playerData.get(0);
        playerNameLabel.setText(playerName);
    }

    //displays in the bottom right of the screen a notification for the player
    public void displayNotifications(){
        //gets the notification from the MainStage
        notifications = MainStage.getNotifications();
        //adds the String to the TextFlow
        String notificationToDisplay = notifications.get(0);
        Text notificationText = new Text(notificationToDisplay);
        notificationText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        notificationsTextFlow.getChildren().add(notificationText);
    }

    //gives the position of the workers to the observer and displays the worker on the board
    public void displayWorker(StackPane cell, Position position){
        List<ViewObserver> observerList = MainStage.getObserverList();
        //gets the clicked StackPane
        StackPane cellWorker = cell;
        //adds the worker image on top of the StackPane
        ImageView worker = new ImageView(new Image("/worker/w1.png"));
        worker.setFitHeight(100);
        worker.setFitWidth(100);
        cellWorker.getChildren().add(worker);
        //gets the Position of the Worker
        Position WorkerPosition = position;
        workerPositions.add(WorkerPosition);
        //when there are two Workers it gives them to the client
        if (workerPositions.size() == 2){
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onCompletedInitializeWorkerPositions(workerPositions);
            actionsCodes.clear();
            notificationsTextFlow.getChildren().clear();
        }
    }

    //handles the click event on a cell of the board
    public void onCellClicked(javafx.scene.input.MouseEvent event) {
        int column = GridPane.getColumnIndex((Node) event.getSource());
        int row = GridPane.getRowIndex((Node) event.getSource());
        //if we are in the askWorkerInitialPosition phase
        if(actionsCodes.get(0) == 1) {
            System.out.println(String.format("Node clicked at: column=%d, row=%d", column, row));
            //adds the StackPane of the cell that we clicked in workerCells
            StackPane cell = (StackPane) getNodeFromGridPane(gridPane, column, row);
            //adds the Position of the cell that we clicked in workerCells
            Position workerPosition = new Position(column, row, 0);
            //asks to display the worker image and store it for the observer
            displayWorker(cell,workerPosition);
            //updates the workerNumber
        }
    }

    //gets the node in a cell of the gridpane
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

}
