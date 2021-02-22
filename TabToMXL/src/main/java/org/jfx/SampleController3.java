package org.jfx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SampleController3 implements Initializable {
	
	String musicXML = "";
	
	@FXML
	private AnchorPane rootPane;
	
	FileChooser fileChooser = new FileChooser();
	
	//Stage curStage = (Stage) rootPane.getScene().getWindow();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		rootPane.setOpacity(0);
		makeFade();
		this.ViewResult();
	}
	
	private void makeFade() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(500));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
	
		fadeTransition.play();
		
	}
	
	@FXML
	private Button saveButton;
	
	@FXML
	private Button viewButton;
	
	@FXML
	private TextArea textArea;
	
	public void SaveAction(ActionEvent event) {
	
		File file = fileChooser.showSaveDialog(new Stage());
		
		File sampleFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("org.jfx/sample.musicxml").getFile()));
		//File sampleFile = new File(getClass().getResource("/application/SampleMXLFile").getFile());{
		if(sampleFile != null) {
			try (Scanner scanner = new Scanner(sampleFile)) {
		        while (scanner.hasNextLine())
		        	musicXML = musicXML + scanner.nextLine() + "\n";
		            //System.out.println(scanner.nextLine());
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
		}
		
		if(file != null) {
			saveSystem(file,musicXML);	
			
		}
	}
	
	
	public void saveSystem(File file, String content) {
		try {
			PrintWriter printWriter = new PrintWriter(file);
			printWriter.write(content);
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void ViewResult() {
		File sampleFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("org.jfx/sample.musicxml").getFile()));
		//File sampleFile = new File(getClass().getResource("/application/SampleMXLFile").getFile());{
		if(sampleFile != null) {
			try (Scanner scanner = new Scanner(sampleFile)) {
		        while (scanner.hasNextLine())
		        	musicXML = musicXML + scanner.nextLine() + "\n";
		            //System.out.println(scanner.nextLine());
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
		}
		if(musicXML == "") {
			textArea.setText("The File is empty");
		}
		else {
			textArea.setText(musicXML);
		}
	}
	
	
}
