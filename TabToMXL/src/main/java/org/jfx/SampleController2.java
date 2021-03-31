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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.xml.bind.JAXBException;

public class SampleController2 implements Initializable {

	String info1;
	String info2;
	String info3;
	String changeCheck;
	int numerator;
	int denominator;
	int tempoInt;
	String[] timeSign;
	ArrayList<Character> checker = new ArrayList<Character>();
	File file;
//	char[] checker;
	Boolean saved = false;
	File fileName;
	FXMLLoader loader;
	ArrayList<String> info = new ArrayList<>();
	ArrayList<String> measureTimeSign = new ArrayList<>();
	ArrayList<Character> checker2 = new ArrayList<Character>();
	String totalMeasure;
	Boolean added = false;
	ArrayList<Character> checker3 = new ArrayList<Character>();
	
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
	private Button add;
	
	@FXML
	private TextField textField;
	
	@FXML
	private TextField timeSignature;
	
	@FXML
	private TextField tempo;
	
	@FXML
	private TextField measure;
	
	@FXML
	private TextField totalMeasures;
	
	@FXML
	private TextArea textArea;
	
	@FXML
	private Button Additional;
	
	public void initData(String textArea2, String textField2, String fileContent, String timeSign, String tempo2, Boolean save) {
		textArea.setText(textArea2);
		textField.setText(textField2);
		info1 = fileContent;
		timeSignature.setText(timeSign);
		tempo.setText(tempo2);
		saved = save;
		changeCheck = textArea2;
	}
	
	public void ButtonAction(ActionEvent event) {
		FileChooser fc = new FileChooser();
		if(file != null) {
			fc.setInitialDirectory(file.getParentFile());
		}
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text File", "*.txt"));
		File selectedFile = fc.showOpenDialog(null);
		file = selectedFile;
		info1 = "";
		if(selectedFile != null) {
//			System.out.println(file);
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
		}

	}
	
