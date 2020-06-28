package it.polimi.ingsw.client.gui.controller;

import it.polimi.ingsw.client.GodCard;
import it.polimi.ingsw.client.Textfields;
import it.polimi.ingsw.client.ViewObserver;
import it.polimi.ingsw.client.gui.MainStage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.json.simple.parser.ParseException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 */
public class ChoosingGodSceneController implements Initializable {
    private Textfields textfields = new Textfields();

    @FXML
    HBox HBox1;
    @FXML
    HBox HBox2;
    @FXML
    HBox HBox3;

    @FXML
    Label topLabel;

    int numberOfPlayers;

    @FXML
    TextFlow godDescription1;
    @FXML
    TextFlow godDescription2;
    @FXML
    TextFlow godDescription3;
    @FXML
    ImageView godPortrait1;
    @FXML
    ImageView godPortrait2;
    @FXML
    ImageView godPortrait3;
    @FXML
    ImageView godPower1;
    @FXML
    ImageView godPower2;
    @FXML
    ImageView godPower3;

    public ChoosingGodSceneController() throws ParseException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HBox3.setVisible(false);
        HBox2.setVisible(false);
        ArrayList<Object> playerData = MainStage.getPlayerData();
        List<GodCard> godPowers = MainStage.getGodPowers();
        numberOfPlayers = (Integer)playerData.get(1);
        GodCard firstGod = godPowers.get(0);
        setGod(firstGod, 1);

        int numberOfGods = godPowers.size();

        if (numberOfGods > 1) {
            GodCard secondGod = godPowers.get(1);
            setGod(secondGod, 2);
            HBox2.setVisible(true);
        }
        if (numberOfGods == 3){
            GodCard thirdGod = godPowers.get(2);
            setGod(thirdGod,3);
            HBox3.setVisible(true);
        }
    }

    void setGod(GodCard God, int godNumber){
        TextFlow godDescription;
        ImageView godPortrait;
        ImageView godPower;

        //set God Name
        String name = God.getGodName();
        Text godName = setGodNameProperties(name);
        Text newLine = new Text("\n");
        //Set God Description
        String text = God.getGodDescription();

        if (godNumber == 1) {
            godDescription = godDescription1;
            godPortrait = godPortrait1;
            godPower = godPower1;
        }
        else if (godNumber ==2) {
            godDescription = godDescription2;
            godPortrait = godPortrait2;
            godPower = godPower2;
        }
        else {
            godDescription = godDescription3;
            godPortrait = godPortrait3;
            godPower = godPower3;
        }

        Text godDescrip = new Text(text);
        godDescription.getChildren().add(godName);
        godDescription.getChildren().add(newLine);
        godDescription.getChildren().add(godDescrip);

        //set Portrait
        String godCode = God.getGodImage();
        Image godPort = new Image("godPortraits/" + godCode );
        godPortrait.setImage(godPort);

        //set godPower Image
        Image godPow = new Image("godPowers/" + godCode);
        godPower.setImage(godPow);
    }

    Text setGodNameProperties(String name){
        Text godName = new Text(name);
        godName.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        return godName;
    }

    public void clickHBox(javafx.scene.input.MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        List<GodCard> godPowers = MainStage.getGodPowers();
        int numberOfGods = godPowers.size();

        if (clickedNode == HBox1 || clickedNode == godDescription1 || clickedNode == godPortrait1 || clickedNode == godPower1) {
            onBoxClicked(1);
        }
        else if(numberOfGods > 1 && (clickedNode == HBox2 || clickedNode == godDescription2 || clickedNode == godPortrait2 || clickedNode == godPower2)){
            onBoxClicked(2);
        }
        else if(numberOfGods == 3 && (clickedNode == HBox3 || clickedNode == godDescription3 || clickedNode == godPortrait3 || clickedNode == godPower3)){
            onBoxClicked(3);
        }
    }

    private void onBoxClicked(int godNumber) {
        List<GodCard> godPowers = MainStage.getGodPowers();
        List<ViewObserver> observerList = MainStage.getObserverList();
        GodCard God = godPowers.get(godNumber-1);
        String chosenGodPower = God.getGodName();
        topLabel.setText("You"+ textfields.getChosen1() + " " + chosenGodPower);
        godPowers.clear();
        godPowers.add(God);
        System.out.print("clicked on " + godNumber + " \n");
        for (ViewObserver viewObserver : observerList) viewObserver.onCompletedInitializeGodPower(chosenGodPower);
    }
}

