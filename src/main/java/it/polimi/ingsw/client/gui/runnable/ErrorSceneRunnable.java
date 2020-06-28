package it.polimi.ingsw.client.gui.runnable;

import it.polimi.ingsw.client.gui.MainStage;
import it.polimi.ingsw.client.gui.controller.BoardSceneController;
import it.polimi.ingsw.client.gui.controller.ErrorSceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class loads the FXML file needed for the <code>ErrorSceneController</code>
 */

public class ErrorSceneRunnable implements Runnable {

    private FXMLLoader loader;
    public ErrorSceneController errorSceneController;
    public ErrorSceneController getErrorSceneController(){return errorSceneController;}

    @Override
    public void run() {
        try {
            Parent root = loader.load();
            Scene ErrorScene = new Scene (root);

            Stage window = MainStage.getStage();

            errorSceneController = loader.getController();
            window.setScene(ErrorScene);
            window.show();

        } catch (IOException ex){
            System.err.println(ex);
        }
    }

    public ErrorSceneRunnable() {
        this.loader = new FXMLLoader(getClass().getClassLoader().getResource("ErrorScene.fxml"));
    }
}
