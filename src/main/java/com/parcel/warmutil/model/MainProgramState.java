package com.parcel.warmutil.model;

import com.parcel.warmutil.model.board.BoardConnector;
import com.parcel.warmutil.model.board.BoardStatus;
import com.parcel.warmutil.model.helpers.ErrorCode;
import com.parcel.warmutil.model.helpers.RelayPosition;
import com.parcel.warmutil.model.helpers.StateChangeHandler;
import com.parcel.warmutil.model.helpers.WorkingStatus;
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

	private BoardStatus boardStatus;
	private WorkingStatus workingStatus = WorkingStatus.NOT_WORKING;
	private volatile boolean requestingNow = false;

	private BoardConnector boardConnector;

	private MainProgramState() {
		addSensorGroup(12, 14, 15);
		addSensorGroup(13, 16, 17);
		addSensorGroup(14, 18, 19);

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

	public boolean saveCurrentOptions() {
		return OptionsSaver.saveOptions(currentOptions);
	}

	public ProgramOptions getCurrentOptions() {
		return currentOptions;
	}

	public void startWorking() {
		if(workingStatus == WorkingStatus.WORKING) {
			return;
		}

		if(!boardConnector.isConnected()) {
			boardStatus = BoardStatus.CONNECTING;
			boardConnector.connectToBoard();
		}

		if(boardConnector.isConnected()) {
			boardStatus = BoardStatus.CONNECTED;
			turnOffAllRelays();

			refreshTimer = new Timer();
			refreshTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					refreshProgramState();
				}
			}, 0, REFRESH_PERIOD);
			refreshWorkingStatus(WorkingStatus.WORKING);
		} else { ;
			boardStatus = BoardStatus.NOT_CONNECTED;
		}

	}

	public void stopWorking() {
		if(workingStatus == WorkingStatus.NOT_WORKING) {
			return;
		}

		if(refreshTimer != null) {
		 	refreshTimer.cancel();
		 	refreshTimer = null;

		 	turnOffAllRelays();
		 	refreshWorkingStatus(WorkingStatus.NOT_WORKING);
		 	resetAllTemperatures();

		 	handleStateChange();
		}
	}

	private void resetAllTemperatures() {
		for(SensorGroup group : sensorGroups) {
			group.resetValues();
		}
	}

	private void turnOffAllRelays() {
		if(boardConnector.isConnected()) {
			sensorGroups.forEach(g -> boardConnector.writeRelayPosition(g.getRelayNumber(), RelayPosition.OFF));
		}
	}

	public synchronized void refreshProgramState() {
		if(boardConnector.isConnected() && workingStatus == WorkingStatus.WORKING) {
			for(SensorGroup group : sensorGroups) {
				reloadSensor(group.getLeftSensor());
				reloadSensor(group.getRightSensor());

				group.recountRelayPosition();

				boardConnector.writeRelayPosition(group.getRelayNumber(), group.getRelayPos());
			}
			handleStateChange();
		}

	}

	private void reloadSensor(Sensor sensor) {
		ErrorCode code = boardConnector.refreshSensorTemp(sensor);

		if(code != ErrorCode.NO_ERROR) {
			logger.log(Level.SEVERE, "Received error "+code+" on pin "+sensor.getPinNum());
		}
	}

	public void handleProgramClose() {
		turnOffAllRelays();
	}

	public BoardStatus getBoardStatus() {
		return boardStatus;
	}

	public void setBoardStatus(BoardStatus boardStatus) {
		this.boardStatus = boardStatus;
	}

	private void refreshWorkingStatus(WorkingStatus status) {
		this.workingStatus = status;
		handleStateChange();
	}

	public WorkingStatus getWorkingStatus() {
		return workingStatus;
	}
}
