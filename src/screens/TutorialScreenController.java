package screens;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
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
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

public class TutorialScreenController {

  private ArrayList<Image> images = new ArrayList<Image>();

  private static int activeImage = 0;

  private Text imageCounter;

  private ImageView iv = new ImageView();

  @FXML private Button previousButton;

  @FXML private Button nextButton;

  @FXML private Pane backgroundPane;

  public void initialize() {
    images.add(new Image("screens/resources/Rack1.jpg"));
    images.add(new Image("screens/resources/Rack2.jpg"));
    images.add(new Image("screens/resources/Rack3.jpg"));
    images.add(new Image("screens/resources/GameBoard1.jpg"));
    images.add(new Image("screens/resources/GameBoard2.jpg"));
    images.add(new Image("screens/resources/GameBoard3.jpg"));
    images.add(new Image("screens/resources/GameBoard4.jpg"));
    images.add(new Image("screens/resources/GameBoard5.jpg"));
    images.add(new Image("screens/resources/GameBoard6.jpg"));

    imageCounter = new Text((activeImage + 1) + "/9");
    imageCounter.setFont(new Font(25));
    imageCounter.setX(20);
    imageCounter.setY(20);
    backgroundPane.getChildren().add(imageCounter);

    updateImage();
  }

  @FXML
  void previous(ActionEvent event) throws Exception {
    backgroundPane.getChildren().remove(iv);
    activeImage -= activeImage > 0 ? 1 : 0;
    updateImage();
  }

  @FXML
  void next(ActionEvent event) throws Exception {
    activeImage += activeImage < 8 ? 1 : 0;
    updateImage();
  }

  @FXML
  void leaveTutorial(ActionEvent event) throws Exception {
    StartScreen.getStage()
        .setOnCloseRequest(
            new EventHandler<WindowEvent>() {
              @Override
              public void handle(final WindowEvent event) {
                Platform.exit();
                System.exit(0);
              }
            });
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

  private void updateImage() {
    //    if (activeImage <= 0) {
    //      previousButton.setDisable(true);
    //    } else {
    //      previousButton.setDisable(false);
    //    }
    //    if (activeImage >= 8) {
    //      nextButton.setDisable(true);
    //    } else {
    //      nextButton.setDisable(false);
    //    }

    for (Node node : backgroundPane.getChildren()) {
      if (node instanceof Text && ((Text) node).getText().contains("/9")) {
        ((Text) node).setText((activeImage + 1) + "/9");
      }
      if (node instanceof Button && ((Button) node).getText().equals("PREVIOUS")) {
        if (activeImage <= 0) {
          ((Button) node).setDisable(true);
        } else {
          ((Button) node).setDisable(false);
        }
      }
      if (node instanceof Button && ((Button) node).getText().equals("NEXT")) {
        if (activeImage >= 8) {
          ((Button) node).setDisable(true);
        } else {
          ((Button) node).setDisable(false);
        }
      }
    }
    imageCounter = new Text("" + activeImage);
    iv = new ImageView(images.get(activeImage));
    iv.setX(200);
    iv.setY(100);
    iv.setPreserveRatio(true);
    iv.setFitWidth(600);
    backgroundPane.getChildren().add(iv);
  }
}
