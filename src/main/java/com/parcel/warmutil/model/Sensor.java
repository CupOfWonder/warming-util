package com.parcel.warmutil.model;

import javax.annotation.Nullable;
import java.util.Deque;
import java.util.LinkedList;
import java.util.OptionalDouble;

public class Sensor {

	private static final int MAX_SAVED_VALUES_COUNT = 3;

	private Deque<Integer> sensorValues = new LinkedList<>();
	private double multiplyKoeff = 1;
	private int correction = 0;
	private int pinNum;

	public int getPinNum() {
		return pinNum;
	}

	public void setPinNum(int pinNum) {
		this.pinNum = pinNum;
	}

	public void setCorrection(int correction) {
		this.correction = correction;
	}

	public void addSensorValue(int voltageOnSensor) {
		sensorValues.add(voltageOnSensor);
		if(sensorValues.size() > MAX_SAVED_VALUES_COUNT) {
			sensorValues.removeFirst();
		}
	}

	/**
	 * Температура на датчике считается как средняя из последних трех показаний
	 *  - это позволяет уменьшить разброс температуры
	 */
	@Nullable
	public Integer getRealTemp() {
		if(sensorValues.size() == 0) {
			return null;
		}

		OptionalDouble doubleTemp = sensorValues.stream().mapToDouble(i -> ((double) i)*multiplyKoeff).average();

		if(doubleTemp.isPresent()) {
			return (int) Math.round(doubleTemp.getAsDouble() + correction);
		}
		return null;
	}

	public void resetValue() {
		sensorValues.clear();
	}

	public void setMultiplyKoeff(double multiplyKoeff) {
		this.multiplyKoeff = multiplyKoeff;
	}
}
