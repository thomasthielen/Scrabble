package screens;

import data.DataHandler;
import gameentities.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.Client;
import network.Server;

/**
 * This class provides the controller for the EndScreen.
 *
 * @author jbleil
 * @author tikrause
 */
public class EndScreenController {

  @FXML private Pane backgroundPane;

  @FXML private Pane tooltipPane;
  @FXML private Button tooltipButton;

  @FXML private Button playAgainButton;

  /**
   * the initialize method displays the Players, their place, their points and their avatar on the
   * screen.
   *
   * @author jbleil
   */
  public void initialize() {
    ArrayList<Player> players = Client.getGameSession().getPlayerList();
    Collections.sort(players);
    
    int counter = 0;
    
    GridPane grid1 = new GridPane();
    grid1.setHgap(50);
    grid1.setVgap(20);

    double[] width = new double[4];

    for (int i = 0; i < width.length; i++) {
      width[i] = 0;
    }

    for (int i = players.size() - 1; i >= 0; i--) {
      Text place = new Text((counter + 1) + ".Place:");
      place.setFill(Paint.valueOf("#f88c00"));
      place.setFont(new Font(18));
      grid1.add(place, counter, 0);
      width[0] += place.getLayoutBounds().getWidth();
      GridPane.setHalignment(place, HPos.CENTER);

      Image avatar =
          new Image(
              getClass().getClassLoader().getResourceAsStream(players.get(i).getAvatar().getUrl()),
              52,
              52,
              true,
              true);
      ImageView imageView = new ImageView(avatar);
      grid1.add(imageView, counter, 1);
      width[1] += avatar.getWidth();

      Text name = new Text(players.get(i).getUsername());
      name.setFill(Paint.valueOf("#f88c00"));
      name.setFont(new Font(18));
      name.relocate(0, 150);
      grid1.add(name, counter, 2);
      width[2] += name.getLayoutBounds().getWidth();
      GridPane.setHalignment(name, HPos.CENTER);

      Text points = new Text("Points: " + players.get(i).getScore());
      points.setFill(Paint.valueOf("#f88c00"));
      points.setFont(new Font(18));
      grid1.add(points, counter, 3);
      width[3] += points.getLayoutBounds().getWidth();
      GridPane.setHalignment(points, HPos.CENTER);

      if (name.getLayoutBounds().getWidth() > points.getLayoutBounds().getWidth()) {
        imageView.setStyle(
            "-fx-translate-x: " + (name.getLayoutBounds().getWidth() - 52) / 2 + ";");
      } else {
        imageView.setStyle(
            "-fx-translate-x: " + (points.getLayoutBounds().getWidth() - 52) / 2 + ";");
      }
      
      counter++;
    }

    Arrays.sort(width);
    grid1.relocate(500 - ((players.size() - 1) * 50) - width[3] / 2, 150);

    backgroundPane.getChildren().add(grid1);

    Client.getGameSession().setEndScreenController(this);
    playAgainButton.setDisable(!Client.isHost());
    tooltipButton.setDisable(Client.isHost());
    tooltipPane.setVisible(false);

    initializeCloseHandler();
  }

  /**
   * Informs the user that he can't restart the game until the host has restarted the session.
   *
   * @author tikrause
   * @param event mouse event
   */
  @FXML
  void openTooltip(MouseEvent event) {
    Text text =
        new Text("Please ask the host to restart the game before you can rejoin \nthe session.");
    text.relocate(10, 10);
    text.setFill(Paint.valueOf("#f88c00"));
    text.setFont(new Font(14));
    tooltipPane.getChildren().add(text);
    tooltipPane.setVisible(true);
  }

  /**
   * Closes the tooltip pane.
   *
   * @author tikrause
   * @param event mouse event
   */
  @FXML
  void closeTooltip(MouseEvent event) {
    tooltipPane.setVisible(false);
  }

  /**
   * Implementation when the decides to leave the game.
   *
   * @author tikrause
   * @param event 'LEAVE GAME'-Button pushed
   * @throws Exception exceptions caused by disconnecting the client
   */
  @FXML
  void leaveGame(ActionEvent event) throws Exception {
    Client.disconnectClient(DataHandler.getOwnPlayer());
    if (Client.isHost()) {
      Server.shutdown();
    }
    Client.getGameSession().getGameScreenController().leave();
  }

  /**
   * Reconnects the client to a new lobby that is running on the same server and port as the session
   * before.
   *
   * @author tikrause
   * @param event button pushed
   * @throws Exception exceptions caused by disconnecting or reconnecting the client
   */
  @FXML
  void playAgain(ActionEvent event) throws Exception {
    if (Client.isHost()) {
      Client.enablePlayAgain(DataHandler.getOwnPlayer());
    }
    Client.disconnectClient(DataHandler.getOwnPlayer());
    if (Client.isHost()) {
      Server.shutdown();
      try {
        Server.createServer(Server.getPort());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    boolean multiPlayer = (Client.getGameSession().getLobbyScreenController() != null);
    Client.connectToServer(DataHandler.getOwnPlayer());
    FXMLLoader loader = new FXMLLoader();
    Parent content;
    if (multiPlayer) {
      content =
          loader.load(
              getClass()
                  .getClassLoader()
                  .getResourceAsStream("screens/resources/LobbyScreen.fxml"));

      if (Client.isHost()) {
        LobbyScreenController lobbyScreenController = loader.getController();
        lobbyScreenController.addipAndPort();
      }
    } else {
      content =
          loader.load(
              getClass()
                  .getClassLoader()
                  .getResourceAsStream("screens/resources/SinglePlayerLobbyScreen.fxml"));
    }
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * Enables the possibility to rejoin the session for all clients after the host has restarted the
   * session.
   *
   * @author tikrause
   */
  public void enablePlayAgain() {
    playAgainButton.setDisable(false);
    tooltipButton.setDisable(true);
  }

  /**
   * This method serves as a Listener for the Text "Soni Sokell" and displays an instance of
   * NameLinkScreen.
   *
   * @author jbleil
   * @param event the MouseEvent that gets thrown when clicking on the "Soni Sokell" Text
   * @throws Exception the Exception when the fxml file is not found
   */
  @FXML
  void openNameScreen(MouseEvent event) throws Exception {
    NameLinkScreen nls = new NameLinkScreen();
    Stage stage = new Stage();
    nls.start(stage);
  }

  /**
   * This method serves as a Listener for the Text "freeicons.io" and displays an instance of
   * SiteLinkScreen.
   *
   * @author jbleil
   * @param event the MouseEvent that gets thrown when clicking on the "freeicons.io" Text
   * @throws Exception the Exception when the fxml file is not found
   */
  @FXML
  void openSiteScreen(MouseEvent event) throws Exception {
    SiteLinkScreen sls = new SiteLinkScreen();
    Stage stage = new Stage();
    sls.start(stage);
  }

  /**
   * Handler for when the user closes the window.
   *
   * @author tikrause
   */
  private void initializeCloseHandler() {
    StartScreen.getStage()
        .setOnCloseRequest(
            new EventHandler<WindowEvent>() {
              @Override
              public void handle(final WindowEvent event) {
                try {
                  Client.disconnectClient(DataHandler.getOwnPlayer());
                  if (Client.isHost()) {
                    Server.shutdown();
                  }
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                Platform.exit();
                System.exit(0);
              }
            });
  }
}
