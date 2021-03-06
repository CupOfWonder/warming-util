package com.parcel.warmutil.model.options;

public class TempRangeOptions {
	private int groupNumber;
	private int minTemp;
	private int maxTemp;

	public TempRangeOptions() {}

	public TempRangeOptions(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public TempRangeOptions(int groupNumber, int minTemp, int maxTemp) {
		this.groupNumber = groupNumber;
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public int getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(int minTemp) {
		this.minTemp = minTemp;
		if(maxTemp < minTemp) {
			maxTemp = minTemp;
		}
	}

	public int getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(int maxTemp) {
		this.maxTemp = maxTemp;
		if(maxTemp < minTemp) {
			minTemp = maxTemp;
		}
	}

	public TempRangeOptions copy() {
		return new TempRangeOptions(groupNumber, minTemp, maxTemp);
	}
}
