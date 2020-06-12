package it.polimi.ingsw.client.gui.runnable;


import it.polimi.ingsw.client.gui.MainStage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoseSceneRunnable implements Runnable{

    @Override
    public void run() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("LoseScene.fxml"));
            Parent root = loader.load();
            Scene waitingScene = new Scene (root);

            Stage window = MainStage.getStage();

            window.setScene(waitingScene);
            window.show();

        } catch (IOException ex){
            System.err.println(ex);
        }
    }

}
