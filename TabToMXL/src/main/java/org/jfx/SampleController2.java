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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
	private TextField measureFrom;
	
	@FXML
	private TextField measureTo;
	
	@FXML
	private TextArea textArea;
	
	public void initData(String textArea2, String textField2, String fileContent, String timeSign, String tempo2, Boolean save, ArrayList<String> list) {
		textArea.setText(textArea2);
		textField.setText(textField2);
		info1 = fileContent;
//		timeSignature.setText(timeSign);
		tempo.setText(tempo2);
		saved = save;
		changeCheck = textArea2;
		measureTimeSign = list;
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
				- You can specify the total measures you have for your tablature and input the desired Time Signatures for your desired Measure Ranges.\n
				- You have to hit the Add button to initiate and add the Time Signature that you have specified.\n
				- When left blank, the values of all your measures are defaulted to 4/4.\n
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
			controller.initData(textArea.getText(), textField.getText(), info1, timeSignature.getText(), tempo.getText(), saved, measureTimeSign);
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

			ScorePartwise scorePartwise = p.readTab(textArea.getText(), timeSignature.getText(), tempoInt);
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
			if(measureFrom.getText().equals("") || measureTo.getText().equals("")) {
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	            errorAlert.setHeaderText("Measure range is not specified");
	            errorAlert.setContentText("Please specify the Measure range to add a Time Signature to");
	            errorAlert.showAndWait();
			}
			else {
				if(measureTimeSign.isEmpty()) {
					for(int i = 0; i <= 100; i++) {
						measureTimeSign.add("4/4");
					}
				}
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
				int x = 0;
				int y = 0;
				int z = 0;
				int w = 0;
				if(measureFrom.equals("0") || measureFrom.equals("0")) {
					x = 1;
					Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		            errorAlert.setHeaderText("Invalid number of Measure range specified");
		            errorAlert.setContentText("Please specify the correct Measure range to add a Time Signature to");
		            errorAlert.showAndWait();
				}
				else {
					for(int i = 0; i < measureFrom.getText().length(); i++) {
						if(!checker2.contains(measureFrom.getText().charAt(i))) {
							x = 1;
							i = measureFrom.getText().length();
							Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				            errorAlert.setHeaderText("Invalid format of Measures specified");
				            errorAlert.setContentText("Please specify the total number of Measures to continue");
				            errorAlert.showAndWait();
						}
					}
					for(int j = 0; j < measureTo.getText().length(); j++) {
						if(!checker2.contains(measureTo.getText().charAt(j))) {
							x = 1;
							j = measureTo.getText().length();
							Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				            errorAlert.setHeaderText("Invalid format of Measures specified");
				            errorAlert.setContentText("Please specify the total number of Measures to continue");
				            errorAlert.showAndWait();
						}
					}
					if(x == 0) {
						int from = Integer.parseInt(measureFrom.getText());
						int to = Integer.parseInt(measureTo.getText());
						if(from < measureTimeSign.size() && to < measureTimeSign.size() && from <= to) {
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
												int numerator = Integer.parseInt(timeSign[0]);
												int denominator = Integer.parseInt(timeSign[1]);
												int[] timeCheck = {1, 2, 3, 4};
												for(int l = 0; l < timeCheck.length; l++) {
													if(timeCheck[l] == numerator) {
														y = 1;
													}
												}
												if(4 != denominator) {
													y = 0;
												}
												if(y == 1) {
													for(int k = from; k <= to; k++) {
														measureTimeSign.set(k, timeSignature.getText());
													}
													Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION);
													confirmAlert.setHeaderText("Time Signature Added");
													confirmAlert.setContentText("The Time Signature has been added to the specified Measure Range");
													confirmAlert.showAndWait();
												}
												else if(y == 0) {
													Alert errorAlert = new Alert(Alert.AlertType.ERROR);
										            errorAlert.setHeaderText("Incorrect Time Signature specified");
										            errorAlert.setContentText("Please enter the Time Signature in the right format");
										            errorAlert.showAndWait();
												}
											}
										}
									}
									else {
										Alert errorAlert = new Alert(Alert.AlertType.ERROR);
							            errorAlert.setHeaderText("Incorrect Time Signature specified");
							            errorAlert.setContentText("Please enter the Time Signature in the right format");
							            errorAlert.showAndWait();
									}
								}
							}
						}
						else {
							Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				            errorAlert.setHeaderText("Measure range specified is incorrect");
				            errorAlert.setContentText("The specified measure range exceeds the total number of measures. Please correct this to continue");
				            errorAlert.showAndWait();
				            timeSignature.setText("");
				            measureFrom.setText("");
				            measureTo.setText("");
						}
						
					}
				}
			}
		}
	}
	
	public void MeasureTyped() {
		if(measureTimeSign.size() > 0) {
			if(checker2.isEmpty()) {
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
			}
			String currentMeasure = measureFrom.getText();
			String destMeasure = measureTo.getText();
			int e = 0;
			if((!measureFrom.getText().equals("") && !measureTo.getText().equals("")) && (!measureFrom.getText().equals("0") && !measureTo.getText().equals("0"))) {
				for(int i = 0; i < currentMeasure.length(); i++) {
					if(!checker2.contains(currentMeasure.charAt(i))) {
						e = 1;
						i = currentMeasure.length();
						Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			            errorAlert.setHeaderText("Invalid format of Measure (From) specified");
			            errorAlert.setContentText("Please specify the correct Measure range you wish to add the Time Signature to");
			            errorAlert.showAndWait();
			            measureFrom.setText("");
					}
				}
				for(int j = 0; j < destMeasure.length(); j++) {
					if(!checker2.contains(destMeasure.charAt(j))) {
						e = 1;
						j = destMeasure.length();
						Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			            errorAlert.setHeaderText("Invalid format of Measure (To) specified");
			            errorAlert.setContentText("Please specify the correct Measure range you wish to add the Time Signature to");
			            errorAlert.showAndWait();
			            measureTo.setText("");
					}
				}
				if(e == 0) {
					int measureNumberFrom = Integer.parseInt(measureFrom.getText());
					int measureNumberTo = Integer.parseInt(measureTo.getText());
					if(measureNumberFrom < measureTimeSign.size() && measureNumberTo < measureTimeSign.size() && measureNumberFrom <= measureNumberTo && measureNumberFrom > 0 && measureNumberTo > 0) {
						if(measureTimeSign.get(measureNumberFrom).equals(measureTimeSign.get(measureNumberTo))){
							timeSignature.setText(measureTimeSign.get(measureNumberFrom));
						}
						else {
							timeSignature.setText("");
							timeSignature.setPromptText("Different Time Signatures apply");
						}
					}
					else {
						Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			            errorAlert.setHeaderText("Measure range specified is incorrect");
			            errorAlert.setContentText("The specified measure range exceeds the total number of measures. Please correct this to continue");
			            errorAlert.showAndWait();
			            timeSignature.setText("");
			            measureFrom.setText("");
			            measureTo.setText("");
					}
				}
			}
			else if(!measureFrom.getText().equals("") && !measureTo.getText().equals("")){
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
	            errorAlert.setHeaderText("Measure range specified is incorrect");
	            errorAlert.setContentText("The specified measure range is invalid. Please correct this to continue");
	            errorAlert.showAndWait();
	            timeSignature.setText("");
	            measureFrom.setText("");
	            measureTo.setText("");
			}
		}
	}
	
	public void TimeAction() {
		
	}
	
	public void TempoAction() {
		
	}
}
