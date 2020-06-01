package it.polimi.ingsw.client.gui.runnable;

import it.polimi.ingsw.client.gui.MainStage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginSceneRunnable implements Runnable {

	@Override
	public void run() {
		try {
			System.out.println("Loading Login scene...");
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("LoginScene.fxml"));
			Parent root = loader.load();
			Scene LoginScene = new Scene (root);

			Stage window = MainStage.getStage();
			window.setScene(LoginScene);
			window.show();
			System.out.println("Login scene loaded");
		} catch (IOException ex){
			System.err.println(ex);
		}
	}
}
