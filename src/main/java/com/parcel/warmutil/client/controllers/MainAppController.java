package com.parcel.warmutil.client.controllers;

import com.parcel.warmutil.client.helpers.StringHelper;
import com.parcel.warmutil.client.helpers.StyleHelper;
import com.parcel.warmutil.client.widgets.MessageShowManager;
import com.parcel.warmutil.client.widgets.table.CalibrationEditCell;
import com.parcel.warmutil.client.widgets.table.TemperatureEditCell;
import com.parcel.warmutil.model.MainProgramState;
import com.parcel.warmutil.model.SensorGroup;
import com.parcel.warmutil.model.board.BoardStatus;
import com.parcel.warmutil.model.events.TableEditFinisher;
import com.parcel.warmutil.model.helpers.StateChangeHandler;
import com.parcel.warmutil.model.helpers.WarmingState;
import com.parcel.warmutil.model.helpers.WorkingStatus;
import com.parcel.warmutil.model.options.CalibrationOptions;
import com.parcel.warmutil.model.options.TempRangeOptions;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.stream.Collectors;

import static com.parcel.warmutil.client.helpers.StringHelper.temperatureString;

public class MainAppController {

	private TableEditFinisher editFinisher = TableEditFinisher.getInstance();

	private Thread startingThread;

	@FXML
	public TableView<SensorGroup> monitoringTable;

	@FXML
	public TableColumn<SensorGroup, String> monitoringGroupNum, monitoringLeftTemp,
			monitoringRightTemp;

	@FXML
	public TableColumn<SensorGroup, WarmingState> monitoringGroupState;

	@FXML
	public TableView<TempRangeOptions> tempOptionsTable;

	@FXML
	public TableColumn<TempRangeOptions, String> tempGroupNum;

	@FXML
	public TableColumn<TempRangeOptions, Integer> tempMin, tempMax;

	@FXML
	public TableView<CalibrationOptions> calibrationTable;

	@FXML
	public TableColumn<CalibrationOptions, String> calibrationGroupNum;

	@FXML
	public TableColumn<CalibrationOptions, Integer> calibrationLeftSensor, calibrationRightSensor;

	@FXML
	private Button startWarming, stopWarming;

	@FXML
	private Label monitoringMessage, tempMessage, calibrationMessage, workingStatusLabel;

	private MainProgramState programState = MainProgramState.getInstance();

	private MessageShowManager messageShowManager = new MessageShowManager();

	@FXML
	public void initialize() {
		initMonitoringTable();
		initTempRangeTable();
		initCalibrationTable();
	}


