package it.polimi.ingsw.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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

		/*FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getResource("../../../../../resources/LoginScene.fxml"));
		Parent firstPane = firstPaneLoader.load();
		Scene loginScene = new Scene(firstPane, 300, 275);
		primaryStage.minWidthProperty().bind(root.heightProperty().multiply(2));
		primaryStage.minHeightProperty().bind(root.widthProperty().divide(2.5));
		LoginScene login = new LoginScene();*/

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

		/*LoginSceneController firstPaneController = (LoginSceneController) firstPaneLoader.getController();
		firstPaneController.setBoardScene(board);*/
	}

	/**
	 * This method changes the Scene of the main Stage
	 *
	 * @param fxmlFile the FXML file of the scene to show
	 * @param playersName the player's name
	 */
/*	public void switchScene(String fxmlFile, String playersName)
	{

		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));
		Parent root;
		try
		{
			root = loader.load();
			if(fxmlFile.equals("BoardScene.fxml"))
			{
				BoardSceneController boardSceneController = loader.getController();
				boardSceneController.transferMessage(playersName);
			}
			this.stage.setScene(new Scene(root));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}*/

}
