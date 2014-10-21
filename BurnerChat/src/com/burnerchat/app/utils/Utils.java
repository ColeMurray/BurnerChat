package com.burnerchat.app.utils;

import android.util.Log;

public class Utils {
	public static void log(String ... s) {
		for (String l : s) {
			Log.v("BURNER_CHAT--", l);
		}
	}
}
