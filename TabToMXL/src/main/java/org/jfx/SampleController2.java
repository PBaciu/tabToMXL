package org.jfx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import java.util.Scanner;

import TabToMXL.Parser;
import TabToMXL.ScoreWriter;
import generated.ScorePartwise;
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

import javax.xml.bind.JAXBException;

public class SampleController2 implements Initializable {

	String info1;
	String info2;
	String info3;
	Boolean saved = false;
	File fileName;
	FXMLLoader loader;
	ArrayList<String> info = new ArrayList<>();
	
	@FXML
	private AnchorPane rootPane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loader = new FXMLLoader();
		loader.setLocation(getClass().getClassLoader().getResource("org.jfx/Sample3.fxml"));
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
	private Button saveChanges;
	
	@FXML
	private TextField textField;
	
	@FXML
	private TextArea textArea;
	
	public void initData(String textArea2, String textField2) {
		textArea.setText(textArea2);
		textField.setText(textField2);;
	}
	
	public void ButtonAction(ActionEvent event) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text File", "*.txt"));
		File selectedFile = fc.showOpenDialog(null);
		info1 = "";
		if(selectedFile != null) {
			if(selectedFile.getName().endsWith(".txt")) {
				//listView.getItems().add(selectedFile.getName());
				textField.setText(selectedFile.getName());
				fileName = selectedFile;
			}


			if(selectedFile.getName().endsWith(".txt") && info1.equals("")) {
				try (Scanner scanner = new Scanner(selectedFile)) {
			        while (scanner.hasNextLine())
			        	info1 = info1 + scanner.nextLine() + "\n";
			    } catch (FileNotFoundException e1) {
			        e1.printStackTrace();
			    }
			}
			else if (selectedFile.getName().endsWith(".txt")) {
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
		else if (textArea.getText().equals("")) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("No File Chosen");
            errorAlert.setContentText("Please choose a File before you preview");
            errorAlert.showAndWait();
			//System.out.println("No File Chosen");
		}

	}
	
	public void ConvertAction() {
		if(!textArea.getText().equals("")) {
			info2 = textArea.getText();
			if(info2 != info1 && textField.getText() != "") {
				if(saved) {
					loadNextScene();
				}
				else {
					saved = true;
					Alert saveAlert = new Alert(Alert.AlertType.WARNING);
					saveAlert.setHeaderText("You seem to have made changes to your Tablature");
					saveAlert.setContentText("Please click the  Save Changes button if you wish to save them to a file.");
					saveAlert.showAndWait();
				}
			}
			else {
				loadNextScene();
			}
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
        helpAlert.setContentText("""
				You can Drag and Drop a file in the Text Field given in this screen. You can also Browse for a File from your computer.
				The Files should only be of a .txt format
				The Uploaded Files will have their content displayed on the Copy/Paste area which can be modified to the Users' preference.
				Hitting the Convert button converts the final variation of the Tablature in the Copy/Paste Text Area into a musicxml file.
				The Save Changes button allows you to save the changes you made in the Tablature to a file of your choosing.""");
        helpAlert.showAndWait();
	}

	private void makeFadeOut(Parent secondView) {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(500));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.setOnFinished(event -> {
			Scene newScene = new Scene(secondView);
			SampleController3 controller = loader.getController();
			controller.initData(textArea.getText(), textField.getText());
			Stage curStage = (Stage) rootPane.getScene().getWindow();
			curStage.setScene(newScene);
			curStage.show();
		});
		fadeTransition.play();
	}
	
	private void loadNextScene() {
		Parser p = new Parser();
		try {
			ScoreWriter writer = new ScoreWriter();

			ScorePartwise scorePartwise = p.readTab(textArea.getText());
			writer.writeToTempFile(scorePartwise);
		} catch (Exception e) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			errorAlert.setHeaderText("Unsupported Tablature Format Error");
			errorAlert.setContentText("Please ensure that the tablature provided matches the format provided in .README");
			errorAlert.showAndWait();
			return;
		}

		Parent secondView;
		try {
			secondView = this.loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		makeFadeOut(secondView);
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
	        	
	        	if(db.getFiles().size() == 1) {
	        		
		        	//listView.getItems().add(db.getFiles().toString());
		        	File droppedFile = db.getFiles().get(0);
		        	textField.setText("");
		        	textArea.setText("");
		        	info3 = "";
		        	if(droppedFile != null) {
		    			if(droppedFile.getName().endsWith(".txt")) {
		    				//listView.getItems().add(droppedFile.getName());
		    				textField.setText(droppedFile.getName());
		    				fileName = droppedFile;
		    			}
		    			else {
		    				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		    	            errorAlert.setHeaderText("Invalid File!");
		    	            errorAlert.setContentText("You need to input a .txt file");
		    	            errorAlert.showAndWait();
		    			}
		    			
		    			if(droppedFile.getName().endsWith(".txt") && info3.equals("")) {
		    				try (Scanner scanner = new Scanner(droppedFile)) {
		    			        while (scanner.hasNextLine())
		    			        	info3 = info3 + scanner.nextLine() + "\n";
		    			    } catch (FileNotFoundException e3) {
		    			        e3.printStackTrace();
		    			    }
		    			}
		    			else if(droppedFile.getName().endsWith(".txt")) {
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
	        	else {
	        		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	                errorAlert.setHeaderText("Multiple Files Chosen");
	                errorAlert.setContentText("Please choose one File at a time");
	                errorAlert.showAndWait();
	        	}
	        }

	        e.setDropCompleted(result);
	        e.consume();

	    });
	}
	
	public void SaveChanges() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text File", "*.txt"));
		File file = fileChooser.showSaveDialog(new Stage());
		info2 = textArea.getText();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
			bw.write(info2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		saved = true;
	}
}
