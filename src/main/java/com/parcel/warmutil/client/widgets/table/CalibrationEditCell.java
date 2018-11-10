package com.parcel.warmutil.client.widgets.table;

import com.parcel.warmutil.model.helpers.Constants;
import com.parcel.warmutil.model.options.CalibrationOptions;

/**
 * Created by nemo on 10.11.18.
 */
public class CalibrationEditCell extends SpinnerEditableCell<CalibrationOptions> {

	@Override
	public String itemToString(Integer item) {
		if(item != null) {
			return item.toString();
		} else {
			return null;
		}
	}

	@Override
	public int getMinValue() {
		return Constants.MIN_CALIBRATION;
	}

	@Override
	public int getMaxValue() {
		return Constants.MAX_CALIBRATION;
	}

}
