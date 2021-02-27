package org.jfx;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import java.util.Scanner;

import TabToMXL.Parser;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SampleController2 implements Initializable {

	String info1 = "";
	String info2 = "";
	String info3 = "";
	ArrayList<String> info = new ArrayList<>();
	Task progress;
	
	@FXML
	private AnchorPane rootPane;
	
	@FXML
	private ProgressBar progressBar;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		rootPane.setOpacity(0);
		makeFade();
		this.DragDrop();
		
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
	private Button chooseFile;
	
	@FXML
	private Button convert;
	
	@FXML
	private Button help;
	
	@FXML
	private TextField textField;
	
	@FXML
	private TextArea textArea;
	
	public void ButtonAction(ActionEvent event) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text File", "*.txt"));
		File selectedFile = fc.showOpenDialog(null);
		textField.setText("");
		if(selectedFile != null) {
			if(selectedFile.getName().endsWith(".txt")) {
				//listView.getItems().add(selectedFile.getName());
				textField.setText(selectedFile.getName());
			}
			else {
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	            errorAlert.setHeaderText("Invalid File!");
	            errorAlert.setContentText("You need to input a .txt file");
	            errorAlert.showAndWait();
				//System.out.println("Invalid File! You need to input a txt file");
			}
			
			if(selectedFile.getName().endsWith(".txt") && info1.equals("")) {
				try (Scanner scanner = new Scanner(selectedFile)) {
			        while (scanner.hasNextLine())
			        	info1 = info1 + scanner.nextLine() + "\n";
			    } catch (FileNotFoundException e1) {
			        e1.printStackTrace();
			    }
			}
			else {
				info1 = "";
				try (Scanner scanner = new Scanner(selectedFile)) {
			        while (scanner.hasNextLine())
			        	info1 = info1 + scanner.nextLine() + "\n";
			    } catch (FileNotFoundException e2) {
			        e2.printStackTrace();
			    }
			}
			//System.out.println(info1);
			if(textArea.getText().isEmpty()) {
				textArea.setText(info1);
			}
			else {
				textArea.clear();
				textArea.setText(info1);
			}
		}
		else {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("No File Chosen");
            errorAlert.setContentText("Please choose a File before you preview");
            errorAlert.showAndWait();
			//System.out.println("No File Chosen");
		}

	}
	
	public void ConvertAction() {
		if(textArea.getText() != "") {
			//System.out.println(textArea.getText());
			info2 = textArea.getText();
			//System.out.println(info2);
//			progress = createWorker();
//			new Thread(progress).start();
//			progressBar.progressProperty().unbind();
//	        progressBar.progressProperty().bind(progress.progressProperty());
//	        progressBar.setProgress(1);
//			if(progressBar.getProgress() >= 0) {
//				makeFadeOut();
//			}
//			for(double i = 0.0; i <= 10.0; i++) {
//				progressBar.setProgress(i*0.01);
//				if(progressBar.getProgress() >= 1) {
//					makeFadeOut();
//				}
//			}
			makeFadeOut();
//			loadNextScene();
		}
		else {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("No Content to Convert");
            errorAlert.setContentText("Please choose a File or Copy/Paste a Tablature to convert");
            errorAlert.showAndWait();
		}
	}
	
	public void HelpAction() {
		Alert helpAlert = new Alert(Alert.AlertType.INFORMATION);
        helpAlert.setHeaderText("Information on Usage");
        helpAlert.setContentText("You can Drag and Drop a file in the Text Field given in this screen. You can also Browse for a File from your computer."
        		+ "\n" + "The Files should only be of the format .txt or .rtf." + "\n" + "The Uploaded Files will have their content displayed on the Cop/Paste area which can be modified to the Users' preference."
        		+ "\n" + "Hitting the Convert button converts the final variation of the Tablature in the Cop/Paste Text Area into a musicxml file.");
        helpAlert.showAndWait();
	}

	private void makeFadeOut() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(500));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.setOnFinished(event -> loadNextScene());
		fadeTransition.play();
	}
	
	private void loadNextScene() {
		try {
			Parser p = new Parser(textArea.getText());
			var tab = p.readTab();
			Thread thread = new Thread(() -> {
				try {
					p.generateXML(tab);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			});
			thread.start();
			thread.join();

//			progress = createWorker();
//			new Thread(progress).start();
//			progressBar.progressProperty().unbind();
//	        progressBar.progressProperty().bind(progress.progressProperty());
//	        progressBar.setProgress(1);

			Parent secondView = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("org.jfx/Sample3.fxml")));
			Scene newScene = new Scene(secondView);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage curStage = (Stage) rootPane.getScene().getWindow();
			curStage.setScene(newScene);
			curStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
 	}
	
	private void DragDrop() {
		textField.setEditable(false);
		textField.setMinWidth(250);
		//textField.setText("");
		textField.setOnDragOver((e) -> {
	        if (e.getGestureSource() != textField && e.getDragboard().hasFiles()) {
	            e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
	        }
	        e.consume();
	    });
		textField.setOnDragDropped(e -> {

	        Dragboard db = e.getDragboard();

	        boolean result = false;
	        if (db.hasFiles()){

	        	//listView.getItems().add(db.getFiles().toString());
	        	File droppedFile = db.getFiles().get(0);
	        	textField.setText("");
	        	if(droppedFile != null) {
	    			if(droppedFile.getName().endsWith(".txt")) {
	    				//listView.getItems().add(droppedFile.getName());
	    				textField.setText(droppedFile.getName());
	    			}
	    			else {
	    				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	    	            errorAlert.setHeaderText("Invalid File!");
	    	            errorAlert.setContentText("You need to input a .txt file");
	    	            errorAlert.showAndWait();
	    			}
	    			
	    			if(droppedFile.getName().endsWith(".txt")  && info3.equals("")) {
	    				try (Scanner scanner = new Scanner(droppedFile)) {
	    			        while (scanner.hasNextLine())
	    			        	info3 = info3 + scanner.nextLine() + "\n";
	    			    } catch (FileNotFoundException e3) {
	    			        e3.printStackTrace();
	    			    }
	    			}
	    			else {
	    				info3 = "";
	    				try (Scanner scanner = new Scanner(droppedFile)) {
	    			        while (scanner.hasNextLine())
	    			        	info3 = info3 + scanner.nextLine() + "\n";
	    			    } catch (FileNotFoundException e4) {
	    			        e4.printStackTrace();
	    			    }
	    			}
	    			//System.out.println(info3);
	    			if(textArea.getText().equals("")) {
	    				textArea.setText(info3);
	    			}
	    			else {
	    				textArea.clear();
	    				textArea.setText(info3);
	    			}
	    		}
	    		else {
	    			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	                errorAlert.setHeaderText("No File Chosen");
	                errorAlert.setContentText("Please choose a File before you preview");
	                errorAlert.showAndWait();
	    		}
	            result = true;
	        }

	        e.setDropCompleted(result);
	        e.consume();

	    });
	}
}
