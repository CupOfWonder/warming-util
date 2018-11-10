package com.parcel.warmutil.model;

import com.parcel.warmutil.model.helpers.ErrorCode;
import com.parcel.warmutil.model.helpers.StateChangeHandler;
import com.parcel.warmutil.model.options.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.parcel.warmutil.model.options.DefaultOptionsCreator.createDefaultOptions;

public class MainProgramState {

	private static Logger logger = Logger.getLogger("MainProgramState");

	private static MainProgramState instance;

	private ProgramOptions currentOptions;

	private List<StateChangeHandler> stateChangeHandlers = new ArrayList<>();

	private int groupCount = 0;
	private List<SensorGroup> sensorGroups = new ArrayList<>();

	private Timer refreshTimer;
	private static final int REFRESH_PERIOD = 1000;

	private boolean programStated = false;
	private volatile boolean requestingNow = false;

	private BoardConnector boardConnector;

	private MainProgramState() {
		addSensorGroup(12, 1, 2);
		addSensorGroup(13, 3, 4);
		addSensorGroup(14, 5, 6);

		tryLoadOptions();
		boardConnector = new BoardConnector(currentOptions.getBoardName(), currentOptions.getMultiplyKoef());
	}

	private void tryLoadOptions() {
		ProgramOptions options = OptionsLoader.loadOptions();
		if(options != null) {
			applyOptions(options);
		} else {
			currentOptions = createDefaultOptions(sensorGroups);
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

	public void startWorking() {
		if(!boardConnector.isConnected()) {
			boardConnector.connectToBoard();
		}

		if(boardConnector.isConnected()) {
			refreshTimer = new Timer();
			refreshTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					refreshProgramState();
				}
			}, 0, REFRESH_PERIOD);
		}
	}

	public void stopWorking() {
		refreshTimer.cancel();
	}

	public synchronized void refreshProgramState() {
		for(SensorGroup group : sensorGroups) {
			reloadSensor(group.getLeftSensor());
			reloadSensor(group.getRightSensor());

			boardConnector.writeRelayPosition(group.getRelayNumber(), group.getRelayPos());
		}
		handleStateChange();
	}

	private void reloadSensor(Sensor sensor) {
		ErrorCode code = boardConnector.refreshSensorTemp(sensor);

		if(code != ErrorCode.NO_ERROR) {
			logger.log(Level.SEVERE, "Received error "+code+" on pin "+sensor.getPinNum());
		}
	}
}
