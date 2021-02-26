package org.jfx;

import java.io.*;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import TabToMXL.Parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
	private Button backButton;
	
	@FXML
	private TextArea textArea;
	
	public void SaveAction(ActionEvent event) {
		
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MusicXML File", "*.musicxml"));
		File file = fileChooser.showSaveDialog(new Stage());

		BufferedReader sampleFile = null;
		try {
			sampleFile = new BufferedReader(new FileReader(System.getProperty("java.io.tmpdir") + "result.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//File sampleFile = new File(getClass().getResource("/application/SampleMXLFile").getFile());{
		if(sampleFile != null) {
			try (Scanner scanner = new Scanner(sampleFile)) {
		        while (scanner.hasNextLine())
		        	musicXML = musicXML + scanner.nextLine() + "\n";
		            //System.out.println(scanner.nextLine());
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
		BufferedReader sampleFile = null;
		try {
			sampleFile = new BufferedReader(new FileReader(System.getProperty("java.io.tmpdir") + "result.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(sampleFile != null) {
			try (Scanner scanner = new Scanner(sampleFile)) {
		        while (scanner.hasNextLine())
		        	musicXML = musicXML + scanner.nextLine() + "\n";
		            //System.out.println(scanner.nextLine());
		    }
		}
		if(musicXML == "") {
			textArea.setText("The File is empty");
		}
		else {
			textArea.setText(musicXML);
		}
	}
	
	public void BackAction(ActionEvent event) {
		makeFadeOut();
	}
	
	private void makeFadeOut() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(500));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				loadPrevScene();
				
			}
		});
		fadeTransition.play();
	}
	
	private void loadPrevScene() {
		try {
			Parent secondView = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("org.jfx/Sample2.fxml")));
			Scene newScene = new Scene(secondView);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage curStage = (Stage) rootPane.getScene().getWindow();
			curStage.setScene(newScene);
			curStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
 	}
	
	
}
