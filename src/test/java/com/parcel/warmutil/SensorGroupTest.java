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
		group.setSensorTempValues(0, 0);

		assertEquals(WarmingState.WARMING, group.getState());
		assertEquals(RelayPosition.ON, group.getRelayPos());
	}

	@Test
	public void sustainTest() {
		SensorGroup group = new SensorGroup(1, 1, 1);
		group.setTempRange(180, 220);
		group.setSensorTempValues(50, 50);

		assertEquals(WarmingState.WARMING, group.getState());

		group.setSensorTempValues(190, 210);
		assertEquals(WarmingState.SUSTAIN, group.getState());
	}

	@Test
	public void coolTest() {
		SensorGroup group = new SensorGroup(1,1,1);
		group.setTempRange(180, 220);
		group.setSensorTempValues(300, 200);

		assertEquals(WarmingState.COOLING, group.getState());
	}

}
