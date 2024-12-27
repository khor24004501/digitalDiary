package test1;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WelcomeController {
    private Stage mainStage;
    
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
    
    @FXML
    private Button exitButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    

    @FXML
    void clickToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/test1/Login.fxml"));
            Parent loginRoot = loader.load(); 
            Scene loginScene = new Scene(loginRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("E-Diary Login");
            stage.show();
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clickToRegister(ActionEvent event) {
        try {
        // Load the RegisterPage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/test1/Register.fxml"));
            Parent registerRoot = loader.load();
            Scene registerScene = new Scene(registerRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(registerScene);
            stage.setTitle("E-Diary Register");
            stage.show();
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    @FXML
    void clickToExit(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
