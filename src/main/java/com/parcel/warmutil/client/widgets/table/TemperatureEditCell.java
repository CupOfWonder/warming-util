package com.parcel.warmutil.client.widgets.table;

import com.parcel.warmutil.model.helpers.Constants;
import com.parcel.warmutil.model.options.TempRangeOptions;

import static com.parcel.warmutil.client.helpers.StringHelper.temperatureString;

public class TemperatureEditCell extends SpinnerEditableCell<TempRangeOptions> {

	public TemperatureEditCell() {
		super();

	}

	@Override
	public String itemToString(Integer item) {
		return temperatureString(item);
	}

	@Override
	public int getMinValue() {
		return Constants.MIN_TEMP;
	}

	@Override
	public int getMaxValue() {
		return Constants.MAX_TEMP;
	}


}


