package com.parcel.warmutil.client.helpers;

import com.parcel.warmutil.model.helpers.WarmingState;
import javafx.scene.paint.Color;

/**
 * Created by nemo on 10.11.18.
 */
public class StyleHelper {
	public static Color colorForWarmingState(WarmingState state) {
		if(state == null) {
			return Color.BLACK;
		}

		switch (state) {
			case COOLING:
				return Color.BLUE;
			case WARMING:
				return Color.FIREBRICK;
			case SUSTAIN:
				return Color.GREEN;
			default:
				throw new IllegalArgumentException("Warming state "+state+" not supported");
		}
	}
}
