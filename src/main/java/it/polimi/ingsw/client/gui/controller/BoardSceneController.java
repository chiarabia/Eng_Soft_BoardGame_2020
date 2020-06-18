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
import java.util.Set;
import java.util.stream.Collectors;

public class BoardSceneController implements Initializable {


    @FXML
    public GridPane gridPane;
    @FXML
    private Button moveButton;
    @FXML
    private Button buildButton;
    @FXML
    private Button domeButton;
    @FXML
    private Button declineButton;
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
    public boolean isMovePossible = false;
    public boolean isBuildPossible = false;
    public boolean isDomeAtAnyLevelPossible = false;
    public boolean isDeclinePossible = false;

    boolean moveButtonActive = false;
    boolean buildButtonActive = false;
    boolean domeButtonActive = false;
    boolean declineButtonActive = false;

    boolean firstTimeSelectedCell = true;
    boolean firstTimeWorkerOne = true;
    boolean firstTimeWorkerTwo = true;
    StackPane previousCell;
    boolean isMoveActionPossible = false;
    boolean isBuildActionPossible = false;

    private boolean isWorkerSelected = false;

    ImageView selectedImage = new ImageView(new Image("/components/selectedCell.png"));

    GodCard chosenGodCard;
    List<Position> startingWorkerPositions = new ArrayList<>();
    List<StackPane> worker1Moves = new ArrayList<>();
    List<StackPane> worker2Moves = new ArrayList<>();
    List<StackPane> worker1Builds = new ArrayList<>();
    List<StackPane> worker2Builds = new ArrayList<>();
    Set<Position> worker1MovesPosition;
    Set<Position> worker2MovesPosition;
    Set<Position> worker1BuildsPosition;
    Set<Position> worker2BuildsPosition;
    Position oldFirstWorkerPosition = new Position(0,0,0);
    Position oldSecondWorkerPosition = new Position (0,0,0);
    int workerSelected;
    Position newWorkerPosition;
    StackPane newWorkerCell;
    Position newBuildingPostion;
    StackPane newWorkerBuild;
    String levelOfBuilding;

     @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         moveButton.setDisable(true);
         buildButton.setDisable(true);
         declineButton.setDisable(true);
         domeButton.setDisable(true);
         domeButton.setVisible(false);
         declineButton.setVisible(false);
        //sets default actioncode
        actionsCodes.add(0);
        //get the godPower
        List<GodCard> godPowers = MainStage.getGodPowers();
        chosenGodCard = godPowers.get(0);
        //set the god card image and description
        setGodDetails(chosenGodCard);
        //displays the name of the player in the label on the top left
        displayPlayerName();

        //adds the notification Text to the TextFlow in the bottom right
        //setTextFormat(notification);
        notificationsTextFlow.getChildren().add(notification);

        moveButton.setOnAction(actionEvent -> {
            List<ViewObserver> observerList = MainStage.getObserverList();
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedMove(newWorkerPosition, workerSelected);
        });

        buildButton.setOnAction(actionEvent -> {
            List<ViewObserver> observerList = MainStage.getObserverList();
            boolean isDome = false;
            if(newBuildingPostion.getZ()==4) isDome = true;
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedBuild(newBuildingPostion, workerSelected, isDome);
        });

        domeButton.setOnAction(actionEvent -> {
            List<ViewObserver> observerList = MainStage.getObserverList();
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedBuild(newBuildingPostion, workerSelected, true);
        });

