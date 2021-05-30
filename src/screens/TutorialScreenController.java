package screens;

import data.DataHandler;
import java.io.IOException;
import java.net.BindException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import network.Client;
import network.Server;

/**
 * This class provides the controller for the Tutorial Screen.
 *
 * @author lsteltma
 * @author jluellig
 * @author jbleil
 */
public class TutorialScreenController {

  private ArrayList<Image> images = new ArrayList<Image>();

  private static int activeImage = 0;

  private Text imageCounter;

  private ImageView iv = new ImageView();

  @FXML private Button previousButton;

  @FXML private Button nextButton;

  @FXML private Pane backgroundPane;

  @FXML private Button startGameButton;

  @FXML private Rectangle startTutorialGameRectangle;

  @FXML private Pane tooltipPane;

  /**
   * This method initializes all the functionalities on the Screen.
   *
   * @author lsteltma
   */
  public void initialize() {
    images.add(
        new Image(getClass().getClassLoader().getResourceAsStream("screens/resources/Rack1.jpg")));
    images.add(
        new Image(getClass().getClassLoader().getResourceAsStream("screens/resources/Rack2.jpg")));
    images.add(
        new Image(getClass().getClassLoader().getResourceAsStream("screens/resources/Rack3.jpg")));
    images.add(
        new Image(
            getClass().getClassLoader().getResourceAsStream("screens/resources/GameBoard1.jpg")));
    images.add(
        new Image(
            getClass().getClassLoader().getResourceAsStream("screens/resources/GameBoard2.jpg")));
    images.add(
        new Image(
            getClass().getClassLoader().getResourceAsStream("screens/resources/GameBoard3.jpg")));
    images.add(
        new Image(
            getClass().getClassLoader().getResourceAsStream("screens/resources/GameBoard4.jpg")));
    images.add(
        new Image(
            getClass().getClassLoader().getResourceAsStream("screens/resources/GameBoard5.jpg")));
    images.add(
        new Image(
            getClass().getClassLoader().getResourceAsStream("screens/resources/GameBoard6.jpg")));

    initializeSlideCounter();
    initializeImage();

    updateImage();

    initializeArrowKeys();

    startGameButton.setDisable(!(activeImage >= 8));

    tooltipPane.toFront();

    tooltipPane.setVisible(false);
  }

  /**
   * This method initializes the slide counter so that the user can see on which slide of the
   * tutorial he currently is.
   *
   * @author lsteltma
   */
  private void initializeSlideCounter() {
    imageCounter = new Text((activeImage + 1) + "/9");
    imageCounter.setFont(new Font(25));
    imageCounter.setFill(Color.WHITE);
    imageCounter.setX(600);
    imageCounter.setY(56);
    backgroundPane.getChildren().add(imageCounter);
  }

  /**
   * This method initializes the first active image shown on the screen.
   *
   * @author lsteltma
   */
  private void initializeImage() {
    iv = new ImageView(images.get(activeImage));
    iv.setX(60);
    iv.setY(70);
    iv.setPreserveRatio(true);
    iv.setFitWidth(880);
    backgroundPane.getChildren().add(iv);
  }

