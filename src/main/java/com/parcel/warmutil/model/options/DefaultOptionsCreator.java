package com.parcel.warmutil.model.options;

import com.parcel.warmutil.model.SensorGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nemo on 11.11.18.
 */
public class DefaultOptionsCreator {
	public static ProgramOptions createDefaultOptions(List<SensorGroup> sensorGroups) {
		ProgramOptions options = new ProgramOptions();

		List<TempRangeOptions> tempOptions = new ArrayList<>();
		List<CalibrationOptions> calibrationOptions = new ArrayList<>();

		for(SensorGroup group : sensorGroups) {
			TempRangeOptions to = new TempRangeOptions(group.getGroupNumber());
			tempOptions.add(to);

			CalibrationOptions co = new CalibrationOptions(group.getGroupNumber());
			calibrationOptions.add(co);
		}

		options.setCalibrationOptions(calibrationOptions);
		options.setTempRangeOptions(tempOptions);
		options.setResistanceOptions(createDefaultResistanceOptions(sensorGroups));

		return options;
	}

	private static List<ResistanceOptions> createDefaultResistanceOptions(List<SensorGroup> sensorGroups) {
		return sensorGroups.stream().map(g -> {
			ResistanceOptions opt = new ResistanceOptions();
			opt.setGroupNumber(g.getGroupNumber());
			if(g.getGroupNumber() == 1) {
				opt.setLeftResistance(976);
				opt.setRightResistance(974);
			} else if(g.getGroupNumber() == 2) {
				opt.setLeftResistance(974);
				opt.setRightResistance(970);
			} else if(g.getGroupNumber() == 3) {
				opt.setLeftResistance(976);
				opt.setRightResistance(976);
			}
			return opt;
		}).collect(Collectors.toList());
	}
}
