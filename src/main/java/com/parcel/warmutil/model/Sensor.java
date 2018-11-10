package com.parcel.warmutil.model;

public class Sensor {
	private int tempOnSensor;
	private int correction = 0;

	public void setTempOnSensor(int tempOnSensor) {
		this.tempOnSensor = tempOnSensor;
	}

	public void setCorrection(int correction) {
		this.correction = correction;
	}

	public int getRealTemp() {
		return tempOnSensor + correction;
	}
}
