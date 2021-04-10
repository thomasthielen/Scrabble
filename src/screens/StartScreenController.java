package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartScreenController {
	
    @FXML
    void onlineGame(ActionEvent event) throws Exception {
    	Stage stage = new Stage();
    	stage.setScene(new Scene(FXMLLoader.load(new File("OnlineScreen.fxml").toURI().toURL())));
    	stage.show();
    }

    @FXML
    void offlineGame(ActionEvent event) throws Exception {
    	Stage stage = new Stage();
    	stage.setScene(new Scene(FXMLLoader.load(new File("OfflineScreen.fxml").toURI().toURL())));
    	stage.show();
    }
}
