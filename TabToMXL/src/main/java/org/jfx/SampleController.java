package org.jfx;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SampleController implements Initializable{
	@FXML
	private StackPane rootPane;
	@FXML
	private void handleButtonClick(ActionEvent event)
	{
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
				loadNextScene();
				
			}
		});
		fadeTransition.play();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
			
	}
	private void loadNextScene() {
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
