package com.parcel.warmutil;

import com.parcel.warmutil.model.Sensor;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class SensorTest {

	@Test
	public void testTempValueCount() {
		Sensor sensor = new Sensor();
		sensor.addSensorValue(10);
		sensor.addSensorValue(20);
		sensor.addSensorValue(30);
		sensor.addSensorValue(40);

		assertEquals(30, sensor.getRealTemp().intValue());
	}

	@Test
	public void testPositiveCorrection() {
		Sensor sensor = new Sensor();
		sensor.addSensorValue(10);
		sensor.addSensorValue(20);
		sensor.addSensorValue(30);
		sensor.setCorrection(5);

		assertEquals(25, (int) sensor.getRealTemp());
	}

}