	public void ConvertAction() {
		if(!textArea.getText().equals("")) {
			info2 = textArea.getText();
			checker.add('1');
			checker.add('2');
			checker.add('3');
			checker.add('4');
			checker.add('5');
			checker.add('6');
			checker.add('7');
			checker.add('8');
			checker.add('9');
			checker.add('0');
			checker.add('.');
			int a = 0;
			int b = 0;
			int c = 0;
			if(timeSignature.getText().equals("")) {
				numerator = 4;
				denominator = 4;
				timeSignature.setText("4/4");
			}
			else {
				if(timeSignature.getText().contains("/")) {
					timeSign = timeSignature.getText().split("/");
					if(timeSign.length > 2 || timeSign.length < 2) {
						a = 1;
						Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			            errorAlert.setHeaderText("Incorrect Time Signature specified");
			            errorAlert.setContentText("Please enter the Time Signature in the right format");
			            errorAlert.showAndWait();
					}
					else {
						for(int i = 0; i < timeSign[0].length(); i++) {
							if(!checker.contains(timeSign[0].charAt(i))) {
								a = 1;
								i = timeSign[0].length();
								Alert errorAlert = new Alert(Alert.AlertType.ERROR);
					            errorAlert.setHeaderText("Incorrect Time Signature specified");
					            errorAlert.setContentText("Please enter the Time Signature in the right format");
					            errorAlert.showAndWait();
							}
						}
						for(int j = 0; j < timeSign[1].length(); j++) {
							if(!checker.contains(timeSign[1].charAt(j))) {
								b = 1;
								j = timeSign[1].length();
								Alert errorAlert = new Alert(Alert.AlertType.ERROR);
					            errorAlert.setHeaderText("Incorrect Time Signature specified");
					            errorAlert.setContentText("Please enter the Time Signature in the right format");
					            errorAlert.showAndWait();
							}
						}
						if(a == 0 && b == 0) {
							numerator = (int)Double.parseDouble(timeSign[0]);
							denominator = (int)Double.parseDouble(timeSign[1]);;
						}
					}
				}
				else {
					a = 1;
					Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		            errorAlert.setHeaderText("Incorrect Time Signature specified");
		            errorAlert.setContentText("Please enter the Time Signature in the right format");
		            errorAlert.showAndWait();
				}
			}
			if(tempo.getText().equals("")) {
				tempoInt = 120;
				tempo.setText("120");
			}
			else {
				if(tempo.getText().equals("0")) {
					c = 1;
					Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		            errorAlert.setHeaderText("Incorrect Tempo specified");
		            errorAlert.setContentText("Please enter the Tempo in the right format");
		            errorAlert.showAndWait();
				}
				else {
					for(int i = 0; i < tempo.getText().length(); i++) {
						if(!checker.contains(tempo.getText().charAt(i))) {
							c = 1;
							i = tempo.getText().length();
							Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				            errorAlert.setHeaderText("Incorrect Tempo specified");
				            errorAlert.setContentText("Please enter the Tempo in the right format");
				            errorAlert.showAndWait();
						}
					}
					if(c == 0) {
						tempoInt = (int)Double.parseDouble(tempo.getText());
						System.out.println(tempoInt);
					}
				}
			}
			if(!info2.equals(info1) && textField.getText() != "") {
				if(saved && info2.equals(changeCheck)) {
					if(a == 0 && b == 0 && c == 0) {
						loadNextScene();
					}
				}
				else {
					saved = true;
					changeCheck = info2;
					Alert saveAlert = new Alert(Alert.AlertType.WARNING);
					saveAlert.setHeaderText("You seem to have made changes to your Tablature");
					saveAlert.setContentText("Please click the Save Changes button if you wish to save them to a file. Alternatively, you can wish to not save the changes and continue.");
					saveAlert.showAndWait();
				}
			}
			else {
				if(a == 0 && b == 0 && c == 0) {
					loadNextScene();
				}
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
				- You can Drag and Drop a file in the Text Field given in this screen. You can also Browse for a File from your computer.\n
				- The Files should only be of a .txt format.\n
				- The Uploaded Files will have their content displayed on the Copy/Paste area which can be modified to the Users' preference.\n
				- There are text fields for Time Signature and Tempo which can be inputed if preferred in the correct format.\n
				- The default values set for Time Signature is 4/4 while the default Tempo is 120.\n
				- Hitting the Convert button converts the final variation of the Tablature in the Copy/Paste Text Area into a musicxml file.\n
				- The Save Changes button allows you to save the changes you made in the Tablature to a file of your choosing.""");
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
			controller.initData(textArea.getText(), textField.getText(), info1, timeSignature.getText(), tempo.getText(), saved);
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
		if(textArea.getText().equals("")) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Text Area is empty");
            errorAlert.setContentText("Please input a Tablature to save into a file");
            errorAlert.showAndWait();
		}
		else {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text File", "*.txt"));
			File file = fileChooser.showSaveDialog(new Stage());
			info2 = textArea.getText();
			info1 = info2;
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
				bw.write(info2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(file != null) {
				textField.setText(file.getName());
			}
			saved = true;
		}
	}
	
	public void AddAction() {
		if(textArea.getText().equals("")) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("No Tablature inputted");
            errorAlert.setContentText("Please input a tablature before trying to add a time signature");
            errorAlert.showAndWait();
		}
		else {
			if(totalMeasures.getText().equals("")) {
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	            errorAlert.setHeaderText("Total number of Measures is not specified");
	            errorAlert.setContentText("Please specify the total number of Measures to continue");
	            errorAlert.showAndWait();
			}
			else {
				checker2.add('1');
				checker2.add('2');
				checker2.add('3');
				checker2.add('4');
				checker2.add('5');
				checker2.add('6');
				checker2.add('7');
				checker2.add('8');
				checker2.add('9');
				checker2.add('0');
				totalMeasure = totalMeasures.getText();
				int x = 0;
				int y = 0;
				int z = 0;
				int w = 0;
				if(totalMeasure.equals("0")) {
					x = 1;
					Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		            errorAlert.setHeaderText("Invalid number of Measures specified");
		            errorAlert.setContentText("Please specify the total number of Measures to continue. 0 is not valid");
		            errorAlert.showAndWait();
				}
				else {
					for(int i = 0; i < totalMeasure.length(); i++) {
						if(!checker2.contains(totalMeasure.charAt(i))) {
							x = 1;
							i = totalMeasure.length();
							Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				            errorAlert.setHeaderText("Invalid format of Measures specified");
				            errorAlert.setContentText("Please specify the total number of Measures to continue");
				            errorAlert.showAndWait();
						}
					}
					if(x == 0 && added == false) {
						int nMeasure = Integer.parseInt(totalMeasure);
						for(int j = 0; j < nMeasure + 1; j++) {
							measureTimeSign.add("4/4");
						}
						added = true;
					}
				}
				checker3.add('1');
				checker3.add('2');
				checker3.add('3');
				checker3.add('4');
				checker3.add('5');
				checker3.add('6');
				checker3.add('7');
				checker3.add('8');
				checker3.add('9');
				checker3.add('0');
				checker3.add('.');
				if(x == 0) {
					if(timeSignature.getText().equals("")) {
						timeSignature.setText("4/4");
					}
					else {
						if(timeSignature.getText().contains("/")) {
							timeSign = timeSignature.getText().split("/");
							if(timeSign.length > 2 || timeSign.length < 2) {
								z = 1;
								Alert errorAlert = new Alert(Alert.AlertType.ERROR);
					            errorAlert.setHeaderText("Incorrect Time Signature specified");
					            errorAlert.setContentText("Please enter the Time Signature in the right format");
					            errorAlert.showAndWait();
							}
							else {
								for(int i = 0; i < timeSign[0].length(); i++) {
									if(!checker3.contains(timeSign[0].charAt(i))) {
										z = 1;
										i = timeSign[0].length();
										Alert errorAlert = new Alert(Alert.AlertType.ERROR);
							            errorAlert.setHeaderText("Incorrect Time Signature specified");
							            errorAlert.setContentText("Please enter the Time Signature in the right format");
							            errorAlert.showAndWait();
									}
								}
								for(int j = 0; j < timeSign[1].length(); j++) {
									if(!checker3.contains(timeSign[1].charAt(j))) {
										w = 1;
										j = timeSign[1].length();
										Alert errorAlert = new Alert(Alert.AlertType.ERROR);
							            errorAlert.setHeaderText("Incorrect Time Signature specified");
							            errorAlert.setContentText("Please enter the Time Signature in the right format");
							            errorAlert.showAndWait();
									}
								}
								if(z == 0 && w == 0) {
									numerator = (int)Double.parseDouble(timeSign[0]);
									denominator = (int)Double.parseDouble(timeSign[1]);;
								}
							}
						}
					}
					if(!timeSignature.getText().equals("")) {
						int nMeasure = Integer.parseInt(totalMeasure);
						String currentMeasure = "";
						if(measure.getText().equals("")) {
							for(int k = 1; k < measureTimeSign.size(); k++) {
								if(measureTimeSign.get(k).equals("")) {
									measureTimeSign.set(k, timeSignature.getText());
								}
							}
						}
						else {
							currentMeasure = measure.getText();
							for(int l = 0; l < currentMeasure.length(); l++) {
								if(!checker2.contains(currentMeasure.charAt(l))) {
									y = 1;
									l = totalMeasure.length();
									Alert errorAlert = new Alert(Alert.AlertType.ERROR);
						            errorAlert.setHeaderText("Invalid format of Measure specified");
						            errorAlert.setContentText("Please specify the correct Measure number you wish to add the Time Signature to");
						            errorAlert.showAndWait();
								}
							}
							if(y == 0) {
								int measureNumber = Integer.parseInt(currentMeasure);
								if(nMeasure < measureNumber) {
									Alert errorAlert = new Alert(Alert.AlertType.ERROR);
						            errorAlert.setHeaderText("Measure number specified is incorrect");
						            errorAlert.setContentText("The specified measure number exceeds the total number of measure. Please correct this to continue");
						            errorAlert.showAndWait();
								}
								else {
									measureTimeSign.set(measureNumber, timeSignature.getText());
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void MeasureTyped() {
		if(!measure.getText().equals("")) {
			int measureNumber = Integer.parseInt(measure.getText());
			if(added == true && measureNumber < measureTimeSign.size()) {
				timeSignature.setText(measureTimeSign.get(measureNumber));
			}
			else if(added == true){
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	            errorAlert.setHeaderText("Measure number specified is incorrect");
	            errorAlert.setContentText("The specified measure number exceeds the total number of measure. Please correct this to continue");
	            errorAlert.showAndWait();
	            timeSignature.setText("");
	            measure.setText("");
			}
			else if(added == false) {
				
			}
		}
	}
	
	public void Additional() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("org.jfx/Options.fxml"));
			final Stage popup = new Stage();
			popup.initModality(Modality.APPLICATION_MODAL);
			popup.setTitle("Tranlation Options");
			popup.setScene(new Scene(root, 322, 240));
			
			popup.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void TimeAction() {
		
	}
	
	public void TempoAction() {
		
	}
}
