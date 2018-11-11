package com.parcel.warmutil;

import com.parcel.warmutil.model.Sensor;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class SensorTest {
	@Test
	public void testNegativeCorrection() {
		Sensor sensor = new Sensor();
		sensor.setTempOnSensor(200);
		sensor.setCorrection(-5);

		assertEquals(195, (int) sensor.getRealTemp());
	}

	@Test
	public void testPositiveCorrection() {
		Sensor sensor = new Sensor();
		sensor.setTempOnSensor(200);
		sensor.setCorrection(5);

		assertEquals(205, (int) sensor.getRealTemp());
	}

	@Test
	public void testZeroCorrection() {
		Sensor sensor = new Sensor();
		sensor.setTempOnSensor(200);
		sensor.setCorrection(0);

		assertEquals(200, (int) sensor.getRealTemp());
	}
}
