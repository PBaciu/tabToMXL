package application3;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class SampleController2 implements Initializable {

	
	@FXML
	private StackPane rootPane;
	
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
	 

}
