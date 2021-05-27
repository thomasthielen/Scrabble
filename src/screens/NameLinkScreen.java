package screens;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class NameLinkScreen extends Application { 
  
  public static void main(String[] args) {
    launch(args);
  }
  
  public void start(Stage primaryStage) {
    WebView webView = new WebView();
    
    webView.getEngine().load("https://freeicons.io/profile/6200");
    
    VBox vBox = new VBox(webView);
    
    Scene scene = new Scene(vBox, 1000, 562);
    
    primaryStage.setScene(scene);
    
    primaryStage.show();
  }
}
