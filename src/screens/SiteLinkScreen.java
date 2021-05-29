package screens;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * This class provides the methods to launch the Screen for the Site Link of "freeicons.io" which is
 * present on every screen that uses avatars.
 *
 * @author jbleil
 */
public class SiteLinkScreen extends Application {

  /**
   * this method launches the application.
   *
   * @author jbleil
   * @param args String[] of the main method
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * sets the scene of the stage.
   *
   * @author jbleil
   */
  public void start(Stage primaryStage) {
    WebView webView = new WebView();

    webView.getEngine().load("https://freeicons.io");

    VBox vBox = new VBox(webView);

    Scene scene = new Scene(vBox, 1000, 562);

    primaryStage.setScene(scene);

    primaryStage.show();
  }
}
