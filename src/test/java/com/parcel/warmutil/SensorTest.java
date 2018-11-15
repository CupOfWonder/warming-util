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

		assertEquals(20, sensor.getRealTemp().intValue());

		sensor.addSensorValue(40);

		assertEquals(30, sensor.getRealTemp().intValue());
	}

	@Test
	public void testMultiply() {
		Sensor sensor = new Sensor();
		sensor.setMultiplyKoeff(2);

		sensor.addSensorValue(10);
		sensor.addSensorValue(20);
		sensor.addSensorValue(30);

		assertEquals(40, sensor.getRealTemp().intValue());
	}


	@Test
	public void testPositiveCorrection() {
		Sensor sensor = new Sensor();
		sensor.setMultiplyKoeff(2);

		sensor.addSensorValue(10);
		sensor.addSensorValue(20);
		sensor.addSensorValue(30);

		sensor.setCorrection(5);

		assertEquals(45, (int) sensor.getRealTemp());
	}

}
