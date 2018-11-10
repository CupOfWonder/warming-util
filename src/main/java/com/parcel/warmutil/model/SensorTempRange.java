package com.parcel.warmutil.model;

public class SensorTempRange {
	private int minTemp;
	private int maxTemp;

	public SensorTempRange(int minTemp, int maxTemp) {
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
	}

	public int getMinTemp() {
		return minTemp;
	}

	public int getMaxTemp() {
		return maxTemp;
	}
}
