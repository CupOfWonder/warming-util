package com.parcel.warmutil.model.options;

public class CalibrationOptions {
	private int groupNumber;
	private int leftCalibration;
	private int rightCalibration;

	public CalibrationOptions(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public int getLeftCalibration() {
		return leftCalibration;
	}

	public void setLeftCalibration(int leftCalibration) {
		this.leftCalibration = leftCalibration;
	}

	public int getRightCalibration() {
		return rightCalibration;
	}

	public void setRightCalibration(int rightCalibration) {
		this.rightCalibration = rightCalibration;
	}
}
