package com.parcel.warmutil.client.controllers;

import com.parcel.warmutil.client.helpers.StringHelper;
import com.parcel.warmutil.client.helpers.StyleHelper;
import com.parcel.warmutil.model.MainProgramState;
import com.parcel.warmutil.model.SensorGroup;
import com.parcel.warmutil.model.helpers.WarmingState;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.util.List;

import static com.parcel.warmutil.client.helpers.StringHelper.temperatureString;

public class MainAppController {

	@FXML
	public TableColumn<SensorGroup, String> monitoringGroupNum, monitoringLeftTemp,
			monitoringRightTemp;

	@FXML
	public TableColumn<SensorGroup, WarmingState> monitoringGroupState;

	@FXML
	public TableView<SensorGroup> monitoringTable;

	@FXML
	private Button startWarming;

	private MainProgramState programState = MainProgramState.getInstance();

	@FXML
	public void initialize() {
		initMonitoringTable();
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
				if(state != null) {
					setText(StringHelper.textForWarmingState(state));
					setTextFill(StyleHelper.colorForWarmingState(state));
				}
			}
		});

	}


	@FXML
	public void handleStartWarming(MouseEvent mouseEvent) {

	}

	private ObservableList<SensorGroup> sensorObservableList() {
		List<SensorGroup> groups = programState.getAllGroups();
		return FXCollections.observableList(groups);
	}

}
