package it.polimi.ingsw.client.gui.runnable;

import it.polimi.ingsw.client.gui.MainStage;
import it.polimi.ingsw.client.gui.controller.LoginSceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginSceneRunnable implements Runnable {

	private FXMLLoader loader;
	public LoginSceneController loginSceneController;
	public LoginSceneController getLoginSceneController(){return loginSceneController;}

	@Override
	public void run() {
		try {
			System.out.println("Loading Login scene...");
			Parent root = loader.load();
			Scene LoginScene = new Scene (root);

			Stage window = MainStage.getStage();

			loginSceneController = loader.getController();
			window.setScene(LoginScene);
			window.show();
			System.out.println("Login scene loaded");
		} catch (IOException ex){
			System.err.println(ex);
		}
	}

	public LoginSceneRunnable(){
		this.loader = new FXMLLoader(getClass().getClassLoader().getResource("LoginScene.fxml"));
	}
}
