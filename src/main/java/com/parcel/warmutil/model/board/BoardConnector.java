package com.parcel.warmutil.model.board;


import com.parcel.harddrivers.Commutator;
import com.parcel.harddrivers.helpers.IntAns;
import com.parcel.warmutil.model.Sensor;
import com.parcel.warmutil.model.helpers.ErrorCode;
import com.parcel.warmutil.model.helpers.RelayPosition;

import static com.parcel.warmutil.model.helpers.ThreadUtils.sleep;

public class BoardConnector {

	private static final int WRITE_ATTEMPT_COUNT = 5;

	private volatile Commutator commutator;

	private String connectionStatus;

	public BoardConnector(String boardName) {
		commutator = new Commutator(boardName);
	}

	public void connectToBoard() {
		connectionStatus = commutator.findPort();
	}

	public boolean isConnected() {
		return connectionStatus != null && !connectionStatus.equals("ERROR");
	}

	public synchronized ErrorCode refreshSensorTemp(Sensor sensor) {
		IntAns ans = commutator.getTemp(sensor.getPinNum(), sensor.getResistance());
		ErrorCode code = ErrorCode.byCode(ans.getError());

		if(code == ErrorCode.NO_ERROR) {
			sensor.addTempFromSensor(ans.getData());
		}
		return code;
	}

	public synchronized void writeRelayPosition(int relayNum, RelayPosition pos) {
		if(pos != null) {
			for(int i = 0; i < WRITE_ATTEMPT_COUNT; i++) {
				int codeNum = commutator.relayWrite(relayNum, pos.isOn());
				ErrorCode code = ErrorCode.byCode(codeNum);

				if(code == ErrorCode.NO_ERROR) {
					break;
				}
				sleep(100);
			}

		}
	}
}
