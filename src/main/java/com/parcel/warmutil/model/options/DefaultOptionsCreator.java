package com.parcel.warmutil.model.options;

import com.parcel.warmutil.model.SensorGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nemo on 11.11.18.
 */
public class DefaultOptionsCreator {
	public static ProgramOptions createDefaultOptions(List<SensorGroup> sensorGroups) {
		ProgramOptions options = new ProgramOptions();

		List<TempRangeOptions> tempOptions = new ArrayList<>();
		List<CalibrationOptions> calibrationOptions = new ArrayList<>();
		List<ResistanceOptions> resistanceOptions = new ArrayList<>();

		for(SensorGroup group : sensorGroups) {
			TempRangeOptions to = new TempRangeOptions(group.getGroupNumber());
			tempOptions.add(to);

			CalibrationOptions co = new CalibrationOptions(group.getGroupNumber());
			calibrationOptions.add(co);

			ResistanceOptions mo = new ResistanceOptions(group.getGroupNumber());
			resistanceOptions.add(mo);

		}
		options.setCalibrationOptions(calibrationOptions);
		options.setTempRangeOptions(tempOptions);
		options.setResistanceOptions(resistanceOptions);

		return options;
	}
}