        declineButton.setOnAction(actionEvent -> {
            List<ViewObserver> observerList = MainStage.getObserverList();
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedDecline();
        });
    }


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

    //gives the position of initial workers to the observer and displays the workers on the board
    public void displayWorker(StackPane cell, Position position){
        List<ViewObserver> observerList = MainStage.getObserverList();
        ArrayList<Object> playerData = MainStage.getPlayerData();
        //gets the clicked StackPane
        StackPane cellWorker = cell;
        //adds the worker image on top of the StackPane
        addWorkerImage(cellWorker, (Integer)playerData.get(2));
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
        if(actionsCodes.get(0) == 2){
            StackPane firstWorkerCell = (StackPane)getNodeFromPosition(gridPane,oldFirstWorkerPosition);
            StackPane secondWorkerCell = (StackPane)getNodeFromPosition(gridPane,oldSecondWorkerPosition);

            StackPane cell = (StackPane) getNodeFromGridPane(gridPane, column, row);
            if(cell == firstWorkerCell || cell == secondWorkerCell){
                isWorkerSelected = true;
                if(cell == firstWorkerCell) workerSelected=1;
                if(cell == secondWorkerCell) workerSelected=2;
            }
            //if (firstTimeSelectedCell = false) addSelectedImageToCell(previousCell,2,selectedImage);
            //addSelectedImageToCell(cell,1,selectedImage);

            if(isWorkerSelected && isMovePossible){
                if(workerSelected == 1){
                    convertPositionListToStackPaneList(worker1MovesPosition,1,1);
                    if(firstTimeWorkerOne) displayNotificationsDuringTurn("You choose the " + workerSelected + " worker \n");
                    isMoveActionPossible = isCellActionPossible(cell,worker1Moves);
                    newWorkerPosition = addZToPosition(column,row,worker1MovesPosition);
                    firstTimeWorkerOne = false;
                    if(!firstTimeWorkerTwo) firstTimeWorkerTwo = true;
                }
                if(workerSelected == 2){
                    convertPositionListToStackPaneList(worker2MovesPosition,2,1);
                    if(firstTimeWorkerTwo) displayNotificationsDuringTurn("You choose the " + workerSelected + " worker \n");
                    isMoveActionPossible = isCellActionPossible(cell,worker2Moves);
                    newWorkerPosition = addZToPosition(column,row,worker2MovesPosition);
                    firstTimeWorkerTwo = false;
                    if(!firstTimeWorkerOne) firstTimeWorkerOne = true;
                }
                moveButton.setDisable(!isMoveActionPossible);
                previousCell = cell;
                //firstTimeSelectedCell = false;
            }

            if(!isMovePossible)moveButton.setDisable(true);

            if(isWorkerSelected && isBuildPossible){
               if(workerSelected == 1){
                   convertPositionListToStackPaneList(worker1BuildsPosition,1,2);
                   isBuildActionPossible = isCellActionPossible(cell,worker1Builds);
                   newBuildingPostion = addZToPosition(column,row,worker1BuildsPosition);
                   if(worker1BuildsPosition.isEmpty()) displayNotificationsDuringTurn("The worker you choose cannot build \n");
               }
               if(workerSelected ==2){
                   convertPositionListToStackPaneList(worker2BuildsPosition,2,2);
                   isBuildActionPossible = isCellActionPossible(cell,worker2Builds);
                   newBuildingPostion = addZToPosition(column,row,worker2BuildsPosition);
                   if(worker2BuildsPosition.isEmpty()) displayNotificationsDuringTurn("The worker you choose cannot build \n");
               }
               buildButton.setDisable(!isBuildActionPossible);
               previousCell = cell;
               //firstTimeSelectedCell = false;
            }
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

    public void updateWorker(Position newPosition, Position oldPosition, int playerID){
        StackPane newCell = (StackPane)getNodeFromPosition(gridPane, newPosition);
        StackPane oldCell = (StackPane)getNodeFromPosition(gridPane, oldPosition);
        removeWorkerImage(oldCell);
        addWorkerImage(newCell,playerID);
    }

    public void updateWorkerInitialPosition (Position newPosition, int playerID){
        StackPane newCell = (StackPane)getNodeFromPosition(gridPane, newPosition);
        addWorkerImage(newCell,playerID);
    }

    public void updateBuilding(Position newPosition, boolean dome){
        String level = "level1.png";
        if (dome == true) level = "dome.png";
        StackPane newBuildingCell = (StackPane)getNodeFromPosition(gridPane,newPosition);
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
        BackgroundImage buildingBackground = new BackgroundImage(building, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, buildingSize );
        cell.setBackground(new Background(buildingBackground));
    }

    //removes the image of a Worker
    public void removeWorkerImage(StackPane cell){
        cell.getChildren().clear();
    }

    public Node getNodeFromPosition(GridPane gridPane, Position position){
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

    public void updateStackPaneLists(Set<Position> worker1Move, Set<Position> worker2Move, Set<Position> worker1Build,Set<Position> worker2Build){
        convertPositionListToStackPaneList(worker1Move,1,1);
        convertPositionListToStackPaneList(worker2Move,2,1);
        convertPositionListToStackPaneList(worker1Build,1,2);
        convertPositionListToStackPaneList(worker2Build,2,2);
    }

    public void convertPositionListToStackPaneList(Set<Position> set, int workerID, int typeOfAction ){
        List<Position> list = set.stream().collect(Collectors.toCollection(ArrayList::new));
        for(int i=0; i<list.size();i++){
            StackPane convertedStackPane = (StackPane) getNodeFromPosition(gridPane,list.get(i));
            if(workerID == 1 && typeOfAction ==1)
                worker1Moves.add(convertedStackPane);
            if(workerID == 2 && typeOfAction == 1)
                worker2Moves.add(convertedStackPane);
            if(workerID == 1 && typeOfAction == 2)
                worker1Builds.add(convertedStackPane);
            if(workerID == 2 && typeOfAction == 2)
                worker2Builds.add(convertedStackPane);
        }
    }

    public boolean isCellActionPossible(StackPane cell, List<StackPane> list){
        boolean isStackPaneInList = false;
        if (list.contains(cell)) isStackPaneInList = true;
        return isStackPaneInList;
    }

    public Position addZToPosition(int x, int y, Set<Position> set){
        List<Position> list = set.stream().collect(Collectors.toCollection(ArrayList::new));
        Position newPosition = new Position(0,0,0);
        for(int i=0; i<list.size();i++) {
            Position listPosition = list.get(i);
            if(x == listPosition.getX() && y == listPosition.getY())
                newPosition = new Position (x,y,listPosition.getZ());
        }
        return newPosition;
    }

    public void addSelectedImageToCell(StackPane cell, int code, ImageView selectedImage){
        if (code == 1) cell.getChildren().add(selectedImage);
        if (code == 2) cell.getChildren().remove(selectedImage);
    }

    public void displayNotificationsDuringTurn(String notification){
        Text notificationText = new Text(notification);
        setTextFormat(notificationText);
        notificationsTextFlow.getChildren().add(notificationText);
    }

    public void setMovePossible (boolean isPossible) {isMovePossible = isPossible;}
    public void setBuildPossible (boolean isPossible) {isBuildPossible = isPossible;}
    public void setDomeAtAnyLevelPossible (boolean isPossible) {isDomeAtAnyLevelPossible = isPossible;}
    public void setDeclinePossible (boolean isPossible) {isDeclinePossible = isPossible;}
    public void setOldFirstWorkerPosition(Position firstWorker){oldFirstWorkerPosition = firstWorker;}
    public void setOldSecondWorkerPosition(Position secondWorker){oldSecondWorkerPosition = secondWorker;}
    public void setWorker1MovesPosition(Set<Position> firstWorkerMoves){worker1MovesPosition = firstWorkerMoves;}
    public void setWorker2MovesPosition(Set<Position> secondWorkerMoves){worker2MovesPosition = secondWorkerMoves;}
    public void setWorker1BuildPosition(Set<Position> firstWorkerBuilds){worker1BuildsPosition = firstWorkerBuilds;}
    public void setWorker2BuildPosition(Set<Position> secondWorkerBuilds){worker2BuildsPosition = secondWorkerBuilds;}
    public static Text getNotification() {return notification;}

    public static void setTextFormat(Text notification){
        notification.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
    }

    public void setVisibleDomeButton(boolean visibility){
        domeButton.setVisible(visibility);
    }

    public void setVisibleDeclineButton(boolean visibility){
        declineButton.setVisible(visibility);
    }

}
