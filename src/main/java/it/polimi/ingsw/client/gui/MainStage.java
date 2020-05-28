package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GodCard;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainStage extends Application {

	int numberOfPlayers;
	String playerName;
	List<GodCard> godPowers;


	public MainStage() throws IOException {
	}

	public static void main(String[] args) {
		launch(args);
	}

	GUICache cache = new GUICache();

	@Override
	public void start(Stage primaryStage) throws IOException {
		//sets the primary stage
		primaryStage = cache.getPrimaryStage();
		cache.setStage(primaryStage);

		//sets the first Scene as the Loading Scene
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("LoginScene.fxml"));

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void setPlayerNameAndNumberOfPlayer(int number, String name){
		numberOfPlayers = number;
		playerName = name;
	}


	public int getNumberOfPlayers(){return numberOfPlayers;}
	public String getPlayerName(){return playerName;}
	public List<GodCard> getGodPowers(){return godPowers;}

	public void setGodPowers(List<GodCard> godsNames){godPowers = godsNames;}

}
