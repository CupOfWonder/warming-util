package com.parcel.warmutil.client.widgets;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class MessageShowManager {

    private static final int APPEAR_DURATION = 400;
    private static final int SHOW_DURATION = 3000;
    private static final int DISAPPEAR_DURATION = 200;

    public void showSuccess(Label label, String message) {
        label.setTextFill(Color.GREEN);
        showMessage(label, message);
    }

    public void showError(Label label, String message) {
        label.setTextFill(Color.FIREBRICK);
        showMessage(label, message);
    }

    private void showMessage(Label label, String message) {
        Platform.runLater(() -> {
			label.setText(message);

			FadeTransition transition = new FadeTransition();
			transition.setNode(label);
			transition.setDuration(Duration.millis(APPEAR_DURATION));
			transition.setFromValue(0);
			transition.setToValue(1);

			transition.play();

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					hideMessage(label);
				}
			}, SHOW_DURATION);
		});
    }

    private void hideMessage(Label label) {
        FadeTransition transition = new FadeTransition();
        transition.setNode(label);
        transition.setDuration(Duration.millis(DISAPPEAR_DURATION));
        transition.setFromValue(1);
        transition.setToValue(0);

        transition.play();
    }
}
