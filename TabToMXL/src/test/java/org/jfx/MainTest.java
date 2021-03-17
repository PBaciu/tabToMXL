package org.jfx;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class MainTest {

	@Start
	public void start(Stage primaryStage)  throws Exception{
		try {
			Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("org.jfx/Sample2.fxml")));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getClassLoader().getResource("org.jfx/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			primaryStage.setTitle("TAB2MXL");
			
			Image icon = new Image("org.jfx/icon.png");
			primaryStage.getIcons().add(icon);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
    void chooseFile_contain_button_with_text(FxRobot robot) {
        FxAssert.verifyThat("#chooseFile", LabeledMatchers.hasText("Browse"));
    }
	
	@Test
    void convert_contain_button_with_text(FxRobot robot) {
        FxAssert.verifyThat("#convert", LabeledMatchers.hasText("Convert"));
    }
	
	@Test
    void help_contain_button_with_text(FxRobot robot) {
        FxAssert.verifyThat("#help", LabeledMatchers.hasText("Help"));
    }
	
	@Test
    void saveChanges_contain_button_with_text(FxRobot robot) {
        FxAssert.verifyThat("#saveChanges", LabeledMatchers.hasText("Save Changes"));
    }
	
//	@Test
//	void when_chooseFile_button_is_clicked(FxRobot robot) {
//		robot.clickOn("#chooseFile");
//		//FxAssert.verifyThat("#onlyButton", LabeledMatchers.hasText("clicked!"));
//	}
//	
//	@Test
//	void checkn_if_TextArea_is_empty(FxRobot robot) {
//		robot.clickOn("#chooseFile");
//		//FxAssert.verifyThat("#onlyButton", LabeledMatchers.hasText("clicked!"));
//	}

}
