package application;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SampleController4 implements Initializable {
	
	String musicXML = "";
	
	@FXML
	private AnchorPane rootPane;
	
	Stage curStage = (Stage) rootPane.getScene().getWindow();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		rootPane.setOpacity(0);
	}
	
	@FXML
	private Button saveButton;
	
	@FXML
	private Button viewButton;
	
	public void SaveAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		 
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(curStage);

        if (file != null) {
            saveTextToFile(musicXML, file);
        }
	}
	
	private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(SampleController4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
