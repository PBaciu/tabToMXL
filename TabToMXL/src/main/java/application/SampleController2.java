package application;

import java.io.File;
import java.io.FileNotFoundException;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SampleController2 implements Initializable {

	String info1 = "";
	String info2 = "";
	ArrayList<String> info = new ArrayList();
	
	@FXML
	private AnchorPane rootPane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		rootPane.setOpacity(0);
		makeFade();
	}

	private void makeFade() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
	
		fadeTransition.play();
		
	}
	
	@FXML
	private Button chooseFile;
	
	@FXML
	private Button upload;
	
	@FXML
	private Button convert;
	
	@FXML
	private ListView listView;
	
	@FXML
	private TextArea textArea;
	
	public void ButtonAction(ActionEvent event) {
		FileChooser fc = new FileChooser();
		File selectedFile = fc.showOpenDialog(null);
		
		if(selectedFile != null) {
			if(selectedFile.getName().endsWith(".txt") || selectedFile.getName().endsWith(".rtf")) {
				listView.getItems().add(selectedFile.getName());
			}
			else {
				System.out.println("Invalid File! You need to input a txt file");
			}
			
			if(selectedFile.getName().endsWith(".txt") || selectedFile.getName().endsWith(".rtf")) {
				try (Scanner scanner = new Scanner(selectedFile)) {
			        while (scanner.hasNextLine())
			        	info1 = info1 + scanner.nextLine() + "\n";
			            //System.out.println(scanner.nextLine());
			    } catch (FileNotFoundException e) {
			        e.printStackTrace();
			    }
			}
			System.out.println(info1);
		}
		else {
			System.out.println("No File Chosen");
		}
	}
	
	public void UploadAction(ActionEvent event) {
		if(textArea.getText() != "") {
			//System.out.println(textArea.getText());
			info2 = textArea.getText();
			System.out.println(info2);
		}
		else {
			System.out.println("Text Area is Empty");
		}
	}
	
	public void ConvertAction(ActionEvent event) {
		makeFadeOut();
	}
	
	private void makeFadeOut() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				loadNextScene();
				
			}
		});
		fadeTransition.play();
	}
	
	private void loadNextScene() {
		try {
			Parent secondView = FXMLLoader.load(getClass().getResource("/application/Sample3.fxml"));
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
