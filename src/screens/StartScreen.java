package screens;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class runs the initial start of the UI and sets the stage for all the following scenes.
 *
 * @author jbleil
 */
public class StartScreen extends Application {

  private static Stage stage = new Stage();

  /**
   * this method sets the scene for the Start screen.
   *
   * @author jbleil
   */
  public void start(Stage s) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass().getClassLoader().getResourceAsStream("screens/resources/StartScreen.fxml"));
    stage.setScene(new Scene(content));
    stage.setTitle("THE BEST SCRABBLE");
    stage.setResizable(false);
    stage.show();
  }

  /**
   * this method returns the Stage of the Application.
   *
   * @author jbleil
   * @return returns the stage
   */
  public static Stage getStage() {
    return stage;
  }
}
