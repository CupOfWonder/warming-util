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
		group.getLeftSensor().addTempFromSensor(10);
		group.getRightSensor().addTempFromSensor(20);
		group.recountRelayPosition();

		assertEquals(WarmingState.WARMING, group.getState());
		assertEquals(RelayPosition.ON, group.getRelayPos());
	}

	@Test
	public void sustainTest() {
		SensorGroup group = new SensorGroup(1, 1, 1);
		group.setTempRange(180, 220);
		group.getLeftSensor().addTempFromSensor(10);
		group.getRightSensor().addTempFromSensor(20);
		group.recountRelayPosition();

		assertEquals(WarmingState.WARMING, group.getState());

		group.getLeftSensor().addTempFromSensor(200);
		group.getLeftSensor().addTempFromSensor(200);
		group.getLeftSensor().addTempFromSensor(200);
		group.getRightSensor().addTempFromSensor(210);
		group.getRightSensor().addTempFromSensor(210);
		group.getRightSensor().addTempFromSensor(210);
		group.recountRelayPosition();

		assertEquals(WarmingState.SUSTAIN, group.getState());
	}

	@Test
	public void coolTest() {
		SensorGroup group = new SensorGroup(1,1,1);
		group.setTempRange(180, 220);
		group.getLeftSensor().addTempFromSensor(500);
		group.getRightSensor().addTempFromSensor(500);
		group.recountRelayPosition();

		assertEquals(WarmingState.COOLING, group.getState());
	}

}
