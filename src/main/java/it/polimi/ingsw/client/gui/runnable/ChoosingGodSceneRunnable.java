package it.polimi.ingsw.client.gui.runnable;

import it.polimi.ingsw.client.gui.GUICache;
import it.polimi.ingsw.client.gui.MainStage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EventObject;

public class ChoosingGodSceneRunnable implements Runnable {

    GUICache cache = new GUICache();

    @Override
    public void run() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ChoosingGodScene.fxml"));
            Parent root = loader.load();
            Scene boardScene = new Scene (root);

            Stage window = MainStage.getStage();

            window.setScene(boardScene);
            window.show();

        } catch (IOException ex){
            System.err.println(ex);
            ex.printStackTrace();
        }
    }
}
