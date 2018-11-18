package com.parcel.warmutil;

import com.parcel.warmutil.model.Sensor;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class SensorTest {

	@Test
	public void testTempValueCount() {
		Sensor sensor = new Sensor();
		sensor.addTempFromSensor(10);
		sensor.addTempFromSensor(20);
		sensor.addTempFromSensor(30);

		assertEquals(20, sensor.getRealTemp().intValue());

		sensor.addTempFromSensor(40);

		assertEquals(30, sensor.getRealTemp().intValue());
	}

	@Test
	public void testCorrection() {
		Sensor sensor = new Sensor();
		sensor.setResistance(2);

		sensor.addTempFromSensor(10);
		sensor.addTempFromSensor(20);
		sensor.addTempFromSensor(30);

		sensor.setCorrection(5);

		assertEquals(25, (int) sensor.getRealTemp());
	}

}
