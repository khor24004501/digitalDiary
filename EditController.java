package test1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditController {
    @FXML
    private TextArea contentField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Slider moodSlider;
    @FXML
    private TextField titleField;
    private String filePath;
    private String username; 
    private String currentEntry;
    //private List<String> entryRow;

    public void setUsername(String username) {
        this.username = username;
        this.filePath = new File("D:/users/" + this.username + "/MyDiaryApp/entries.csv").getAbsolutePath();
    }
    
    public void setCurrentEntry(String entry, String filepath){
        this.currentEntry = entry;
        System.out.println("Selected Entry in CreateController: " + currentEntry);
        
        try(BufferedReader br = new BufferedReader(new FileReader(filepath))){
            String line;
            br.readLine();//skip header
            while((line = br.readLine())!=null){
                String[]column = line.split(",");
                if(column[0].equals(currentEntry)){
                    titleField.setText(column[0]);
                    datePicker.setValue(convertStringToDate(column[1]));
                    moodSlider.setValue(Double.parseDouble(column[2]));
                    contentField.setText(column[3]);
                    break;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
     @FXML
    void clickToBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/test1/EntriesDisplay.fxml"));
                AnchorPane diaryRoot = loader.load();
                EntriesController entryController = loader.getController(); 
                entryController.setUsername(username);
                System.out.println("click Back to entries:  " + username);
                entryController.populate();
                Scene diaryScene = new Scene(diaryRoot);
                
                Stage stage = (Stage) contentField.getScene().getWindow();
                stage.setScene(diaryScene);
                stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clickToChooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    
        Stage stage = (Stage) titleField.getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();

            showAlert("Success", "File path has been set successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "No file selected. File path has been set to default location.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void clickToSave(ActionEvent event) {
        System.out.println(username + " Save");
        
        /*if (titleField.getText().isEmpty() || contentField.getText().isEmpty()){
            showAlert("Incomplete","Please fill in the required fields.",Alert.AlertType.ERROR);
            return;
        }
        
        String title = titleField.getText();
        LocalDate date = (datePicker.getValue() != null)? datePicker.getValue() : LocalDate.now();
        String content = contentField.getText();
        double mood = moodSlider.getValue();
        
        if (filePath == null || filePath.isEmpty()) {
            filePath = new File("D:/users/" + username + "/MyDiaryApp/entries.csv").getAbsolutePath();
            return; 
        }
        
        File file = new File(filePath);
        boolean isNewFile = false;
        
        String actualContent = escapeCSV(content);
        
        try {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                showAlert("Error", "Failed to create directory for the file.", Alert.AlertType.ERROR);
                return;  // Abort further operations
            }

            
            if (!file.exists()){
                isNewFile = file.createNewFile();
            }
            
            try (FileWriter writer = new FileWriter(file, true)){
                if (isNewFile){
                    writer.write("Title,Date,Mood,Content\n");
                }
                writer.write(title+","+date+","+mood+","+actualContent+"\n");
                showAlert("Success","Diary entry is created successfully!", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to write to the file.", Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error","Diary entry creation failed.",Alert.AlertType.ERROR);
        }*/
        
        if (titleField.getText().isEmpty() || contentField.getText().isEmpty()) {
        showAlert("Incomplete", "Please fill in the required fields.", Alert.AlertType.ERROR);
        return;
        }

        String title = titleField.getText();
        LocalDate date = (datePicker.getValue() != null) ? datePicker.getValue() : LocalDate.now();
        String content = contentField.getText();
        double mood = moodSlider.getValue();

        if (filePath == null || filePath.isEmpty()) {
            filePath = new File("D:/users/" + username + "/MyDiaryApp/entries.csv").getAbsolutePath();
            return;
        }

        File file = new File(filePath);
        boolean isModified = false;

        String actualContent = escapeCSV(content);

        //try {
            // Ensure the file's parent directory exists
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                showAlert("Error", "Failed to create directory for the file.", Alert.AlertType.ERROR);
                return;  // Abort further operations
            }

            // Read the entire CSV file into memory
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to read the file.", Alert.AlertType.ERROR);
                return;
            }

        // Find the row to update
            boolean headerProcessed = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] columns = line.split(",");

                if (columns.length >= 4 && columns[0].equals(currentEntry)) {
                // Update the row with new values
                    lines.set(i, title + "," + date + "," + mood + "," + actualContent);
                    isModified = true;
                    break;
                }
            }

            // If the entry was not found, show an error and return
            if (!isModified) {
                showAlert("Error", "Entry not found to update.", Alert.AlertType.ERROR);
                return;
            }

            // Now write the updated content back to the file
            try (FileWriter writer = new FileWriter(file)) {
                for (String line : lines) {
                    writer.write(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to write to the file.", Alert.AlertType.ERROR);
            }

            // Success message
            showAlert("Success", "Diary entry updated successfully!", Alert.AlertType.INFORMATION);

       /* } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Diary entry update failed.", Alert.AlertType.ERROR);
        }*/

        
        try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/test1/EntriesDisplay.fxml"));
                AnchorPane diaryRoot = loader.load();  
                EntriesController entriesController = loader.getController(); 
                entriesController.setUsername(username);
                entriesController.populate();
                Scene diaryScene = new Scene(diaryRoot);
                Stage stage = (Stage) titleField.getScene().getWindow();
                stage.setScene(diaryScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();  // Handle the exception if loading the new scene fails
            }
    }
    
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private String escapeCSV(String input) {
        if (input.contains(",") || input.contains("\n") || input.contains("\"")) {
            input = input.replace("\"", "\"\""); 
            input = "\"" + input + "\"";         
        }
        return input;
    }
    
    private LocalDate convertStringToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            return LocalDate.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
