//要在properties library add resources file（jar） 不要add错同名的file
//然后clean n build
package test1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main1 extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("/test1/Welcome.fxml"));
            AnchorPane welcomePage = welcomeLoader.load();
    
            Scene welcomeScene = new Scene(welcomePage);
            primaryStage.setScene(welcomeScene);
            primaryStage.setTitle("E-Diary");

            primaryStage.show();

            WelcomeController welcomeController = welcomeLoader.getController();
            welcomeController.setMainStage(primaryStage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); 
    }
}
