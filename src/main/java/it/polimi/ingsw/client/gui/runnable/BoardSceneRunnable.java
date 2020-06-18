package it.polimi.ingsw.client.gui.runnable;

import it.polimi.ingsw.client.gui.MainStage;
import it.polimi.ingsw.client.gui.controller.BoardSceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BoardSceneRunnable implements Runnable {

	private FXMLLoader loader;
	public BoardSceneController boardSceneController;
	public BoardSceneController getBoardSceneController(){return boardSceneController;}

	@Override
	public void run() {
		try {
			Parent root = loader.load();
			Scene BoardScene = new Scene (root);

			Stage window = MainStage.getStage();

			boardSceneController = loader.getController();
			window.setScene(BoardScene);
			window.show();

		} catch (IOException ex){
			System.err.println(ex);
		}
	}

	public BoardSceneRunnable() {
		this.loader = new FXMLLoader(getClass().getClassLoader().getResource("BoardScene.fxml"));
	}
}
