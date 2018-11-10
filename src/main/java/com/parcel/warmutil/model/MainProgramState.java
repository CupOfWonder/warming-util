package com.parcel.warmutil.model;

import java.util.ArrayList;
import java.util.List;

public class MainProgramState {

	private static MainProgramState instance;

	private int groupCount = 0;
	private List<SensorGroup> sensorGroups = new ArrayList<>();

	private MainProgramState() {
		addSensorGroup(12, 1, 2);
		addSensorGroup(13, 3, 4);
		addSensorGroup(14, 5, 6);
	}

	private void addSensorGroup(int relayNumber, int leftSensorPin, int rightSensorPin) {
		SensorGroup group = new SensorGroup(relayNumber, leftSensorPin, rightSensorPin);
		group.setGroupNumber(++groupCount);
		sensorGroups.add(group);
	}

	public static MainProgramState getInstance() {
		if(instance == null) {
			instance = new MainProgramState();
		}
		return instance;
	}

	public List<SensorGroup> getAllGroups() {
		return sensorGroups;
	}
}
