package com.parcel.warmutil.model;

import com.parcel.warmutil.model.helpers.RelayPosition;
import com.parcel.warmutil.model.helpers.SensorTempRange;
import com.parcel.warmutil.model.helpers.WarmingState;

public class SensorGroup {
	private Sensor leftSensor = new Sensor();
	private Sensor rightSensor = new Sensor();
	private SensorTempRange tempRange;

	private WarmingState state;
	private RelayPosition relayPos;

	private int groupNumber;
	private int relayNumber;
	private int leftSensorPin;
	private int rightSensorPin;

	public SensorGroup(int relayNumber, int leftSensorPin, int rightSensorPin) {
		this.relayNumber = relayNumber;
		this.leftSensorPin = leftSensorPin;
		this.rightSensorPin = rightSensorPin;
	}

	private void recountRelayPosition() {
		int groupTemp = countGroupTemp();

		if(groupTemp < tempRange.getMinTemp()) {
			switchToWarmingState();
		} else if(groupTemp >= tempRange.getMinTemp() && groupTemp <= tempRange.getMaxTemp()) {
			switchToSustainState();
		} else if (groupTemp > tempRange.getMaxTemp()) {
			switchToCoolingState();
		}
	}

	private void switchToWarmingState() {
		state = WarmingState.WARMING;
		relayPos = RelayPosition.ON;
	}

	private void switchToSustainState() {
		state = WarmingState.SUSTAIN;
	}

	private void switchToCoolingState() {
		state = WarmingState.COOLING;
		relayPos = RelayPosition.OFF;
	}

	private int countGroupTemp() {
		return (int) Math.round(((double) leftSensor.getRealTemp() + (double) rightSensor.getRealTemp())/2);
	}

	public void setSensorTempValues(int leftTemp, int rightTemp) {
		leftSensor.setTempOnSensor(leftTemp);
		rightSensor.setTempOnSensor(rightTemp);

		recountRelayPosition();
	}

	public void setSensorCorrections(int leftCorrection, int rightCorrection) {
		leftSensor.setCorrection(leftCorrection);
		rightSensor.setCorrection(rightCorrection);
	}

	public void setTempRange(SensorTempRange tempRange) {
		this.tempRange = tempRange;
	}

	public WarmingState getState() {
		return state;
	}

	public RelayPosition getRelayPos() {
		return relayPos;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public Integer getLeftTemp() {
		return leftSensor.getRealTemp();
	}

	public Integer getRightTemp() {
		return rightSensor.getRealTemp();
	}

	public int getRelayNumber() {
		return relayNumber;
	}

	public int getLeftSensorPin() {
		return leftSensorPin;
	}

	public int getRightSensorPin() {
		return rightSensorPin;
	}
}
