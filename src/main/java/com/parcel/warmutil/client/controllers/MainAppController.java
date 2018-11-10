package com.parcel.warmutil.client.controllers;

import com.parcel.warmutil.client.helpers.StringHelper;
import com.parcel.warmutil.client.helpers.StyleHelper;
import com.parcel.warmutil.client.widgets.table.CalibrationEditCell;
import com.parcel.warmutil.client.widgets.table.TemperatureEditCell;
import com.parcel.warmutil.model.MainProgramState;
import com.parcel.warmutil.model.SensorGroup;
import com.parcel.warmutil.model.events.TableEditFinisher;
import com.parcel.warmutil.model.helpers.WarmingState;
import com.parcel.warmutil.model.options.CalibrationOptions;
import com.parcel.warmutil.model.options.TempRangeOptions;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.stream.Collectors;

import static com.parcel.warmutil.client.helpers.StringHelper.temperatureString;

public class MainAppController {

	TableEditFinisher editFinisher = TableEditFinisher.getInstance();

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
	private Button startWarming;

	private MainProgramState programState = MainProgramState.getInstance();

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

	}

	private void initTempRangeTable() {
		tempOptionsTable.getSelectionModel().setCellSelectionEnabled(true);
		tempOptionsTable.setItems(tempOptionsObservableList());

		tempGroupNum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
				Long.toString(cellData.getValue().getGroupNumber()))
		);
		tempMin.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
				cellData.getValue().getMinTemp())
		);
		tempMin.setCellFactory(cellData -> new TemperatureEditCell());
		tempMin.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TempRangeOptions, Integer>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<TempRangeOptions, Integer> event) {
				event.getRowValue().setMinTemp(event.getNewValue());
				tempOptionsTable.refresh();
			}
		});
		tempMax.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
				cellData.getValue().getMaxTemp())
		);
		tempMax.setCellFactory(cellData -> new TemperatureEditCell());
		tempMax.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TempRangeOptions, Integer>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<TempRangeOptions, Integer> event) {
				event.getRowValue().setMaxTemp(event.getNewValue());
				tempOptionsTable.refresh();
			}
		});
	}

	//Таблица "Калибровка датчиков"
	private void initCalibrationTable() {
		calibrationTable.getSelectionModel().setCellSelectionEnabled(true);
		calibrationTable.setItems(tempCalibrationOptionsObservableList());

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
		List<SensorGroup> groups = programState.getAllGroups();
		List<TempRangeOptions> options = groups.stream().map(
				group -> new TempRangeOptions(group.getGroupNumber())
		).collect(Collectors.toList());
		return FXCollections.observableList(options);
	}

	private ObservableList<CalibrationOptions> tempCalibrationOptionsObservableList() {
		List<SensorGroup> groups = programState.getAllGroups();
		List<CalibrationOptions> options = groups.stream().map(
				group -> new CalibrationOptions(group.getGroupNumber())
		).collect(Collectors.toList());
		return FXCollections.observableList(options);
	}

	public void onTempOptionsConfirmed(MouseEvent mouseEvent) {
		editFinisher.finishEdit();
	}

	@FXML
	public void handleStartWarming(MouseEvent mouseEvent) {

	}

}
