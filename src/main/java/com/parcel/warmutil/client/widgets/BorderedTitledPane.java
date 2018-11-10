package com.parcel.warmutil.client.widgets;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Created by nemo on 10.11.18.
 */
class BorderedTitledPane extends StackPane {
	BorderedTitledPane(String titleString, Node content) {
		Label title = new Label(" " + titleString + " ");
		title.getStyleClass().add("bordered-titled-title");
		StackPane.setAlignment(title, Pos.TOP_CENTER);

		StackPane contentPane = new StackPane();
		content.getStyleClass().add("bordered-titled-content");
		contentPane.getChildren().add(content);

		getStyleClass().add("bordered-titled-border");
		getChildren().addAll(title, contentPane);
	}
}