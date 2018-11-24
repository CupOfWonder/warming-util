package com.parcel.warmutil.model.board;


import com.parcel.harddrivers.Commutator;
import com.parcel.harddrivers.helpers.IntAns;
import com.parcel.warmutil.model.Sensor;
import com.parcel.warmutil.model.helpers.ErrorCode;
import com.parcel.warmutil.model.helpers.LostConnectionHandler;
import com.parcel.warmutil.model.helpers.RelayPosition;

import java.util.ArrayList;
import java.util.List;

import static com.parcel.warmutil.model.helpers.ThreadUtils.sleep;

public class BoardConnector {

	private static final int WRITE_ATTEMPT_COUNT = 6;

	private volatile Commutator commutator;

	private String connectionStatus;

	private List<LostConnectionHandler> lostConnectionHandlers = new ArrayList<>();

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
					return;
				}
				sleep(100);
			}
			//Если после ограниченного кол-ва попыток не удалось изменить положение реле - считаем что соединение утеряно
			handlerLostConnection();
		}
	}

	public void addLostConnectionHandler(LostConnectionHandler handler) {
		lostConnectionHandlers.add(handler);
	}

	private void handlerLostConnection() {
		connectionStatus = null;
		if(lostConnectionHandlers != null) {
			lostConnectionHandlers.forEach(LostConnectionHandler::onLostConnection);
		}
	}
}
