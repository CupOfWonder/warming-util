package com.parcel.warmutil.model.options;

import java.util.List;

public class ProgramOptions {
	private String boardName = "test";
	private double multiplyKoef = 1;
	private List<TempRangeOptions> tempRangeOptions;
	private List<CalibrationOptions> calibrationOptions;

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public double getMultiplyKoef() {
		return multiplyKoef;
	}

	public void setMultiplyKoef(double multiplyKoef) {
		this.multiplyKoef = multiplyKoef;
	}

	public List<TempRangeOptions> getTempRangeOptions() {
		return tempRangeOptions;
	}

	public void setTempRangeOptions(List<TempRangeOptions> tempRangeOptions) {
		this.tempRangeOptions = tempRangeOptions;
	}

	public List<CalibrationOptions> getCalibrationOptions() {
		return calibrationOptions;
	}

	public void setCalibrationOptions(List<CalibrationOptions> calibrationOptions) {
		this.calibrationOptions = calibrationOptions;
	}
}
