package com.parcel.warmutil.model.helpers;


public enum RelayPosition {
	ON(true),
	OFF(false);

	private final boolean value;

	RelayPosition(boolean value) {
		this.value = value;
	}

	public boolean isOn() {
		return value;
	}
}
