package screens;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.Client;
import gameentities.*;

public class EndScreenController {

  @FXML private Pane backgroundPane;

  /**
   * the initialize method displays the Players, their place, their points and their avatar on the
   * screen.
   *
   * @author jbleil
   */
  public void initialize() {
    ArrayList<Player> players = Client.getGameSession().getPlayerList();
    GridPane grid1 = new GridPane();
    grid1.setHgap(50);
    grid1.setVgap(20);
    grid1.setPrefWidth(200);

    for (int i = 0; i < players.size(); i++) {
      Text place = new Text((i + 1) + ".Place:");
      place.setFill(Paint.valueOf("#f88c00"));
      place.setFont(new Font(18));
      grid1.add(place, i, 0);
      GridPane.setHalignment(place, HPos.CENTER);
      Image avatar = new Image(players.get(i).getAvatar().getUrl(), 52, 52, true, true);
      grid1.add(new ImageView(avatar), i, 1);
      GridPane.setHalignment(new ImageView(avatar), HPos.CENTER);
      Text name = new Text(players.get(i).getUsername());
      name.setFill(Paint.valueOf("#f88c00"));
      name.setFont(new Font(18));
      name.relocate(0, 150);
      grid1.add(name, i, 2);
      GridPane.setHalignment(name, HPos.CENTER);
      Text points = new Text("Points: " + players.get(i).getScore());
      points.setFill(Paint.valueOf("#f88c00"));
      points.setFont(new Font(18));
      grid1.add(points, i, 3);
      GridPane.setHalignment(points, HPos.CENTER);
    }
    grid1.relocate(80, 150);

    backgroundPane.getChildren().add(grid1);
  }

  @FXML
  void leaveGame(ActionEvent event) throws Exception {}

  @FXML
  void playAgain(ActionEvent event) throws Exception {}

  /**
   * This method serves as a Listener for the Text "Soni Sokell" and displays an instance of
   * NameLinkScreen.
   *
   * @author jbleil
   * @param event the MouseEvent that gets thrown when clicking on the "Soni Sokell" Text
   * @throws Exception
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
   * @throws Exception
   */
  @FXML
  void openSiteScreen(MouseEvent event) throws Exception {
    SiteLinkScreen sls = new SiteLinkScreen();
    Stage stage = new Stage();
    sls.start(stage);
  }
}
