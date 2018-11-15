package com.parcel.warmutil.model.options;

public class MultiplyKoeffOptions {
	private int groupNumber;
	private double leftKoeff = 1000;
	private double rightKoeff = 1000;

	public MultiplyKoeffOptions() {}

	public MultiplyKoeffOptions(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public double getLeftKoeff() {
		return leftKoeff;
	}

	public void setLeftKoeff(double leftKoeff) {
		this.leftKoeff = leftKoeff;
	}

	public double getRightKoeff() {
		return rightKoeff;
	}

	public void setRightKoeff(double rightKoeff) {
		this.rightKoeff = rightKoeff;
	}
}
