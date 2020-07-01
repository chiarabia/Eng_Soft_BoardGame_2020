package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.Position;
import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.gui.MainStage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
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

/**
 * This class handles the controls for the scene with the Board and all the game phases.
 *
 * <p><p>The BoardSceneController is loaded from the <code>BoardScene.fxml</code> file. In the CENTER of the Scene there is the actual Board.
 * All the game phases are handled from this class and can be divided into three different phases. When the Scene loads, the player will either
 * be waiting for the other players to go through their first phase or be themselves in the first phase.
 * <p><ul>
 * <li><i>First phase</i>: in this phase the player is choosing its first workers positions and has a <code>actionCode = 1</code> value. The method that handles the click
 * on a cell of the <code>GridPane</code>, {@link #onCellClicked(MouseEvent)}, automatically adds a Worker to the board and sends to the Client all needed information. When this phase ends the player has to either wait
 * for the other players to go through their first phase or second phase.
 * <li><i>Second phase</i>: in this phase the game has actually started and each player plays its turn. It has a <code>actionCode = 2</code> value. When its the player's turn
 * the method that handles the click on a cell, {@link #onCellClicked(MouseEvent)}, shows the player, with a visual clue, which cell is selected and all possible actions that they can make. Buttons are disabled,
 * or not, depending on the possible actions.
 * <li><i>Third phase</i>: a player has won and the game has ended. The notification of either winning or losing is shown through an <code>Image</code>, the <code>GridPane</code>
 * with the board is cleared and all buttons are not visible.
 * </ul><p>
 *     
 * All phases and possible actions are displayed as notifications in the bottom right of the screen. 
 */
public class BoardSceneController implements Initializable {

    private Textfields textfields = new Textfields();
    
    @FXML
    private BorderPane board;
    /**
     * The 5x5 <code>GridPane</code> positioned in the CENTER of the <code>BorderPane board</code>.
     * <p>All <code>GridPane</code> nodes are <code>StackPane</code>
     */
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
    /**
     * The <code>TextFlow</code> in the bottom right that displays all the current notifications for the Player
     */
    @FXML
    public TextFlow notificationsTextFlow;

    /**
     * A <code>List</code> for possible game Phases.
     * <p>When actionCode = 1 the game is in the initial phase where the player is choosing the position of its workers
     * <p>When actionCode = 2 its the player's turn
     * <p>When actionCode = 0 the game just started
     */
    public static List<Integer> actionsCodes = new ArrayList<>();
    /**
     * A <code>Text</code> notification for the Player
     */
    public static Text notification = new Text("");
    /**
     * If a move action is possible
     */
    public boolean isMovePossible = false;
    /**
     * If a build action is possible
     */
    public boolean isBuildPossible = false;
    /**
     * If the player has the ability to build the Dome type of building at any z coordinate
     */
    public boolean isDomeAtAnyLevelPossible = false;
    /**
     * If the player has finished all its possible actions or the remaining ones are optional
     */
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
    Position newBuildingPosition;

    private static final int DOME_LEVEL = 4;
    private static final int DELETE = 2;
    private static final int ADD = 1;
    private static final int CELL = 100;
    private static final int BOARD = 500;
    private static final int MOVE = 1;
    private static final int BUILD = 2;
    private static final int MAX_NOTIFICATIONS = 7;
    private static final int TURN_FONT_NOTIFICATIONS = 12;
    private static final int DESCRIPTION_FONT = 10;


    public BoardSceneController() throws ParseException {
    }

