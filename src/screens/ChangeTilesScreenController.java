package screens;

import gameentities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import network.Client;

public class ChangeTilesScreenController {

  @FXML private Pane backgroundPane;

  TextField[] countFields = new TextField[27];
  TextField[] valueFields = new TextField[27];

  /**
   * this method gets the Letter of a Tile with the corresponding count and value from the Bag.
   * These informations are getting displayed on the screen in 3 different gridPanes.
   *
   * @author jbleil
   */
  public void initialize() {
    Text value1 = new Text("Value");
    value1.relocate(130, 90);
    backgroundPane.getChildren().add(value1);
    Text count1 = new Text("Count");
    count1.relocate(225, 90);
    backgroundPane.getChildren().add(count1);
    Text value2 = new Text("Value");
    value2.relocate(430, 90);
    backgroundPane.getChildren().add(value2);
    Text count2 = new Text("Count");
    count2.relocate(525, 90);
    backgroundPane.getChildren().add(count2);
    Text value3 = new Text("Value");
    value3.relocate(730, 90);
    backgroundPane.getChildren().add(value3);
    Text count3 = new Text("Count");
    count3.relocate(825, 90);
    backgroundPane.getChildren().add(count3);
    Bag bag = Client.getGameSession().getBag();
    GridPane grid1 = new GridPane();
    grid1.setHgap(15);
    grid1.setVgap(10);
    grid1.relocate(100, 120);
    GridPane grid2 = new GridPane();
    grid2.setHgap(15);
    grid2.setVgap(10);
    grid2.relocate(400, 120);
    backgroundPane.getChildren().add(grid2);
    GridPane grid3 = new GridPane();
    grid3.setHgap(15);
    grid3.setVgap(10);
    grid3.relocate(700, 120);
    backgroundPane.getChildren().add(grid3);
    int rowCounter = 0;
    backgroundPane.getChildren().add(grid1);
    for (TileContainer t : bag.getTileCounter()) {
      if (rowCounter < 9) {
        grid1.add(new Text(t.getTile().getLetter() + " :"), 0, rowCounter);
        TextField valueField = new TextField();
        valueField.setPrefWidth(80);
        valueField.setText("" + t.getTile().getValue());
        valueFields[rowCounter] = valueField;
        grid1.add(valueField, 1, rowCounter);
        TextField countField = new TextField();
        countField.setPrefWidth(80);
        countField.setText("" + t.getCount());
        countFields[rowCounter] = countField;
        grid1.add(countField, 2, rowCounter);
        rowCounter++;
      } else if (rowCounter < 18) {
        grid2.add(new Text(t.getTile().getLetter() + " :"), 0, rowCounter - 9);
        TextField valueField = new TextField();
        valueField.setPrefWidth(80);
        valueField.setText("" + t.getTile().getValue());
        valueFields[rowCounter] = valueField;
        grid2.add(valueField, 1, rowCounter - 9);
        TextField countField = new TextField();
        countField.setPrefWidth(80);
        countField.setText("" + t.getCount());
        countFields[rowCounter] = countField;
        grid2.add(countField, 2, rowCounter - 9);
        rowCounter++;
      } else {
        grid3.add(new Text(t.getTile().getLetter() + " :"), 0, rowCounter - 18);
        TextField valueField = new TextField();
        valueField.setPrefWidth(80);
        valueField.setText("" + t.getTile().getValue());
        valueFields[rowCounter] = valueField;
        grid3.add(valueField, 1, rowCounter - 18);
        TextField countField = new TextField();
        countField.setPrefWidth(80);
        countField.setText("" + t.getCount());
        countFields[rowCounter] = countField;
        grid3.add(countField, 2, rowCounter - 18);
        rowCounter++;
      }
    }
  }

  @FXML
  void submitChanges(ActionEvent event) throws Exception {
    Bag bag = new Bag();
    int counter = 0;
    for (TileContainer t : bag.getTileCounter()) {
      t.getTile().setValue(Integer.parseInt(valueFields[counter].getText()));
      t.setCount(Integer.parseInt(countFields[counter].getText()));
      counter++;
    }
    bag.refreshBag();
    Client.getGameSession().setBag(bag);
    Client.getGameSession().sendGameStateMessage();
    back(event);
  }

  @FXML
  void back(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/LobbyScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }
}
