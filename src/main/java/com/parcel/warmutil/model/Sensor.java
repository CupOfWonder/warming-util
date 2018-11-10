package com.parcel.warmutil.model;

import javax.annotation.Nullable;

public class Sensor {
	private Integer tempOnSensor;
	private int correction = 0;
	private int pinNum;

	public int getPinNum() {
		return pinNum;
	}

	public void setPinNum(int pinNum) {
		this.pinNum = pinNum;
	}

	public void setTempOnSensor(int tempOnSensor) {
		this.tempOnSensor = tempOnSensor;
	}

	public void setCorrection(int correction) {
		this.correction = correction;
	}

	@Nullable
	public Integer getRealTemp() {
		if(tempOnSensor != null) {
			return tempOnSensor + correction;
		} else {
			return null;
		}
	}
}
