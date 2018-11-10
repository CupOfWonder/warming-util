package com.parcel.warmutil.client;/**
 * Created by nemo on 10.11.18.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MainApp.fxml"));

		Scene scene = new Scene(root, 826, 450);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("MainApp.css").toExternalForm());

		stage.setResizable(false);
		stage.sizeToScene();

		stage.setTitle("Управление нагревом");
		stage.setScene(scene);
		stage.show();
	}

}
