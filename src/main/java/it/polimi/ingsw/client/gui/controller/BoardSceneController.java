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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardSceneController implements Initializable {


    @FXML
    public GridPane gridPane;
    @FXML
    private BorderPane board;
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
    public TextFlow notificationsTextFlow;

    public static List<Integer> actionsCodes = new ArrayList<>();



    public static Text notification = new Text("Welcome!");


    GodCard chosenGodCard;
    List<Position> startingWorkerPositions = new ArrayList<>();
    Position newWorkerPosition;
    StackPane newWorkerCell;
    Position newBuildingPostion;
    StackPane newWorkerBuild;
    String levelOfBuilding;

    public static Text getNotification(){return notification;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //popualateGridPane(gridPane);
        board.setCenter(gridPane);
        actionsCodes.add(0);
        //get the godPower
        List<GodCard> godPowers = MainStage.getGodPowers();
        chosenGodCard = godPowers.get(0);
        //set the god card image and description
        setGodDetails(chosenGodCard);
        //displays the name of the player in the label on the top left
        displayPlayerName();

        notificationsTextFlow.getChildren().add(notification);

        moveButton.setOnAction(actionEvent -> {
            addWorkerImage(newWorkerCell,1);
        });

        buildButton.setOnAction(actionEvent -> {
            addBuildingImage(newWorkerBuild,levelOfBuilding);
        });
    }

    public static List<Integer> getActionsCodes() {return actionsCodes;}

    /**
     * Sets the god image card and god description on the screen
     * @param card the God Card Object with the name of the god, the description and the image
     */
    public void setGodDetails(GodCard card){
        MainStage.getLock().add(new Object());
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

    //gives the position of the workers to the observer and displays the worker on the board
    public void displayWorker(StackPane cell, Position position){
        List<ViewObserver> observerList = MainStage.getObserverList();
        //gets the clicked StackPane
        StackPane cellWorker = cell;
        //adds the worker image on top of the StackPane
        addWorkerImage(cellWorker,1 );
        //gets the Position of the Worker
        Position WorkerPosition = position;
        startingWorkerPositions.add(WorkerPosition);
        //when there are two Workers it gives them to the client
        if (startingWorkerPositions.size() == 2){
            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onCompletedInitializeWorkerPositions(startingWorkerPositions);
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
    public Node getNodeFromGridPane(GridPane gridPane, int x, int y) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                return node;
            }
        }
        return null;
    }

    //Enables the move button only when the player can move
    public void keyReleasedPropertyMove(){

    }

    //Enables the move button only when the player can build
    public void keyReleasedPropertyBuild(){

    }

    public void updateWorker(Position newPosition, Position oldPosition, int playerID){
        StackPane newCell = (StackPane)getNodefromPosition(gridPane, newPosition);
        StackPane oldCell = (StackPane)getNodefromPosition(gridPane, oldPosition);
        removeWorkerImage(oldCell);
        addWorkerImage(newCell,playerID);
    }

    public void updateWorkerInitialPosition (Position newPosition, int playerID){
        StackPane newCell = (StackPane)getNodefromPosition(gridPane, newPosition);
        addWorkerImage(newCell,playerID);
    }

    public void updateBuilding(Position newPosition, boolean dome){
        String level = "level1.png";
        if (dome == true) level = "dome.png";
        StackPane newBuildingCell = (StackPane)getNodefromPosition(gridPane,newPosition);
        int z = newPosition.getZ();
        if (dome == false) level = "level" + z + ".png";
        addBuildingImage(newBuildingCell,level);
    }

    //adds an imageView of the workers
    public void addWorkerImage(StackPane cell, int player){
        ImageView worker = new ImageView(new Image("/worker/w" + player +".png"));
        worker.setFitHeight(100);
        worker.setFitWidth(100);
        cell.getChildren().add(worker);
    }

    //adds an imageView of a building
    public void addBuildingImage(StackPane cell, String level){
        Image building = new Image(level);
        BackgroundSize buildingSize = new BackgroundSize(100,100, false,false, true, true);
        BackgroundImage buildingBackground= new BackgroundImage(building, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, buildingSize );
        cell.setBackground(new Background(buildingBackground));
    }

    //removes the image of a Worker
    public void removeWorkerImage(StackPane cell){
        cell.getChildren().clear();
    }

    public Node getNodefromPosition(GridPane gridPane, Position position){
        int x = position.getX();
        int y = position.getY();
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                return node;
            }
        }
        return null;
    }

    public static void updateActionCode(int code){
        actionsCodes.clear();
        actionsCodes.add(code);
    }

/*    public static void popualateGridPane(GridPane gridPane){
        StackPane newStackPane = new StackPane();
        newStackPane.setMaxHeight(100);
        newStackPane.setMaxWidth(100);
        newStackPane.setMinHeight(100);
        newStackPane.setMinWidth(100);
        gridPane.setPrefSize(500, 500);
        gridPane.setMaxWidth(500);
        gridPane.setMaxHeight(500);
        gridPane.setMinHeight(500);
        gridPane.setMinWidth(500);
        for (int x = 0; x<5 ; x++){
            for(int y=0; x<5; y++) gridPane.add(newStackPane,x,y);
        }
        Image board = new Image("/Board/SantoriniBoardOnly.png");
        BackgroundSize boardSize = new BackgroundSize(500,500, false,false, true, true);
        BackgroundImage boardBackground= new BackgroundImage(board, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, boardSize );
        gridPane.setBackground(new Background(boardBackground));
    }*/
}
