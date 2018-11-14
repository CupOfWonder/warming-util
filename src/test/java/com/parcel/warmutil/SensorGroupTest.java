package com.parcel.warmutil;


import com.parcel.warmutil.model.SensorGroup;
import com.parcel.warmutil.model.helpers.RelayPosition;
import com.parcel.warmutil.model.helpers.WarmingState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SensorGroupTest {

	@Test
	public void warmTest() {
		SensorGroup group = new SensorGroup(1,1, 1);
		group.setTempRange(180, 220);
		group.getLeftSensor().addSensorValue(10);
		group.getRightSensor().addSensorValue(20);
		group.recountRelayPosition();

		assertEquals(WarmingState.WARMING, group.getState());
		assertEquals(RelayPosition.ON, group.getRelayPos());
	}

	@Test
	public void sustainTest() {
		SensorGroup group = new SensorGroup(1, 1, 1);
		group.setTempRange(180, 220);
		group.getLeftSensor().addSensorValue(10);
		group.getRightSensor().addSensorValue(20);
		group.recountRelayPosition();

		assertEquals(WarmingState.WARMING, group.getState());

		group.getLeftSensor().addSensorValue(200);
		group.getLeftSensor().addSensorValue(200);
		group.getLeftSensor().addSensorValue(200);
		group.getRightSensor().addSensorValue(210);
		group.getRightSensor().addSensorValue(210);
		group.getRightSensor().addSensorValue(210);
		group.recountRelayPosition();

		assertEquals(WarmingState.SUSTAIN, group.getState());
	}

	@Test
	public void coolTest() {
		SensorGroup group = new SensorGroup(1,1,1);
		group.setTempRange(180, 220);
		group.getLeftSensor().addSensorValue(500);
		group.getRightSensor().addSensorValue(500);
		group.recountRelayPosition();

		assertEquals(WarmingState.COOLING, group.getState());
	}

}
