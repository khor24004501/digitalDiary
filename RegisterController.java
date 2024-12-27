package test1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

public class RegisterController {
    @FXML
    private Button buttonBack;
    @FXML
    private Button buttonRegister;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField usernameField;
    private static final String USER_FILE = "users.csv";
    
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

    @FXML
    void clickRegister(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields",Alert.AlertType.ERROR);
            return; // Don't proceed with registration
        }
        // Proceed with registration if fields are not empty
            registerUser(username, email, password);
    }

    private void registerUser(String userName, String email, String password) {
        if (isUserExists(userName, email)) {
            showAlert("Error", "Username or email already exists!",Alert.AlertType.ERROR);
            return;
        }

        try (FileWriter writer = new FileWriter(USER_FILE, true)) {
            writer.write(userName + "," + email + "," + password + "\n");
            showAlert("Success", "Registration successful! Welcome, " + userName,Alert.AlertType.INFORMATION);
            
            //切换去create页面 第一次没有file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/test1/Create.fxml"));
            AnchorPane diaryRoot = loader.load();
            CreateController createController = loader.getController(); 
            createController.setUsername(userName);
            Scene diaryScene = new Scene(diaryRoot);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(diaryScene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Unable to save user data.",Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        setFileDirectory(userName);
    }
    
    public void setFileDirectory(String username){
        File directory = new File("D:/users/" + username + "/MyDiaryApp");
            if (!directory.exists()) {
            directory.mkdirs();
            }
    }
    
    private boolean isUserExists(String userName, String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(userName) || userDetails[1].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
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
