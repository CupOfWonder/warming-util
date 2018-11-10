package com.parcel.warmutil.client;/**
 * Created by nemo on 10.11.18.
 */

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MainApp.fxml"));

		Scene scene = new Scene(root, 825, 450);

		stage.setTitle("Управление нагревом");
		stage.setScene(scene);
		stage.show();
	}

}
