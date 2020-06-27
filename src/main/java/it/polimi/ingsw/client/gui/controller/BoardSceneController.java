package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.gui.MainStage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import org.json.simple.parser.ParseException;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class BoardSceneController implements Initializable {

    private Textfields textfields = new Textfields();


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
    public static Text notification = new Text("");
    public boolean isMovePossible = false;
    public boolean isBuildPossible = false;
    public boolean isDomeAtAnyLevelPossible = false;
    public boolean isDeclinePossible = false;

    boolean firstTimeSelectedCell = true;
    boolean isMyTurn = false;
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
    List<Position> possiblePosition = new ArrayList<>();
    Set<Position> worker1MovesPosition;
    Set<Position> worker2MovesPosition;
    Set<Position> worker1BuildsPosition;
    Set<Position> worker2BuildsPosition;
    Position oldFirstWorkerPosition = new Position(0,0,0);
    Position oldSecondWorkerPosition = new Position (0,0,0);
    int workerSelected;
    Position newWorkerPosition;
    Position newBuildingPostion;


    public BoardSceneController() throws ParseException {
    }

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
            disableAllButtons();
            resetAllPossibleIndicators();
        });

        //handles the click on the Build Button
        buildButton.setOnAction(actionEvent -> {
            //sends the position of the new building, the worker id of the worker that made the action and if the building is a dome
            List<ViewObserver> observerList = MainStage.getObserverList();
            boolean isDome = false;
            if(newBuildingPostion.getZ()==4) isDome = true;
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedBuild(newBuildingPostion.mirrorYCoordinate(), workerSelected, isDome);
            disableAllButtons();
            resetAllPossibleIndicators();
        });

        //handles the click on the Dome Button
        domeButton.setOnAction(actionEvent -> {
            //sends the position of the new dome building and the worker id
            List<ViewObserver> observerList = MainStage.getObserverList();
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedBuild(newBuildingPostion.mirrorYCoordinate(), workerSelected, true);
            disableAllButtons();
            resetAllPossibleIndicators();
        });

        //handles the click on the Decline Button
        declineButton.setOnAction(actionEvent -> {
            //tells the client that the player has declined the possibility of new actions
            List<ViewObserver> observerList = MainStage.getObserverList();
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedDecline();
            disableAllButtons();
            resetAllPossibleIndicators();
            resetWorkerBooleans();
            clearActionsList();
            addSelectedImageToCell(previousCell,2,selectedImage);
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
     * This method handles the positioning of the first two workers on the board
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
        Platform.runLater(()-> {
            int column = GridPane.getColumnIndex((Node) event.getSource());
            int row = GridPane.getRowIndex((Node) event.getSource());

            //if we are in the askWorkerInitialPosition phase
            if (actionsCodes != null && actionsCodes.size() > 0 && actionsCodes.get(0) == 1) {
                if(isContained(possiblePosition, column, row)&&!isContained(startingWorkerPositions, column, row)) {
                    System.out.println(String.format("Node clicked at: column=%d, row=%d", column, row));
                    //adds the StackPane of the cell that we clicked in workerCells
                    StackPane cell = (StackPane) getNodeFromGridPane(gridPane, column, row);
                    //adds the Position of the cell that we clicked in workerCells
                    Position workerPosition = new Position(column, row, 0);
                    //asks to display the worker image and store it for the observer
                    displayWorker(cell, workerPosition);
                    //updates the workerNumber
                }
                else{
                    displayNotificationsDuringTurn(textfields.getNocell() +"\n");
                }
            }
            //if we are in a normal turn
            else if (actionsCodes != null && actionsCodes.size() > 0 && actionsCodes.get(0) == 2) {
                //gets the current position in terms of StackPane of the two workers
                StackPane firstWorkerCell = (StackPane) getNodeFromPosition(gridPane, oldFirstWorkerPosition);
                StackPane secondWorkerCell = (StackPane) getNodeFromPosition(gridPane, oldSecondWorkerPosition);

                //the cell that has been clicked on
                StackPane cell = (StackPane) getNodeFromGridPane(gridPane, column, row);

                //if the cell that has been clicked has either the first or second worker of the player
                if (cell == firstWorkerCell || cell == secondWorkerCell) {
                    isWorkerSelected = true;
                    if (cell == firstWorkerCell) {
                        workerSelected = 1;
                        if(firstTimeWorkerOne) {
                            displayNotificationsDuringTurn(textfields.getWorker1() + "\n");
                            firstTimeWorkerOne = false;
                            firstTimeWorkerTwo = true;
                        }
                    }

                    if (cell == secondWorkerCell) {
                        workerSelected = 2;
                        if(firstTimeWorkerTwo) {
                            displayNotificationsDuringTurn(textfields.getWorker2() + "\n");
                            firstTimeWorkerTwo = false;
                            firstTimeWorkerOne = true;
                        }
                    }
                    notifyAvailableActions();
                }

                if (isMyTurn) {
                    if (!firstTimeSelectedCell) addSelectedImageToCell(previousCell, 2, selectedImage);
                    addSelectedImageToCell(cell, 1, selectedImage);
                }

                if (!isMyTurn && !firstTimeSelectedCell) addSelectedImageToCell(previousCell, 2, selectedImage);

                //if a worker has been selected and a move action is possible
                if (isWorkerSelected && isMovePossible) {
                    List<StackPane> workerMoves;
                    Set<Position> workerMovesPosition;

                    if (workerSelected == 1) {
                        workerMoves = worker1Moves;
                        workerMovesPosition = worker1MovesPosition;

                    }else {
                        workerMoves = worker2Moves;
                        workerMovesPosition = worker2MovesPosition;
                    }
                    convertPositionListToStackPaneList(workerMovesPosition, workerSelected, 1);
                    isMoveActionPossible = isCellActionPossible(cell, workerMoves);
                    newWorkerPosition = addZToPosition(column, row, workerMovesPosition);

                    //if a movement action is possible in the cell that has been clicked by the player the moveButton is avalaible
                    moveButton.setDisable(!isMoveActionPossible);
                    clearActionsList();
                    //saves the last cell that the player clicked
                    previousCell = cell;
                    firstTimeSelectedCell = false;
                }

                //if a movement is no longer possible, the move button is disabled
                if (!isMovePossible) moveButton.setDisable(true);

                if (isWorkerSelected && isBuildPossible) {
                    List<StackPane> workerBuilds;
                    Set<Position> workerBuildsPosition;

                    if (workerSelected == 1) {
                        workerBuildsPosition = worker1BuildsPosition;
                        convertPositionListToStackPaneList(workerBuildsPosition, workerSelected, 2);
                        workerBuilds = worker1Builds;

                    }
                    else {
                        workerBuildsPosition = worker2BuildsPosition;
                        convertPositionListToStackPaneList(workerBuildsPosition, workerSelected, 2);
                        workerBuilds = worker2Builds;
                    }
                    isBuildActionPossible = isCellActionPossible(cell, workerBuilds);
                    newBuildingPostion = addZToPosition(column, row, workerBuildsPosition);

                    //if a build action is possible in the cell that has been clicked by the player the buildButton is avalaible
                    buildButton.setDisable(!isBuildActionPossible);
                    //if a build action is possible in the cell that has been clicked, and the player can choose to
                    //build a dome wherever, the domeButton is avalaible
                    domeButton.setDisable(!isBuildActionPossible && !isDomeAtAnyLevelPossible);
                    clearActionsList();
                    previousCell = cell;
                    firstTimeSelectedCell = false;
                }

                if (isDeclinePossible) declineButton.setDisable(false);
                //if a build action is no longer possible, the build button is disabled
                if (!isBuildPossible) buildButton.setDisable(true);
                //if a dome building action is no longer possible, the build button is disabled
                if (!isDomeAtAnyLevelPossible) domeButton.setDisable(true);
                //if a decline action is no longer possible, the decline button is disabled
                if (!isDeclinePossible) declineButton.setDisable(true);

            }
        });
    }

    /**TODO:javadoc
     *
     */
    private void notifyAvailableActions () {
        Set<Position> workerMovesPosition;
        Set<Position> workerBuildsPosition;

        if(workerSelected == 1) {
            workerMovesPosition = worker1MovesPosition;
            workerBuildsPosition = worker1BuildsPosition;
        }
        else {
            workerMovesPosition = worker2MovesPosition;
            workerBuildsPosition = worker2BuildsPosition;
        }
        if(workerBuildsPosition.isEmpty()&&workerMovesPosition.isEmpty())
            displayNotificationsDuringTurn(textfields.getWcantdoactions()+"\n");

        else if(!workerMovesPosition.isEmpty() && !workerBuildsPosition.isEmpty())
            displayNotificationsDuringTurn(textfields.getWcanboth()+"\n");

        else if(isMovePossible) {
            if(workerMovesPosition.isEmpty())
                displayNotificationsDuringTurn(textfields.getWcantmove()+"\n");
            else
                displayNotificationsDuringTurn(textfields.getWcanmove() +"\n");
        }
        else if(isBuildPossible) {
            if (workerBuildsPosition.isEmpty())
                displayNotificationsDuringTurn(textfields.getWcantbuild() + "\n");
            else
                displayNotificationsDuringTurn(textfields.getWcanbuild() + "\n");
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
     * Removes the position of the worker image on the board
     * @param workerPosition the old position where to delete the image
     */
    public void removeWorker(Position workerPosition) {
        StackPane cell = (StackPane)getNodeFromPosition(gridPane, workerPosition);
        removeWorkerImage(cell);
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
        if (level.equals("dome.png")){
            ImageView dome = new ImageView(building);
            dome.setFitHeight(100);
            dome.setFitWidth(100);
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
            if (notificationsTextFlow.getChildren().size() >= 7) {
                notificationsTextFlow.getChildren().clear();
            }
            Text notificationText = new Text(notification);
            setTextFormat(notificationText, 12);
            notificationsTextFlow.getChildren().add(notificationText);
    }

    public void disableDeclineButton(boolean isDeclinePossible){
        declineButton.setDisable(isDeclinePossible);
    }

    public void displayEndGameImage(boolean isVictory){
        gridPane.getChildren().clear();
        Image victoryImage = new Image("/components/VICTORY.png");
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
    public void setMyTurn(boolean myTurn){isMyTurn = myTurn;}

    public void displayGameInfos(ClientBoard board){
        Platform.runLater(()-> {
            String firstText = "";
            for (int i = 1; i <= board.getNumOfPlayers(); i++) {
                if (i == board.getMyPlayerId())
                    firstText = firstText + "- You" + textfields.getChosen1() + " " + board.getPlayer(i).getGodPowerName() + "\n";
                else
                    firstText = firstText + "- " + board.getPlayer(i).getPlayerName() + textfields.getChosen2() + board.getPlayer(i).getGodPowerName() + "\n";
            }
            displayNotificationsDuringTurn(firstText);
        });
    }
    public void setPossiblePosition(List<Position> possiblePosition) {
        /*for(int i = 0; i<possiblePosition.size(); i++) {
            this.possiblePosition.add(possiblePosition.get(i));
        }
         */
        Platform.runLater(()-> {
            displayNotificationsDuringTurn(textfields.getChooseworkerpositions() + "\n");
        });
        this.possiblePosition = possiblePosition;
    }

    public static Text getNotification() {return notification;}

    private boolean isContained (List<Position> list, int x, int y) {
        if (list==null||list.size()<1) {
        }
        else {
            for (Position position : list) {
                if (position.getX() == x && position.getY() == y)
                    return true;
            }
        }
        return false;
    }

    public static void setTextFormat(Text notification, int font){notification.setFont(Font.font("Verdana", FontWeight.BOLD, font)); }

    public void setVisibleDomeButton(boolean visibility){ domeButton.setVisible(visibility); }
    public void setVisibleDeclineButton(boolean visibility){
        declineButton.setVisible(visibility);
    }

    public void disableAllButtons () {
        moveButton.setDisable(true);
        buildButton.setDisable(true);
        domeButton.setDisable(true);
        declineButton.setDisable(true);
    }

    public void resetAllPossibleIndicators () {
        isDomeAtAnyLevelPossible = false;
        isBuildActionPossible = false;
        isMoveActionPossible = false;
        isDeclinePossible = false;
    }
    public void clearActionsList() {
        worker1Moves.clear();
        worker2Moves.clear();
        worker1Builds.clear();
        worker2Builds.clear();
    }
    public void resetWorkerBooleans () {
        firstTimeWorkerOne = true;
        firstTimeWorkerTwo = true;
        isWorkerSelected = false;
        isMovePossible = false;
        isBuildActionPossible = false;
        isBuildPossible = false;
    }
}
