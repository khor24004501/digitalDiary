package test1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EntriesController {
    @FXML
    private Button createButton;
    @FXML
    private Button editButton;
    @FXML
    private Button exitButton;
    @FXML
    private ListView<String> listView;
    private String filePath;
    private String username; 
    String currentChoose;

    public void setUsername(String username) {
        this.username = username;
        this.filePath = new File("D:/users/" + this.username + "/MyDiaryApp/entries.csv").getAbsolutePath();
        //System.out.println("In setUsername " + this.username);
    }
    
    
    @FXML
    void clickToCreateNew(ActionEvent event) {
        try {   
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/test1/Create.fxml"));
                AnchorPane diaryRoot = loader.load();
                CreateController createController = loader.getController(); 
                createController.setUsername(username);
                //System.out.println("clickLogin " + username);
                Scene diaryScene = new Scene(diaryRoot);
                
                Stage stage = (Stage) createButton.getScene().getWindow();
                stage.setScene(diaryScene);
                stage.show();
                
            } catch (IOException e) {
                e.printStackTrace(); 
            }
    }

    @FXML
    void clickToEdit(ActionEvent event) {
        try {   
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/test1/Edit.fxml"));
                AnchorPane diaryRoot = loader.load();
                
                EditController editController = loader.getController();
                editController.setUsername(this.username);
                editController.setCurrentEntry(currentChoose,filePath);
                
                
                Scene diaryScene = new Scene(diaryRoot);
                Stage stage = (Stage) createButton.getScene().getWindow();
                stage.setScene(diaryScene);
                stage.show();
                
            } catch (IOException e) {
                e.printStackTrace();  
            }
    }
    
    public void populate() {
        //System.out.println("In populate " + this.username);
        if (filePath == null || filePath.isEmpty()) {
            filePath = new File("D:/users/" + username + "/MyDiaryApp/entries.csv").getAbsolutePath();
        }
        
        List<String> entries = getDiaryEntries();
        listView.getItems().addAll(entries); 
        
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentChoose = newValue;
                System.out.println("Current choose: " + currentChoose);
            }
        });
    }
    
    private List<String> getDiaryEntries() {
        List<String> entries = new ArrayList<>();
        
        //System.out.println("File Path: " + filePath);
    
        // Ensure the file exists
        File file = new File(filePath);
        if (!file.exists()) {
            showAlert("File: " + filePath);
            return entries;
        }
        
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            br.readLine();
            String line;
            while((line = br.readLine())!=null){
                String[] column = line.split(",");
                if (column.length > 0) {
                String title = column[0].trim(); 
                
                System.out.println("Title: " + title);
                entries.add(title);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return entries;
    }
    
    
    @FXML
    void clickToExit(ActionEvent event) {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

}
