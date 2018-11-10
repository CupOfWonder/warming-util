package com.parcel.warmutil.model;


import com.parcel.harddrivers.Commutator;
import com.parcel.harddrivers.helpers.IntAns;
import com.parcel.warmutil.model.helpers.ErrorCode;
import com.parcel.warmutil.model.helpers.RelayPosition;

public class BoardConnector {
	private Commutator commutator;

	private double multiplyKoeff;
	private String connectionStatus;

	public BoardConnector(String boardName, double multiplyKoeff) {
		commutator = new Commutator(boardName);
		this.multiplyKoeff = multiplyKoeff;
	}

	public void connectToBoard() {
		connectionStatus = commutator.findPort();
	}

	public boolean isConnected() {
		System.out.println(connectionStatus);
		return connectionStatus != null && !connectionStatus.equals("ERROR");
	}

	public ErrorCode refreshSensorTemp(Sensor sensor) {
		IntAns ans = commutator.getTemp(sensor.getPinNum(), multiplyKoeff);
		ErrorCode code = ErrorCode.byCode(ans.getError());

		if(code == ErrorCode.NO_ERROR) {
			sensor.setTempOnSensor(ans.getData());
		}
		return code;
	}

	public void writeRelayPosition(int relayNum, RelayPosition pos) {
		commutator.relayWrite(relayNum, pos.isOn());
	}
}
