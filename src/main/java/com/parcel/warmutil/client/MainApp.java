package com.parcel.warmutil.client;/**
 * Created by nemo on 10.11.18.
 */

import com.parcel.warmutil.model.MainProgramState;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainApp extends Application {

	MainProgramState programState = MainProgramState.getInstance();

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

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});

		initShutdownHooks();

		stage.setTitle("Управление нагревом");
		stage.setScene(scene);
		stage.show();
	}

	private void initShutdownHooks() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				programState.handleProgramClose();
				System.out.println("Program closed");
			}
		});
	}

}
