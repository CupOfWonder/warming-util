package com.parcel.warmutil.model.helpers;

import com.parcel.warmutil.model.MainProgramState;

public interface StateChangeHandler {
	public void onStateChange(MainProgramState state);
}
