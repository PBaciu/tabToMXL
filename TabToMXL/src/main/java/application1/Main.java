package application1;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class Main extends Application {
    Label lb;
    Button btn;
	public void start(Stage primaryStage) throws Exception {
		
		lb =  new Label("Welcome to Tab2Xml");
		btn = new Button("Click to Proceed");
		lb.getStyleClass().add("my_customLabel");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				lb.setText("Welcome");
			}
		});
		
		VBox root = new VBox();
		root.getChildren().addAll(lb,btn);
		
		Scene scene= new Scene(root,300,200);
		primaryStage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
