package com.parcel.warmutil.client.helpers;

import com.parcel.warmutil.model.helpers.WarmingState;
import com.parcel.warmutil.model.helpers.WorkingStatus;

/**
 * Created by nemo on 10.11.18.
 */
public class StringHelper {

	private static final String EMPTY_VALUE = "Не определено";

	public static String temperatureString(Integer degrees) {
		if(degrees == null) {
			return EMPTY_VALUE;
		} else {
			return degrees+ " °C";
		}
	}

	public static String textForWarmingState(WarmingState state) {
		if(state == null) {
			return EMPTY_VALUE;
		}
		switch(state) {
			case COOLING:
				return "Охлаждается";
			case SUSTAIN:
				return "Поддерживает температуру";
			case WARMING:
				return "Нагревается";
			default:
				throw new IllegalArgumentException("Warming state "+state+" not supported");
		}
	}

	public static String textForWorkingStatus(WorkingStatus status) {
		if(status == null) {
			return "";
		}
		switch (status) {
			case WORKING:
				return "Установка запущена";
			case STARTING:
				return "Запуск установки...";
			case NOT_WORKING:
				return "Установка не запущена";
			case STOPPING:
				return "Остановка...";
			default:
				throw new IllegalArgumentException("Working status "+status+ " not supported");
		}
	}
}
