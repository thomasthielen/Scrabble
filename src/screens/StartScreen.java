package screens;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class runs the initial start of the UI and sets the stage for all thefollowing scenes
 *
 * @author jbleil
 */
public class StartScreen extends Application {

  private static Stage stage = new Stage();

  /**
   * this method sets the scene for the Start screen
   *
   * @author jbleil
   */
  public void start(Stage s) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(
        getClass()
            .getResource("resources" + System.getProperty("file.separator") + "StartScreen.fxml"));
    Parent content = loader.load();
    stage.setScene(new Scene(content));
    stage.show();
  }

  /**
   * this method returns the Stage of the Application
   *
   * @author jbleil
   * @return
   */
  public static Stage getStage() {
    return stage;
  }
}
