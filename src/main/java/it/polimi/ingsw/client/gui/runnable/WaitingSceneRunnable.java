package it.polimi.ingsw.client.gui.runnable;


import it.polimi.ingsw.client.gui.MainStage;
import it.polimi.ingsw.client.gui.controller.WaitingSceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WaitingSceneRunnable implements Runnable{

	public WaitingSceneController waitingSceneController;
	public WaitingSceneController getWaitingSceneController(){return waitingSceneController;}

	@Override
	public void run() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("WaitingScene.fxml"));
			Parent root = loader.load();
			Scene waitingScene = new Scene (root);

			waitingSceneController = loader.getController();
			Stage window = MainStage.getStage();

			window.setScene(waitingScene);
			window.show();

		} catch (IOException ex){
			System.err.println(ex);
		}
	}

}
