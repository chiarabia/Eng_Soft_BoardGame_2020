package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.GodCard;
import it.polimi.ingsw.client.ViewObserver;
import it.polimi.ingsw.client.gui.MainStage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
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
    private BorderPane board;
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
         //disables all button and set the Dome and Decline button as not visible
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

        //handles the click on the Move Button
        moveButton.setOnAction(actionEvent -> {
            //sends the position and worker id of the worker that has been moved
            List<ViewObserver> observerList = MainStage.getObserverList();
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedMove(newWorkerPosition.mirrorYCoordinate(), workerSelected);
            //moveButton.setDisable(true);
        });

        //handles the click on the Build Button
        buildButton.setOnAction(actionEvent -> {
            //sends the position of the new building, the worker id of the worker that made the action and if the building is a dome
            List<ViewObserver> observerList = MainStage.getObserverList();
            boolean isDome = false;
            if(newBuildingPostion.getZ()==4) isDome = true;
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedBuild(newBuildingPostion.mirrorYCoordinate(), workerSelected, isDome);
            //buildButton.setDisable(true);
        });

        //handles the click on the Dome Button
        domeButton.setOnAction(actionEvent -> {
            //sends the position of the new dome building and the worker id
            List<ViewObserver> observerList = MainStage.getObserverList();
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedBuild(newBuildingPostion.mirrorYCoordinate(), workerSelected, true);
            //domeButton.setDisable(true);
        });

        //handles the click on the Decline Button
        declineButton.setOnAction(actionEvent -> {
            //tells the client that the player has declined the possibility of new actions
            List<ViewObserver> observerList = MainStage.getObserverList();
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedDecline();
            moveButton.setDisable(true);
            buildButton.setDisable(true);
            domeButton.setDisable(true);
            declineButton.setDisable(true);
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
        setTextFormat(godDescrp,10);
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

    /**
     * This class handles the positioning of the first two workers on the board
     * and sends the Positions to the client.
     *
     * @param cell the pane that the player clicked
     * @param position the Position corrisponding to the StackPane
     */

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
            ArrayList<Position> startingWorkerPositionsWithYmirrored = new ArrayList<Position>();
            for (int i = 0; i < startingWorkerPositions.size(); i++) {
                startingWorkerPositionsWithYmirrored.add(i, startingWorkerPositions.get(i).mirrorYCoordinate());
            }

            for (int i = 0; i < observerList.size(); i++)
                observerList.get(i).onCompletedInitializeWorkerPositions(startingWorkerPositionsWithYmirrored);
            actionsCodes.clear();
            notificationsTextFlow.getChildren().clear();
        }
    }

    //handles the click event on a cell of the board
    public void onCellClicked(javafx.scene.input.MouseEvent event) {
        int column = GridPane.getColumnIndex((Node) event.getSource());
        int row = GridPane.getRowIndex((Node) event.getSource());
        //if we are in the askWorkerInitialPosition phase
        if(actionsCodes!=null && actionsCodes.get(0)!=null && actionsCodes.get(0) == 1) {
            System.out.println(String.format("Node clicked at: column=%d, row=%d", column, row));
            //adds the StackPane of the cell that we clicked in workerCells
            StackPane cell = (StackPane) getNodeFromGridPane(gridPane, column, row);
            //adds the Position of the cell that we clicked in workerCells
            Position workerPosition = new Position(column, row, 0);
            //asks to display the worker image and store it for the observer
            displayWorker(cell,workerPosition);
            //updates the workerNumber
        }
        //if we are in a normal turn
        else if(actionsCodes!=null && actionsCodes.get(0)!=null && actionsCodes.get(0) == 2){
            //gets the current position in terms of StackPane of the two workers
            StackPane firstWorkerCell = (StackPane)getNodeFromPosition(gridPane,oldFirstWorkerPosition);
            StackPane secondWorkerCell = (StackPane)getNodeFromPosition(gridPane,oldSecondWorkerPosition);

            //the cell that has been clicked on
            StackPane cell = (StackPane) getNodeFromGridPane(gridPane, column, row);

            //if the cell that has been clicked has either the first or second worker of the player
            if(cell == firstWorkerCell || cell == secondWorkerCell){
                isWorkerSelected = true;
                if(cell == firstWorkerCell) workerSelected=1;
                if(cell == secondWorkerCell) workerSelected=2;
            }
            if (firstTimeSelectedCell = false) addSelectedImageToCell(previousCell,2,selectedImage);
            addSelectedImageToCell(cell,1,selectedImage);

            //if a worker has been selected and a move action is possible
            if(isWorkerSelected && isMovePossible){
                if(workerSelected == 1){
                    convertPositionListToStackPaneList(worker1MovesPosition,1,1);
                    //if it's the first time that the first worker has been clicked the player is notified
                    if(firstTimeWorkerOne) displayNotificationsDuringTurn("You choose the " + workerSelected + " worker \n");
                    isMoveActionPossible = isCellActionPossible(cell,worker1Moves);
                    newWorkerPosition = addZToPosition(column,row,worker1MovesPosition);
                    firstTimeWorkerOne = false;
                    if(!firstTimeWorkerTwo) firstTimeWorkerTwo = true;
                }
                if(workerSelected == 2){
                    convertPositionListToStackPaneList(worker2MovesPosition,2,1);
                    //if it's the first time that the first worker has been clicked the player is notified
                    if(firstTimeWorkerTwo) displayNotificationsDuringTurn("You choose the " + workerSelected + " worker \n");
                    isMoveActionPossible = isCellActionPossible(cell,worker2Moves);
                    newWorkerPosition = addZToPosition(column,row,worker2MovesPosition);
                    firstTimeWorkerTwo = false;
                    if(!firstTimeWorkerOne) firstTimeWorkerOne = true;
                }
                //if a movement action is possible in the cell that has been clicked by the player the moveButton is avalaible
                moveButton.setDisable(!isMoveActionPossible);
                //saves the last cell that the player clicked
                previousCell = cell;

            }

            //if a movement is no longer possible, the move button is disabled
            if(!isMovePossible)moveButton.setDisable(true);

            if(isWorkerSelected && isBuildPossible){
               if(workerSelected == 1){
                   convertPositionListToStackPaneList(worker1BuildsPosition,1,2);
                   isBuildActionPossible = isCellActionPossible(cell,worker1Builds);
                   newBuildingPostion = addZToPosition(column,row,worker1BuildsPosition);
                   //if the worker selected can't build we display a message
                   if(worker1BuildsPosition.isEmpty()) displayNotificationsDuringTurn("The worker you choose cannot build \n");
               }
               if(workerSelected ==2){
                   convertPositionListToStackPaneList(worker2BuildsPosition,2,2);
                   isBuildActionPossible = isCellActionPossible(cell,worker2Builds);
                   newBuildingPostion = addZToPosition(column,row,worker2BuildsPosition);
                   //if the worker selected can't build we display a message
                   if(worker2BuildsPosition.isEmpty()) displayNotificationsDuringTurn("The worker you choose cannot build \n");
               }
                //if a build action is possible in the cell that has been clicked by the player the buildButton is avalaible
               buildButton.setDisable(!isBuildActionPossible);
                //if a build action is possible in the cell that has been clicked, and the player can choose to
                //build a dome wherever, the domeButton is avalaible
               domeButton.setDisable(!isBuildActionPossible && !isDomeAtAnyLevelPossible);
               previousCell = cell;
            }
            if(isDeclinePossible)declineButton.setDisable(false);
            //if a build action is no longer possible, the build button is disabled
            if(!isBuildPossible)buildButton.setDisable(true);
            //if a dome building action is no longer possible, the build button is disabled
            if(!isDomeAtAnyLevelPossible)domeButton.setDisable(true);
            //if a decline action is no longer possible, the decline button is disabled
            if(!isDeclinePossible)declineButton.setDisable(true);
        }

    }

    /**
     * Updates the position of the worker image on the board
     * @param newPosition the new position where to put the image
     * @param oldPosition the old position where to delete the image
     * @param playerID the playerID, for the color of the worker
     */
    public void updateWorker(Position newPosition, Position oldPosition, int playerID){
        StackPane newCell = (StackPane)getNodeFromPosition(gridPane, newPosition);
        StackPane oldCell = (StackPane)getNodeFromPosition(gridPane, oldPosition);
        removeWorkerImage(oldCell);
        addWorkerImage(newCell,playerID);
    }

    /**
     * Updates the image of the worker initial position
     * @param newPosition the new position where to put the image
     * @param playerID the playerID, for the color of the worker
     */
    public void updateWorkerInitialPosition (Position newPosition, int playerID){
        StackPane newCell = (StackPane)getNodeFromPosition(gridPane, newPosition);
        addWorkerImage(newCell,playerID);
    }

    /**
     * Updates the image of the buildings
     * @param newPosition position on the board where to put the image
     * @param dome if the building has to be a dome
     */
    public void updateBuilding(Position newPosition, boolean dome){
        String level = "level0.png";
        if (dome) level = "dome.png";
        StackPane newBuildingCell = (StackPane)getNodeFromPosition(gridPane,newPosition);
        int z = newPosition.getZ();
        if (!dome) level = "level" + z + ".png";
        addBuildingImage(newBuildingCell,level);
    }

    /**
     * Adds the worker ImageView to a StackPane
     * @param cell the StackPane where to add the ImageView
     * @param playerID playerID for the color of the worker
     */
    public void addWorkerImage(StackPane cell, int playerID){
        ImageView worker = new ImageView(new Image("/worker/w" + playerID +".png"));
        worker.setFitHeight(100);
        worker.setFitWidth(100);
        cell.getChildren().add(worker);
    }

    /**
     * Adds the building image to a StackPane as a BackgroundImage
     * @param cell the StackPane where to change the background
     * @param level the building level
     */
    public void addBuildingImage(StackPane cell, String level){
        Image building = new Image("/Buildings/" + level);
        if (level == "dome.png"){
            ImageView dome = new ImageView(building);
            cell.getChildren().add(dome);
        }
        else{
            BackgroundSize buildingSize = new BackgroundSize(100,100, false,false, true, true);
            BackgroundImage buildingBackground = new BackgroundImage(building, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, buildingSize );
            cell.setBackground(new Background(buildingBackground));
        }
    }

    //removes the image of a Worker in a StackPane
    public void removeWorkerImage(StackPane cell){
        cell.getChildren().clear();
    }

    /**
     * Gets the node in the GridPane from the coordinates x and y
     * @param gridPane the board
     * @param x
     * @param y
     * @return the pane that is positioned at (x,y)
     */
    public Node getNodeFromGridPane(GridPane gridPane, int x, int y) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                return node;
            }
        }
        return null;
    }

    /**
     * Gets the node in the GridPane from a Position object
     * @param gridPane the board
     * @param position
     * @return the pane
     */
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

    /**
     * Updates the action code of the game.
     * An actioncode = 1 is the WorkingStartingPosition phase
     * An actioncode = 2 is the Turn phase
     * @param code
     */

    public static void updateActionCode(int code){
        actionsCodes.clear();
        actionsCodes.add(code);
    }

    /**
     * Converts a Set<Position> in a List<Position> ands saves the data in a
     * List<StackPane>
     * @param set the set to convert
     * @param workerID if the set has information about the first or second worker
     * @param typeOfAction if the set has information about a move action (1) or build action (2)
     */

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

    /**
     * Checks if a cell is inside a List<StackPane>
     * @param cell the StackPane to check
     * @param list
     * @return true if the cell is inside the list, false otherwise
     */
    public boolean isCellActionPossible(StackPane cell, List<StackPane> list){
        boolean isStackPaneInList = false;
        if (list.contains(cell)) isStackPaneInList = true;
        return isStackPaneInList;
    }

    /**
     * Adds the Z coordinate to a Position. The position that is saved when
     * a player clicks on the board lacks the z coordinate. The method checks
     * on the avalaible moves/build list the Position object with the same x and y coordinate
     * to get z.
     * @param x
     * @param y
     * @param set the set where to check for the z coordinate
     * @return the new Position object updated
     */
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

    /**
     * adds or removes the ImageView that gives the effect of a Cell being clicked
     * @param cell the StackPane where to add the image
     * @param code if the image needs to be removed(2) or added(1)
     * @param selectedImage the ImageView to add to the StackPane
     */
    public void addSelectedImageToCell(StackPane cell, int code, ImageView selectedImage){
        if (code == 1 && !cell.getChildren().contains(selectedImage))
            cell.getChildren().add(selectedImage);
        if (code == 2 && cell.getChildren().contains(selectedImage))
            cell.getChildren().remove(selectedImage);
    }

    /**
     * Displays a notification in the TextFlow notificatinsTextFlow in the bottom right
     * @param notification the String that we want to add
     */
    public void displayNotificationsDuringTurn(String notification){
        Text notificationText = new Text(notification);
        setTextFormat(notificationText,12);
        notificationsTextFlow.getChildren().add(notificationText);
        if(notificationsTextFlow.getChildren().size() > 5) notificationsTextFlow.getChildren().clear();
    }

    public void displayNotificationsDuringTurn(String notification, int font){
        Text notificationText = new Text(notification);
        setTextFormat(notificationText,font);
        notificationsTextFlow.getChildren().add(notificationText);
        if(notificationsTextFlow.getChildren().size() > 5) notificationsTextFlow.getChildren().clear();
    }

    public void disableDeclineButton(boolean isDeclinePossible){
        declineButton.setDisable(isDeclinePossible);
    }

    public void displayEndGameImage(boolean isVictory){
        gridPane.getChildren().clear();
        Image victoryImage = new Image("/components/VICTORY.PNG");
        Image loseImage = new Image("/components/LOSE.png");
        if (isVictory) addImageToBoard(victoryImage);
        else addImageToBoard(loseImage);
        moveButton.setVisible(false);
        buildButton.setVisible(false);
        domeButton.setVisible(false);
        declineButton.setVisible(false);
    }

    public void addImageToBoard(Image image){
        StackPane imageContainer = new StackPane();
        imageContainer.setMinHeight(500);
        imageContainer.setMinWidth(500);
        BackgroundSize buildingSize = new BackgroundSize(500,500, false,false, true, true);
        imageContainer.setBackground(new Background(new BackgroundImage(new Image("/Board/SantoriniBoardOnly.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, buildingSize )));
        board.setCenter(imageContainer);
        ImageView endGame = new ImageView(image);
        endGame.setFitWidth(500);
        endGame.setFitHeight(500);
        imageContainer.getChildren().add(endGame);
    }

    public void setMovePossible (boolean isPossible) {isMovePossible = isPossible;}
    public void setBuildPossible (boolean isPossible) {isBuildPossible = isPossible;}
    public void setDomeAtAnyLevelPossible (boolean isPossible) {isDomeAtAnyLevelPossible = isPossible;}
    public void setDeclinePossible (boolean isPossible) {isDeclinePossible = isPossible;}
    public void setWorkerSelected(boolean isWorker){isWorkerSelected = isWorker;}
    public void setOldFirstWorkerPosition(Position firstWorker){oldFirstWorkerPosition = firstWorker;}
    public void setOldSecondWorkerPosition(Position secondWorker){oldSecondWorkerPosition = secondWorker;}
    public void setWorker1MovesPosition(Set<Position> firstWorkerMoves){worker1MovesPosition = firstWorkerMoves;}
    public void setWorker2MovesPosition(Set<Position> secondWorkerMoves){worker2MovesPosition = secondWorkerMoves;}
    public void setWorker1BuildPosition(Set<Position> firstWorkerBuilds){worker1BuildsPosition = firstWorkerBuilds;}
    public void setWorker2BuildPosition(Set<Position> secondWorkerBuilds){worker2BuildsPosition = secondWorkerBuilds;}
    public static Text getNotification() {return notification;}


    public static void setTextFormat(Text notification, int font){notification.setFont(Font.font("Verdana", FontWeight.BOLD, font)); }

    public void setVisibleDomeButton(boolean visibility){ domeButton.setVisible(visibility); }
    public void setVisibleDeclineButton(boolean visibility){
        declineButton.setVisible(visibility);
    }

}
