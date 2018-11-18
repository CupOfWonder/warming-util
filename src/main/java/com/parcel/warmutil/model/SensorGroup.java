package com.parcel.warmutil.model;

import com.parcel.warmutil.model.helpers.RelayPosition;
import com.parcel.warmutil.model.helpers.SensorTempRange;
import com.parcel.warmutil.model.helpers.WarmingState;

public class SensorGroup {
	private Sensor leftSensor = new Sensor();
	private Sensor rightSensor = new Sensor();
	private SensorTempRange tempRange;

	private WarmingState state;
	private RelayPosition relayPos = RelayPosition.OFF;

	private int groupNumber;
	private int relayNumber;

	public SensorGroup(int relayNumber, int leftSensorPin, int rightSensorPin) {
		this.relayNumber = relayNumber;
		leftSensor.setPinNum(leftSensorPin);
		rightSensor.setPinNum(rightSensorPin);
	}

	public void recountRelayPosition() {
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

	public void setSensorCorrections(int leftCorrection, int rightCorrection) {
		leftSensor.setCorrection(leftCorrection);
		rightSensor.setCorrection(rightCorrection);
	}

	public void setTempRange(int minTemp, int maxTemp) {
		this.tempRange = new SensorTempRange(minTemp, maxTemp);
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

	public Sensor getLeftSensor() {
		return leftSensor;
	}

	public void setLeftSensor(Sensor leftSensor) {
		this.leftSensor = leftSensor;
	}

	public Sensor getRightSensor() {
		return rightSensor;
	}

	public void setRightSensor(Sensor rightSensor) {
		this.rightSensor = rightSensor;
	}

	public void resetValues() {
		leftSensor.resetValue();
		rightSensor.resetValue();
		state = null;
	}

	public void setSensorResistances(double leftMultiplyKoeff, double rightMultiplyKoeff) {
		leftSensor.setResistance(leftMultiplyKoeff);
		rightSensor.setResistance(rightMultiplyKoeff);
	}
}
