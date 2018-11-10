package com.parcel.warmutil.model;

import com.parcel.warmutil.model.helpers.StateChangeHandler;
import com.parcel.warmutil.model.options.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainProgramState {

	private static MainProgramState instance;

	private ProgramOptions currentOptions;

	private List<StateChangeHandler> stateChangeHandlers = new ArrayList<>();

	private int groupCount = 0;
	private List<SensorGroup> sensorGroups = new ArrayList<>();

	private MainProgramState() {
		addSensorGroup(12, 1, 2);
		addSensorGroup(13, 3, 4);
		addSensorGroup(14, 5, 6);

		tryLoadOptions();
	}

	private void tryLoadOptions() {
		ProgramOptions options = OptionsLoader.loadOptions();
		if(options != null) {
			applyOptions(options);
		} else {
			createDefaultOptions();
		}
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

	public void applyOptions(ProgramOptions options) {
		this.currentOptions = options;
		applyOptionsToModel();
	}

	private void applyOptionsToModel() {
		Map<Integer, SensorGroup> groupMap = getGroupMap();

		for(TempRangeOptions opts : currentOptions.getTempRangeOptions()) {
			SensorGroup group = groupMap.get(opts.getGroupNumber());

			if(group != null) {
				group.setTempRange(opts.getMinTemp(), opts.getMaxTemp());
			}
		}

		for(CalibrationOptions opts : currentOptions.getCalibrationOptions()) {
			SensorGroup group = groupMap.get(opts.getGroupNumber());

			if(group != null) {
				group.setSensorCorrections(opts.getLeftCalibration(), opts.getRightCalibration());
			}
		}
	}

	private Map<Integer, SensorGroup> getGroupMap() {
		Map<Integer, SensorGroup> groupMap = new HashMap<>();
		sensorGroups.forEach(g -> groupMap.put(g.getGroupNumber(), g));

		return groupMap;
	}

	public void addStateChangeHandler(StateChangeHandler handler) {
		stateChangeHandlers.add(handler);
	}

	public void handleStateChange() {
		stateChangeHandlers.forEach(h -> h.onStateChange(this));
	}

	public void createDefaultOptions() {
		ProgramOptions options = new ProgramOptions();

		List<TempRangeOptions> tempOptions = new ArrayList<>();
		List<CalibrationOptions> calibrationOptions = new ArrayList<>();
		for(SensorGroup group : sensorGroups) {
			TempRangeOptions to = new TempRangeOptions(group.getGroupNumber());
			tempOptions.add(to);

			CalibrationOptions co = new CalibrationOptions(group.getGroupNumber());
			calibrationOptions.add(co);
		}
		options.setCalibrationOptions(calibrationOptions);
		options.setTempRangeOptions(tempOptions);

		currentOptions = options;
	}

	public void applyTempRangeToCurrentOptions(List<TempRangeOptions> tempRangeOptions) {
		currentOptions.setTempRangeOptions(tempRangeOptions);
		applyOptionsToModel();
	}

	public void applyNewCalibrationToOptions(List<CalibrationOptions> calibrationOptions) {
		currentOptions.setCalibrationOptions(calibrationOptions);
		applyOptionsToModel();
	}

	public void saveCurrentOptions() {
		OptionsSaver.saveOptions(currentOptions);
	}

	public ProgramOptions getCurrentOptions() {
		return currentOptions;
	}
}
