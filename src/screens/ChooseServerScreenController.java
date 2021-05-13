package screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * this class provides the controller for the Choose Server Screen
 *
 * @author jbleil
 */
public class ChooseServerScreenController {

  @FXML
  private TextField ipField;

  @FXML
  private TextField portField;
  
  /**
   * This method serves as the Listener for "JOIN GAME"-Button It redirects the user to the Lobby
   * Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void joinGame(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/LobbyScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "Back"-Button It redirects the user to the Start Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void back(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/StartScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }
}
