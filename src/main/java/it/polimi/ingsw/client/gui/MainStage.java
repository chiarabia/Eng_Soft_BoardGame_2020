package it.polimi.ingsw.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainStage extends Application {

	public static void main(String[] args) {
		launch(args);
	}


	private Stage stage;

	@Override
	public void start(Stage primaryStage) throws Exception{
		//sets the primary stage
		this.stage = primaryStage;
		primaryStage.setTitle("Santorini");
		primaryStage.setMinHeight(774);
		primaryStage.setMinWidth(1386);
		//sets the first Scene as the Loading Scene
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("LoginScene.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}


}