    /**
     * Sets the BoardScene and handles the buttons' events.
     * <p>The Board Scene starts with all buttons disabled and the domeButton and declineButton as not visible.
     * ActionCode is set to 0. The player godPower is retrieved from the MainStage.
     * <p>
     * <p><b>MoveButton Event</b>
     * <p>When the <code>moveButton</code> is clicked, the information of the new <code>Position</code> and the ID of the worker that was moved is sent to the Client.
     * Then all buttons and indicators are disabled and reset.
     * <p>
     * <p><b>BuildButton Event</b>
     * <p>When the <code>buildButton</code> is clicked, the information of the <code>Position</code> of the new building, the ID of the worker and if the building is a Dome is sent to the Client.
     * Then all buttons and indicators are disabled and reset.
     * <p>
     * <p><b>domeButton Event</b>
     * <p>When the <code>domeButton</code> is clicked, the information of the <code>Position</code> of the new building, the ID of the worker and the fact that the building is a Dome is sent to the Client.
     * Then all buttons and indicators are disabled and reset.
     * <p>
     * <p><b>declineButton Event</b>
     * <p>When the <code>declineButton</code> is clicked, the information that the turn ended is sent to the Client.
     * Then all buttons and indicators are disabled and reset, together with the workers' information.
     * @param url
     * @param resourceBundle
     * @see #resetAllPossibleIndicators()
     */
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
            if(newBuildingPosition.getZ()==DOME_LEVEL) isDome = true;
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedBuild(newBuildingPosition.mirrorYCoordinate(), workerSelected, isDome);
            disableAllButtons();
            resetAllPossibleIndicators();
        });

        //handles the click on the Dome Button
        domeButton.setOnAction(actionEvent -> {
            //sends the position of the new dome building and the worker id
            List<ViewObserver> observerList = MainStage.getObserverList();
            for (int i = 0; i < observerList.size(); i++) observerList.get(i).onCompletedBuild(newBuildingPosition.mirrorYCoordinate(), workerSelected, true);
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
            addSelectedImageToCell(previousCell,DELETE,selectedImage);
        });
    }


    /**
     * Sets the <code>Image</code> and god description in a <code>TextField</code> on the screen
     * @param card the <code>GodCard</code> with the name of the god, the description and the image
     */
    public void setGodDetails(GodCard card){
        MainStage.getLock().add(new Object());
        //set God Description
        String text = card.getGodDescription();
        Text godDescrp = new Text(text);
        setTextFormat(godDescrp,DESCRIPTION_FONT);
        godDescriptionTextFlow.getChildren().add(godDescrp);
        //set God Card
        String godCode = card.getGodImage();
        Image godCardImage = new Image("godCards/" + godCode);
        godCardImageView.setImage(godCardImage);
    }

    /**
     * Displays the name of the Player in a <code>Label</code> in the top-left
     */
    public void displayPlayerName(){
        ArrayList<Object> playerData = MainStage.getPlayerData();
        String playerName = (String)playerData.get(0);
        playerNameLabel.setText(playerName);
    }

    /**
     * Handles the positioning of the first two workers on the board.
     * It sends the <code>Position</code> to the client.
     *
     * @param cell the <code>StackPane</code> that the player clicked
     * @param position the <code>Position</code> corresponding to the <code>StackPane</code>
     * @see #addWorkerImage(StackPane, int)
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

    /**
     * Handles the click event on a cell of the board.
     *
     * <p>The method behaves differently based on the <code>actionCode</code>.
     * <p>
     * <p><b>When ActionCode = 1</b>
     * <p>The game is in the initial phase where the player needs to choose its workers first <code>Position</code>. When the player clicks
     * on the board they position a worker immediately on the board. A check for possible already positioned workers is made.
     * <p>
     * <p><b>When ActionCode = 2</b>
     * <p>It's the player's turn. To help the player better visualize what cell of the board they have clicked on, an <code>Image</code> will appear on the
     * selected <code>StackPane</code>. Firstly the player needs to select the worker they want to make an action with. The worker selected is saved and a notification
     * of which worker has been selected is shown. When a worker is selected the Player will be notified of the types of action they can make. When the player clicks on
     * a Cell with a possible action the Button of the respective action will be visible and the player will be able to click it.
     * @param event the <code>MouseEvent</code>
     * @see #displayWorker(StackPane, Position)
     * @see #addSelectedImageToCell(StackPane, int, ImageView) 
     */
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
                    //if the cell is not available it prints a message
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

                //handles the image that gives the player the visual clue of which Cell they clicked on
                //if it's not the player turn the image will not appear
                if (isMyTurn) {
                    if (!firstTimeSelectedCell) addSelectedImageToCell(previousCell, DELETE, selectedImage);
                    addSelectedImageToCell(cell, ADD, selectedImage);
                }
                if (!isMyTurn && !firstTimeSelectedCell) addSelectedImageToCell(previousCell, ADD, selectedImage);

                //if a worker has been selected and a move action is possible
                if (isWorkerSelected && isMovePossible) {
                    List<StackPane> workerMoves;
                    Set<Position> workerMovesPosition;

                    //sets the information for the right worker
                    if (workerSelected == 1) {
                        workerMoves = worker1Moves;
                        workerMovesPosition = worker1MovesPosition;

                    }else {
                        workerMoves = worker2Moves;
                        workerMovesPosition = worker2MovesPosition;
                    }
                    //converts the information into StackPane
                    convertPositionListToStackPaneList(workerMovesPosition, workerSelected, 1);
                    //checks if the action is possible for the clicked cell
                    isMoveActionPossible = isCellActionPossible(cell, workerMoves);
                    newWorkerPosition = addZToPosition(column, row, workerMovesPosition);

                    //if a movement action is possible in the cell that has been clicked by the player the moveButton is available
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

                    //sets the information for the right worker
                    if (workerSelected == 1) {
                        workerBuildsPosition = worker1BuildsPosition;
                        //converts the information into StackPane
                        convertPositionListToStackPaneList(workerBuildsPosition, workerSelected, 2);
                        workerBuilds = worker1Builds;

                    }
                    else {
                        workerBuildsPosition = worker2BuildsPosition;
                        //converts the information into StackPane
                        convertPositionListToStackPaneList(workerBuildsPosition, workerSelected, 2);
                        workerBuilds = worker2Builds;
                    }
                    isBuildActionPossible = isCellActionPossible(cell, workerBuilds);
                    newBuildingPosition = addZToPosition(column, row, workerBuildsPosition);

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
     * Updates the <code>Position</code> of the worker image on the board.
     * It removes the <code>Image</code> of the worker from its old <code>Position</code> and adds the new one in the new <code>Position</code>.
     * The color of the worker is decided based on the playerID
     * @param newPosition the new Position where to put the image
     * @param oldPosition the old Position where to delete the image
     * @param playerID the player number that we need for the color of the worker
     * @see #addWorkerImage(StackPane, int)
     */
    public void updateWorker(Position newPosition, Position oldPosition, int playerID){
        StackPane newCell = (StackPane)getNodeFromPosition(gridPane, newPosition);
        StackPane oldCell = (StackPane)getNodeFromPosition(gridPane, oldPosition);
        removeImage(oldCell);
        addWorkerImage(newCell,playerID);
    }

    /**
     * Removes the worker <code>Image</code> from the Board.
     * @param workerPosition the old <code>Position</code> where to delete the <code>Image</code>.
     */
    public void removeWorker(Position workerPosition) {
        StackPane cell = (StackPane)getNodeFromPosition(gridPane, workerPosition);
        removeImage(cell);
    }

    /**
     * Adds the <code>Image</code> of the worker at the start of the game.
     * This method is exclusively used in the ActionCode=1 phase, when the Player has chosen
     * its workers initial <code>Position</code>.
     * @param newPosition the new <code>Position</code> where to put the image
     * @param playerID the player number that we need for the color of the worker
     * @see #addWorkerImage(StackPane, int)
     */
    public void updateWorkerInitialPosition (Position newPosition, int playerID){
        StackPane newCell = (StackPane)getNodeFromPosition(gridPane, newPosition);
        addWorkerImage(newCell,playerID);
    }

    /**
     * Updates the <code>Image</code> of the buildings.
     * @param newPosition <code>Position</code> on the board where to put the image
     * @param dome true if the building that needs to be added is a dome, false otherwise.
     * @see #addBuildingImage(StackPane, String) 
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
     * Adds the worker <code>Image</code> to a <code>StackPane</code>.
     * <p>The color of the worker is <i>blue</i> for playerID = 1, <i>yellow</i> for playerID = 2, <i>green</i> for PlayerID = 3.
     * @param cell the <code>StackPane</code> where to add the <code>Image</code>.
     * @param playerID the player number that we need for the color of the worker
     */
    public void addWorkerImage(StackPane cell, int playerID){
        ImageView worker = new ImageView(new Image("/worker/w" + playerID +".png"));
        worker.setFitHeight(CELL);
        worker.setFitWidth(CELL);
        cell.getChildren().add(worker);
    }

    /**
     * Adds the building image in a <code>StackPane</code>.
     * <p>The <code>String</code> level is used to find the right image for the building.
     * If the building is a dome the image is a <code>ImageView</code>, otherwise it's the <code>Background</code> of the <code>StackPane</code>.
     * @param cell the <code>StackPane</code> where to add the building
     * @param level the building level
     */
    public void addBuildingImage(StackPane cell, String level){
        Image building = new Image("/Buildings/" + level);
        if (level.equals("dome.png")){
            ImageView dome = new ImageView(building);
            dome.setFitHeight(CELL);
            dome.setFitWidth(CELL);
            cell.getChildren().add(dome);
        }
        else{
            BackgroundSize buildingSize = new BackgroundSize(CELL,CELL, false,false, true, true);
            BackgroundImage buildingBackground = new BackgroundImage(building, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, buildingSize );
            cell.setBackground(new Background(buildingBackground));
        }
    }

    /**
     * Clears the <code>StackPane</code> of its children
     * @param cell the <code>StackPane</code> we want to clear
     */
    public void removeImage(StackPane cell){
        cell.getChildren().clear();
    }

    /**
     * Gets the <code>Node</code> in a <code>GridPane</code> from (x,y) coordinates
     * @param gridPane the <code>GridPane</code> used for the Board
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the <code>Node</code> that is positioned at (x,y)
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
     * Gets the <code>Node</code> in a <code>GridPane</code> from a <code>Position</code>
     * @param gridPane the <code>GridPane</code> used for the Board
     * @param position the position (x,y) of the pane we want
     * @return the <code>Node</code> at the position
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
     * Updates the <code>List&lt;Integer&gt; ActionCode</code>
     * @param code int number of the code
     * @see #actionsCodes
     */

    public static void updateActionCode(int code){
        actionsCodes.clear();
        actionsCodes.add(code);
    }

    /**
     * Converts a <code>Set&lt;Position&gt;</code> in a <code>List&lt;Position&gt;</code> and saves the data in a
     * <code>List&lt;StackPane&gt;</code>.
     * @param set the <code>Set</code> to convert
     * @param workerID if the <code>Set</code> has information about the first or second worker
     * @param typeOfAction if the <code>Set</code> has information about a move action (MOVE) or build action (BUILD)
     */

    public void convertPositionListToStackPaneList(Set<Position> set, int workerID, int typeOfAction ){
        List<Position> list = set.stream().collect(Collectors.toCollection(ArrayList::new));
        for(int i=0; i<list.size();i++){
            StackPane convertedStackPane = (StackPane) getNodeFromPosition(gridPane,list.get(i));
            if(workerID == 1 && typeOfAction == MOVE)
                worker1Moves.add(convertedStackPane);
            if(workerID == 2 && typeOfAction == MOVE)
                worker2Moves.add(convertedStackPane);
            if(workerID == 1 && typeOfAction == BUILD)
                worker1Builds.add(convertedStackPane);
            if(workerID == 2 && typeOfAction == BUILD)
                worker2Builds.add(convertedStackPane);
        }
    }

    /**
     * Checks if a <code>StackPane</code> is inside a <code>List&lt;StackPane&gt;</code>
     * @param cell the <code>StackPane</code> to check
     * @param list the <code>List</code> we want to check
     * @return true if the cell is inside the list, false otherwise
     */
    public boolean isCellActionPossible(StackPane cell, List<StackPane> list){
        boolean isStackPaneInList = false;
        if (list.contains(cell)) isStackPaneInList = true;
        return isStackPaneInList;
    }

    /**
     * Adds the Z coordinate to a <code>Position</code>.
     * <p>The position that is saved when a player clicks on the board lacks the z coordinate. The method checks,
     * from the available moves/build <code>List</code>, the <code>Position</code> with the same x and y coordinate
     * to get z coordinate.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param set the <code>Set</code> where to check for the z coordinate
     * @return the <code>Position</code> object with all the coordinates
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
     * Adds or removes the <code>Image</code> that gives the effect of a Cell of the board being clicked.
     * @param cell the <code>StackPane</code> where to add the <code>Image</code>
     * @param code if the <code>Image</code> needs to be removed(DELETE) or added(ADD)
     * @param selectedImage the <code>ImageView</code> to add to the StackPane
     */
    public void addSelectedImageToCell(StackPane cell, int code, ImageView selectedImage){
        if (code == ADD && !cell.getChildren().contains(selectedImage))
            cell.getChildren().add(selectedImage);
        if (code == DELETE && cell.getChildren().contains(selectedImage))
            cell.getChildren().remove(selectedImage);
    }

    /**
     * Displays a notification in the <code>TextFlow</code> in the bottom right
     * @param notification the <code>String</code> that will be added
     */
    public void displayNotificationsDuringTurn(String notification){
            if (notificationsTextFlow.getChildren().size() >= MAX_NOTIFICATIONS) {
                notificationsTextFlow.getChildren().clear();
            }
            Text notificationText = new Text(notification);
            setTextFormat(notificationText, TURN_FONT_NOTIFICATIONS);
            notificationsTextFlow.getChildren().add(notificationText);
    }


    /**
     * Displays the EndGame phase of the game.
     * A Victory/Lose <code>Image</code> will be on the board and the <code>Buttons</code> won't be visible.
     * @param isVictory if true displays the Victory <code>Image</code> otherwise the Lose <code>Image</code>
     * @see #addImageToBoard(Image)
     */
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

    /**
     * This method adds an <code>Image</code> in the center of the Board
     * @param image the <code>Image</code> we want to add
     */
    public void addImageToBoard(Image image){
        StackPane imageContainer = new StackPane();
        imageContainer.setMinHeight(BOARD);
        imageContainer.setMinWidth(BOARD);
        BackgroundSize buildingSize = new BackgroundSize(BOARD,BOARD, false,false, true, true);
        imageContainer.setBackground(new Background(new BackgroundImage(new Image("/Board/SantoriniBoardOnly.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, buildingSize )));
        board.setCenter(imageContainer);
        ImageView endGame = new ImageView(image);
        endGame.setFitWidth(BOARD);
        endGame.setFitHeight(BOARD);
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

    //TODO add javadoc
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
    //TODO add javadoc
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
    public void disableDeclineButton(boolean isDeclinePossible){
        declineButton.setDisable(isDeclinePossible);
    }

    /**
     * Disables all <code>Buttons</code>
     */

    public void disableAllButtons () {
        moveButton.setDisable(true);
        buildButton.setDisable(true);
        domeButton.setDisable(true);
        declineButton.setDisable(true);
    }

    /**
     * Resets all the indicators needed to able the buttons.
     */
    public void resetAllPossibleIndicators () {
        isDomeAtAnyLevelPossible = false;
        isBuildActionPossible = false;
        isMoveActionPossible = false;
        isDeclinePossible = false;
    }

    /**
     * Clears the <code>List</code> with the <code>StackPane</code> where is possible to build and move
     */
    public void clearActionsList() {
        worker1Moves.clear();
        worker2Moves.clear();
        worker1Builds.clear();
        worker2Builds.clear();
    }

    /**
     * Resets all indicators for the Workers
     */
    public void resetWorkerBooleans () {
        firstTimeWorkerOne = true;
        firstTimeWorkerTwo = true;
        isWorkerSelected = false;
        isMovePossible = false;
        isBuildActionPossible = false;
        isBuildPossible = false;
    }
}
