package application;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


public class Main extends Application {
	public static void main(String[] args){
	    launch(args);
	}

	@Override
	public void start(Stage primaryStage){

	    Text droppedFile = new Text("File:");

	    TextField fieldOfFile = new TextField();
	    fieldOfFile.setPromptText("Drop file here");
	    fieldOfFile.setEditable(false);
	    fieldOfFile.setMinWidth(250);
	    fieldOfFile.setOnDragOver((e) -> {
	        if (e.getGestureSource() != fieldOfFile && e.getDragboard().hasFiles()) {
	            e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
	        }
	        e.consume();
	    });
	    fieldOfFile.setOnDragDropped(e -> {

	        Dragboard db = e.getDragboard();
	        boolean result = false;
	        if (db.hasFiles()){

	            fieldOfFile.setText(db.getFiles().toString());
	            result = true;

	        }

	        e.setDropCompleted(result);
	        e.consume();

	    });

	    Button continueButton = new Button("Continue");
	    Button clearButton = new Button("Clear");
	    //continueButton.setOnAction(e -> buttonClicked(droppedFile, fieldOfFile, continueButton, clearButton, hBox));
	    clearButton.setOnMouseClicked(e -> fieldOfFile.setText(""));

	    HBox hBox = new HBox();
	    hBox.setAlignment(Pos.CENTER_LEFT);
	    hBox.getChildren().addAll(droppedFile, fieldOfFile, continueButton, clearButton);
	    continueButton.setOnAction(e -> buttonClicked(droppedFile, fieldOfFile, continueButton, clearButton, hBox));

	    HBox.setMargin(droppedFile, new Insets(20));
	    HBox.setMargin(fieldOfFile, new Insets(20, 20, 20, 20));
	    HBox.setMargin(clearButton, new Insets(20, 20, 20, 20));
	    HBox.setMargin(continueButton, new Insets(20, 20, 20, 20));

	    primaryStage.setScene(new Scene(hBox));
	    primaryStage.sizeToScene();
	    primaryStage.centerOnScreen();
	    primaryStage.show();

	}
	
	private void buttonClicked(Text droppedFile, TextField fieldOfFile, Button continueButton, Button clearButton, HBox hBox) {
		Button downloadButton = new Button("Download");
		hBox.setAlignment(Pos.CENTER_LEFT);
	    hBox.getChildren().addAll(downloadButton);
	    HBox.setMargin(downloadButton, new Insets(20, 20, 20, 20));
	}
	
}
