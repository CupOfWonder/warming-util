package com.parcel.warmutil.model.helpers;

/**
 * Created by nemo on 23.11.18.
 */
public class ThreadUtils {
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