	private void initMonitoringTable() {
		monitoringTable.setSelectionModel(null);
		monitoringTable.setItems(sensorObservableList());

		monitoringGroupNum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
				Long.toString(cellData.getValue().getGroupNumber()))
		);
		monitoringLeftTemp.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<String>(
				temperatureString(cellData.getValue().getLeftTemp())
		));
		monitoringRightTemp.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<String>(
				temperatureString(cellData.getValue().getRightTemp())
		));
		monitoringGroupState.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<WarmingState>(
				cellData.getValue().getState()
		));
		monitoringGroupState.setCellFactory(param -> new TableCell<SensorGroup, WarmingState>() {
			@Override
			protected void updateItem(WarmingState state, boolean empty) {
				if(!empty) {
					setText(StringHelper.textForWarmingState(state));
					setTextFill(StyleHelper.colorForWarmingState(state));
				}
			}
		});

		programState.addStateChangeHandler(new StateChangeHandler() {
			@Override
			public void onStateChange(MainProgramState state) {
				monitoringTable.refresh();

				WorkingStatus status = state.getWorkingStatus();
				workingStatusLabel.setText(StringHelper.textForWorkingStatus(status));
				workingStatusLabel.setTextFill(StyleHelper.colorForWorkingStatus(status));

				switch (status) {
					case NOT_WORKING:
						stopWarming.setDisable(true);
						startWarming.setDisable(false);
						break;
					case STARTING:
						stopWarming.setDisable(true);
						startWarming.setDisable(true);
						break;
					case WORKING:
						stopWarming.setDisable(false);
						startWarming.setDisable(true);
						break;
				}
			}
		});
	}

	private void initTempRangeTable() {
		tempOptionsTable.getSelectionModel().setCellSelectionEnabled(true);
		tempOptionsTable.setItems(tempOptionsObservableList());

		//Колонка "№ группы"
		tempGroupNum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
				Long.toString(cellData.getValue().getGroupNumber()))
		);

		//Колонка "Минимальная темп-ра группы"
		tempMin.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
				cellData.getValue().getMinTemp())
		);
		tempMin.setCellFactory(cellData -> new TemperatureEditCell());
		tempMin.setOnEditCommit(event -> {
			event.getRowValue().setMinTemp(event.getNewValue());
			tempOptionsTable.refresh();
		});

		//Колонка "Максимальная темп-ра группы"
		tempMax.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
				cellData.getValue().getMaxTemp())
		);
		tempMax.setCellFactory(cellData -> new TemperatureEditCell());
		tempMax.setOnEditCommit(event -> {
			event.getRowValue().setMaxTemp(event.getNewValue());
			tempOptionsTable.refresh();
		});
	}

	//Таблица "Калибровка датчиков"
	private void initCalibrationTable() {
		calibrationTable.getSelectionModel().setCellSelectionEnabled(true);
		calibrationTable.setItems(calibrationOptionsObservableList());

		//Колонка "№ группы"
		calibrationGroupNum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
				Long.toString(cellData.getValue().getGroupNumber()))
		);

		//Колонка "Поправка к левому датчику"
		calibrationLeftSensor.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Integer>(
				cellData.getValue().getLeftCalibration()
		));
		calibrationLeftSensor.setOnEditCommit(event -> {
			event.getRowValue().setLeftCalibration(event.getNewValue());
			calibrationTable.refresh();
		});
		calibrationLeftSensor.setCellFactory(cellData -> new CalibrationEditCell());

		//Колонка "Поправка к правому датчику
		calibrationRightSensor.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Integer>(
				cellData.getValue().getRightCalibration()
		));
		calibrationRightSensor.setOnEditCommit(event -> {
			event.getRowValue().setRightCalibration(event.getNewValue());
			calibrationTable.refresh();
		});
		calibrationRightSensor.setCellFactory(cellData -> new CalibrationEditCell());
	}

	private ObservableList<SensorGroup> sensorObservableList() {
		List<SensorGroup> groups = programState.getAllGroups();
		return FXCollections.observableList(groups);
	}

	private ObservableList<TempRangeOptions> tempOptionsObservableList() {
		List<TempRangeOptions> currentTempOptions = programState.getCurrentOptions().getTempRangeOptions();

		return FXCollections.observableList(cloneTempOptions(currentTempOptions));
	}

	private ObservableList<CalibrationOptions> calibrationOptionsObservableList() {
		List<CalibrationOptions> currentCalibrationOptions = programState.getCurrentOptions().getCalibrationOptions();
		return FXCollections.observableList(cloneCalibrationOptions(currentCalibrationOptions));
	}

	public void onTempOptionsConfirmed(MouseEvent mouseEvent) {
		editFinisher.finishEdit();
		programState.applyTempRangeToCurrentOptions(cloneTempOptions(tempOptionsTable.getItems()));
		if(programState.saveCurrentOptions()) {
			showOptionsSaved(tempMessage);
		} else {
			showOptionsSaveError(tempMessage);
		}


	}

	private List<TempRangeOptions> cloneTempOptions(List<TempRangeOptions> options) {
		return options.stream().map(TempRangeOptions::copy).collect(Collectors.toList());
	}

	private List<CalibrationOptions> cloneCalibrationOptions(List<CalibrationOptions> options) {
		return options.stream().map(CalibrationOptions::copy).collect(Collectors.toList());
	}

	public void onCalibrationOptionsConfirmed(MouseEvent mouseEvent) {
		editFinisher.finishEdit();
		programState.applyNewCalibrationToOptions(cloneCalibrationOptions(calibrationTable.getItems()));
		if(programState.saveCurrentOptions()) {
			showOptionsSaved(calibrationMessage);
		} else {
			showOptionsSaveError(calibrationMessage);
		}

	}

	private void showOptionsSaveError(Label calibrationMessage) {
		messageShowManager.showError(calibrationMessage, "При сохранении опций возникла ошибка");
	}

	private void showOptionsSaved(Label label) {
		messageShowManager.showSuccess(label, "Опции сохранены");
	}

	public void onCalibrationOptionsCancelled(MouseEvent mouseEvent) {
		editFinisher.finishEdit();
		calibrationTable.setItems(calibrationOptionsObservableList());
	}

	public void onTempOptionsCancelled(MouseEvent mouseEvent) {
		editFinisher.finishEdit();
		tempOptionsTable.setItems(tempOptionsObservableList());
	}

	public void onStartClick(MouseEvent mouseEvent) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				programState.startWorking();

				BoardStatus status = programState.getBoardStatus();
				if(status != BoardStatus.CONNECTED) {
					messageShowManager.showError(monitoringMessage, "Не удалось подключиться к плате");
				}

				return null;
			}
		};
		Thread th = new Thread(task);
		th.start();
	}

	public void onStopClick(MouseEvent mouseEvent) {
		programState.stopWorking();
	}


}
