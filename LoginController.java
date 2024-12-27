package test1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private Button buttonBack;
    @FXML
    private Button buttonLogin;
    @FXML
    private TextField loginPasswordField;
    @FXML
    public TextField loginUserField;
    
    private String userName; 
    private static final String USER_FILE = "users.csv";
    
    public void setUsername(String userInput) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if ((userInput.equals(userDetails[0]))) {
                    this.userName = userDetails[0];
                    break;
                }else if(userInput.equals(userDetails[1])){
                    this.userName = userDetails[0];
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            showAlert("Error", "User file not found. Please register.",Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void clickLogin() {
        //click button开始check
        if (authenticateUser(loginUserField.getText(), loginPasswordField.getText())) {
            showAlert("Success", "Login successful! Welcome back.",Alert.AlertType.INFORMATION);
            setUsername(loginUserField.getText());
            //System.out.println(userName + " login");
            //切换去entries页面
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/test1/EntriesDisplay.fxml"));
                AnchorPane diaryRoot = loader.load();
                EntriesController entryController = loader.getController(); 
                entryController.setUsername(userName);
                System.out.println("clickLogin " + userName);
                entryController.populate();
                
                Scene diaryScene = new Scene(diaryRoot);                
                Stage stage = (Stage) loginUserField.getScene().getWindow();
                stage.setScene(diaryScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();  // Handle the exception if loading the new scene fails
            }
        } else {
            showAlert("Error", "Invalid username/email or password.",Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    void clickBack(ActionEvent event) {
        try {
            //切换回主页
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/test1/Welcome.fxml"));
            Parent mainPageRoot = loader.load(); 
            Scene mainPageScene = new Scene(mainPageRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainPageScene);
            stage.setTitle("E-Diary");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean authenticateUser(String userInput, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if ((userDetails[0].equals(userInput) || userDetails[1].equals(userInput))
                        && userDetails[2].equals(password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            showAlert("Error", "User file not found. Please register.",Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