  /**
   * Initializes the function for the stage to switch to the next or previous image by pressing the
   * arrow keys.
   *
   * @author jluellig
   */
  private void initializeArrowKeys() {
    for (Node n : backgroundPane.getChildren()) {
      n.setOnKeyPressed(
          new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
              if (ke.getCode().equals(KeyCode.LEFT)) {
                try {
                  previous(new ActionEvent());
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
              if (ke.getCode().equals(KeyCode.RIGHT)) {
                try {
                  next(new ActionEvent());
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }
          });
    }
  }

  /**
   * This method is the listener for the previous button.
   *
   * @author jbleil
   * @author lsteltma
   * @param event the ActionEvent when the previous-Button is klicked
   */
  @FXML
  void previous(ActionEvent event) {
    activeImage -= activeImage > 0 ? 1 : 0;
    updateImage();
  }

  /**
   * This method is the listener for the next button.
   *
   * @author jbleil
   * @author lsteltma
   * @param event the ActionEvent when the next-Button is klicked
   */
  @FXML
  void next(ActionEvent event) {
    activeImage += activeImage < 8 ? 1 : 0;
    updateImage();
  }

  /**
   * This method is the listener for the leave button.
   *
   * @author jbleil
   * @author lsteltma
   * @param event the ActionEvent when the leave-Button is klicked
   * @throws Exception the Exception when the FXML file is not found
   */
  @FXML
  void leaveTutorial(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content;
    try {
      content =
          loader.load(
              getClass()
                  .getClassLoader()
                  .getResourceAsStream("screens/resources/OnlineOrOfflineScreen.fxml"));
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called every time the previous or next button gets pressed to update the shown
   * image. Also sets the startgame button as enabled on the last slide.
   *
   * @author lsteltma
   * @author tthielen
   */
  private void updateImage() {
    for (Node node : backgroundPane.getChildren()) {
      if (node instanceof Text && ((Text) node).getText().contains("/9")) {
        ((Text) node).setText((activeImage + 1) + "/9");
      }
      if (node instanceof Button && ((Button) node).getText().equals("<")) {
        if (activeImage <= 0) {
          ((Button) node).setDisable(true);
        } else {
          ((Button) node).setDisable(false);
        }
      }
      if (node instanceof Button && ((Button) node).getText().equals(">")) {
        if (activeImage >= 8) {
          ((Button) node).setDisable(true);
          startGameButton.setDisable(false);
          startTutorialGameRectangle.setVisible(false);
        } else {
          ((Button) node).setDisable(false);
          startGameButton.setDisable(true);
          startTutorialGameRectangle.setVisible(true);
        }
      }
      if (node instanceof ImageView) {
        iv.setImage(images.get(activeImage));
      }
    }
  }

  /**
   * This method serves as a Listener for the startGameButton. When the Button is clicked it starts
   * a Tutorial Game.
   *
   * @author jbleil
   * @author tthielen
   * @param event ActionEvent that gets triggered when the startGameButton is clicked
   */
  @FXML
  void startGame(ActionEvent event) throws Exception {
    createSinglePlayerLobby();
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/SinglePlayerLobbyScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * Creates a single player lobby by creating a server connection that can't be connected to at the
   * 'JOIN GAME' function. Also sets the Tutorial boolean to true.
   *
   * @author tikrause
   * @author tthielen
   */
  private void createSinglePlayerLobby() {
    int port = 800;
    while (port < 1000) {
      try {
        Server.createServer(port);
        Client.initializeClient("localhost", port, true);
        Client.connectToServer(DataHandler.getOwnPlayer());
        Client.setTutorial(true);
        break;
      } catch (BindException e) {
        port++;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * opens tooltipPane and sets the tooltip text.
   *
   * @author jbleil
   * @param event MouseEvent that gets triggered when hovering over the startTutorialGameRectangle
   */
  @FXML
  void openTooltip(MouseEvent event) {
    Text text =
        new Text(
            "You have to read through all nine pages" + "\nbefore you can start a tutorial game");
    text.relocate(5, 5);
    text.setFill(Paint.valueOf("#f88c00"));
    tooltipPane.getChildren().add(text);
    tooltipPane.setPrefHeight(text.getLayoutBounds().getHeight() + 10);
    tooltipPane.setVisible(true);
  }

  /**
   * closes tolltip when hovering out of the MouseEvent that gets triggered when hovering over the
   * startTutorialGameRectangle.
   *
   * @author jbleil
   * @param event MouseEvent that gets triggered when hovering over the startTutorialGameRectangle
   */
  @FXML
  void closeTooltip(MouseEvent event) {
    tooltipPane.setVisible(false);
  }
}
