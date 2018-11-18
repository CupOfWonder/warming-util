package com.parcel.warmutil.model.options;

public class ResistanceOptions {
	private int groupNumber;
	private double leftResistance = 1000;
	private double rightResistance = 1000;

	public ResistanceOptions() {}

	public ResistanceOptions(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public double getLeftResistance() {
		return leftResistance;
	}

	public void setLeftResistance(double leftResistance) {
		this.leftResistance = leftResistance;
	}

	public double getRightResistance() {
		return rightResistance;
	}

	public void setRightResistance(double rightResistance) {
		this.rightResistance = rightResistance;
	}
}
