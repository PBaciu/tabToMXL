package org.jfx;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;

import java.util.Objects;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage)  throws Exception{
		try {
			Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("org.jfx/Sample2.fxml")));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getClassLoader().getResource("org.jfx/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			primaryStage.setMinHeight(800); //setting minimum height
			primaryStage.setMinWidth(900);
			
			
			primaryStage.setTitle("TAB2MXL");
			
			Image icon = new Image("org.jfx/icon.png");
			primaryStage.getIcons().add(icon);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
