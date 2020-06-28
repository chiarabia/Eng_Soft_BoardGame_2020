package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GodCard;
import it.polimi.ingsw.client.ViewObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is the main javafx class
 * <p>
 * <p>It sets the <code>primaryStage</code> of the application, holds a data cache for the GUI for simple information
 * and disconnects the player if the player closes the application.
 */

public class MainStage extends Application {

	private final static BlockingQueue<Object> lock = new LinkedBlockingQueue<>();
	private static List<ViewObserver> observerList = new ArrayList<>();
	private static Stage stage;

	/**
	 * This <code>ArrayList&lt;Object&gt;</code> functions like a mini cache for the GUI.
	 * <p>0 for the name of the player
	 * <p>1 for the number of players
	 * <p>2 for the playerID
	 */
	public static ArrayList<Object> playerData = new ArrayList<>();
	/**
	 * stores the godPowers of the match
	 */
	public static List<GodCard> godPowers = new ArrayList<>();

	public static ArrayList<Object> getPlayerData(){return playerData;}
	public static List<ViewObserver> getObserverList(){return observerList;}
	public static List<GodCard> getGodPowers(){return godPowers;}

	public static Stage getStage() {
		return stage;
	}

	public static BlockingQueue<Object> getLock() { return lock; }

	/**
	 * Launches the javafx application
	 */
	public static void main() {
		launch();
	}

	/**
	 * Sets the <code>Stage</code>
	 * <p>Sets the properties of the <code>Stage</code> and handles when the player closes the application
	 * @param primaryStage the <code>PrimaryStage</code>
	 * @throws IOException
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		lock.add(new Object());

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

		//handles the event when the player closes the applcation
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

	}



}
