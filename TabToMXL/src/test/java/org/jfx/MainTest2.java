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
class MainTest2 {

	@Start
	public void start(Stage primaryStage)  throws Exception{
		try {
			Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("org.jfx/Sample3.fxml")));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getClassLoader().getResource("org.jfx/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


@Test
void chooseFile_contain_button_with_text(FxRobot robot) {
    FxAssert.verifyThat("#backButton", LabeledMatchers.hasText("Back"));
}

@Test
void convert_contain_button_with_text(FxRobot robot) {
    FxAssert.verifyThat("#saveButton", LabeledMatchers.hasText("Save"));
}

@Test
void help_contain_button_with_text(FxRobot robot) {
    FxAssert.verifyThat("#help", LabeledMatchers.hasText("Help"));
}

@Test
void saveChanges_contain_button_with_text(FxRobot robot) {
    FxAssert.verifyThat("#newConversion", LabeledMatchers.hasText("New Conversion"));
}}