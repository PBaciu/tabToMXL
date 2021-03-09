package org.jfx;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SampleController3 implements Initializable {
	
	StringBuilder musicXMLBuilder = new StringBuilder();
	
	@FXML
	private AnchorPane rootPane;
	
	FileChooser fileChooser = new FileChooser();
	
	String textArea2;
	String textField2;
	String fileContent;
	
	//Stage curStage = (Stage) rootPane.getScene().getWindow();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		rootPane.setOpacity(0);
		makeFade();
		this.viewResult();
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
	private Button help;
	
	@FXML
	private Button newConversion;
	
	@FXML
	private TextArea textArea;
	
	public void initData(String textArea, String textField, String fileContent) {
		this.textArea2 = textArea;
		this.textField2 = textField;
		this.fileContent = fileContent;
	}
	
	public void saveAction() {
		
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MusicXML File", "*.musicxml"));
		File file = fileChooser.showSaveDialog(new Stage());

		StringBuilder content = new StringBuilder();
		try {
			for(var line: Files.readAllLines(Path.of( System.getProperty("java.io.tmpdir") + "result.xml")) ){
				content.append(line);
				content.append("\n");
			}
			saveSystem(file, content.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void saveSystem(File file, String content) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void viewResult() {
		BufferedReader sampleFile = null;
		try {
			sampleFile = new BufferedReader(new FileReader(System.getProperty("java.io.tmpdir") + "result.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(sampleFile != null) {
			try (Scanner scanner = new Scanner(sampleFile)) {
		        while (scanner.hasNextLine())
		        	musicXMLBuilder.append(scanner.nextLine()).append("\n");
		            //System.out.println(scanner.nextLine());
		    }
		}
		if(musicXMLBuilder.toString().equals("")) {
			textArea.setText("The File is empty");
		}
		else {
			textArea.setText(musicXMLBuilder.toString());
		}
	}
	
	public void helpAction() {
		Alert helpAlert = new Alert(Alert.AlertType.INFORMATION);
        helpAlert.setHeaderText("Information on Usage");
        helpAlert.setContentText("•This page displays the Converted tablature in a MusicXML format. \n"
        		+ "\n" + "•You can save the contents that are displayed into your computer using the Save button (Can only be saved as a .musicxml file. \n"
        		+ "\n" + "•The Back button takes you back to the previous page to help you perform a new Tablature Conversion.");
        helpAlert.showAndWait();
	}
	
	public void newConvertAction() {
		makeFadeOut2();
	}
	
	public void backAction() {
		makeFadeOut();
	}
	
	private void makeFadeOut() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(500));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.setOnFinished(event -> loadPrevScene());
		fadeTransition.play();
	}
	
	private void makeFadeOut2() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(500));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.setOnFinished(event -> loadPrevScene2());
		fadeTransition.play();
	}
	
	private void loadPrevScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("org.jfx/Sample2.fxml"));
			Parent secondView = loader.load();
			Scene newScene = new Scene(secondView);
			SampleController2 controller = loader.getController();
			controller.initData(textArea2, textField2, this.fileContent);
			Stage curStage = (Stage) rootPane.getScene().getWindow();
			curStage.setScene(newScene);
			curStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
 	}
	
	private void loadPrevScene2() {
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
